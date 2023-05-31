package com.example.mystoryapps.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.R
import com.example.mystoryapps.databinding.ActivityLoginBinding
import com.example.mystoryapps.db.MyPreferences
import com.example.mystoryapps.viewmodel.LoginViewModel
import com.example.mystoryapps.viewmodel.MainViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory
import com.example.mystoryapps.response.RequestLogin
import com.example.mystoryapps.viewmodel.RepoViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels{
        RepoViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        playAnimation()
        loginAction()

        val pref = MyPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        loginViewModel.message.observe(this) {
            val user = loginViewModel.dataUser.value
            if (user != null) {
                try {
                    checkSession(it, user?.loginResult?.token ?: "", loginViewModel.isError, mainViewModel)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        }

        mainViewModel.getSession().observe(this) { session ->
            if (session) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, getString(R.string.go_login), Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val tvPass = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val btLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val btSignIn = ObjectAnimator.ofFloat(binding.signUp, View.ALPHA, 1f).setDuration(500)

        val bareng = AnimatorSet().apply {
            playTogether(btLogin, btSignIn)
        }

        AnimatorSet().apply {
            playSequentially(tvEmail, email, tvPass, pass, bareng)
            startDelay = 500
        }.start()
    }

    private fun checkSession(message:String, token: String, error: Boolean, mainViewModel: MainViewModel) {
        if (error){
            when (message){
                getString(R.string.unauthorized)-> {
                    Toast.makeText(this, getString(R.string.unauthorized), Toast.LENGTH_SHORT).show()
                    binding.apply {
                        edLoginEmail.setText("")
                        edLoginPassword.setText("")
                    }
                }
                getString(R.string.bad_request) -> Toast.makeText(this, getString(R.string.bad_request), Toast.LENGTH_SHORT).show()
                getString(R.string.internal_server_error) -> Toast.makeText(this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        } else {
            val dataUser = loginViewModel.dataUser.value
            if (dataUser?.loginResult != null) {
                Toast.makeText(
                    this,
                    "${getString(R.string.success_login)} ${dataUser.loginResult.name}",
                    Toast.LENGTH_LONG
                ).show()
                mainViewModel.setSession(true)
                if (token != "") mainViewModel.saveToken(token)
                mainViewModel.saveName(dataUser.loginResult.name)
            } else {
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            try {
                when {
                    email.isEmpty() -> {
                        binding.emailEditTextLayout.error = getString(R.string.email_required)
                    }
                    password.isEmpty() -> {
                        binding.passwordEditTextLayout.error = getString(R.string.password_required)
                    }
                    else -> {
                        if (binding.edLoginEmail.isEmail && binding.edLoginPassword.isPassword) {
                            val requestLogin = RequestLogin(email, password)
                            loginViewModel.login(this, requestLogin)
                        } else {
                            throw Exception(getString(R.string.unauthorized))
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUp.setOnClickListener {
            val intent = Intent(this, RegisActivity::class.java)
            startActivity(intent)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}