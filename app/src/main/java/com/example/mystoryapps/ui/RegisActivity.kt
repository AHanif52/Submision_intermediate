package com.example.mystoryapps.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.R
import com.example.mystoryapps.databinding.ActivityRegisBinding
import com.example.mystoryapps.db.MyPreferences
import com.example.mystoryapps.viewmodel.MainViewModel
import com.example.mystoryapps.viewmodel.RegisViewModel
import com.example.mystoryapps.viewmodel.ViewModelFactory
import com.example.mystoryapps.response.RequestReg
import com.example.mystoryapps.viewmodel.RepoViewModelFactory

class RegisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisBinding
    private val regisViewModel: RegisViewModel by viewModels{
        RepoViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        playAnimation()
        setActionBar()
        regAction()

        val pref = MyPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getSession().observe(this) { session ->
            if (session) {
                val intent = Intent(this@RegisActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        regisViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val tvPass = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val btRegis = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(title, tvName, name ,tvEmail, email, tvPass, pass, btRegis)
            startDelay = 500
        }.start()
    }

    private fun regAction() {
        binding.btnRegister.setOnClickListener {
            binding.apply {
                edRegisterName.clearFocus()
                edRegisterEmail.clearFocus()
                edRegisterPassword.clearFocus()
            }

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.name_required)
                }

                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_required)
                }

                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.password_required)
                }

                else -> {
                    if (binding.edRegisterName.isName && binding.edRegisterEmail.isEmail && binding.edRegisterPassword.isPassword) {
                        val requestReg = RequestReg(name, email, password)
                        regisViewModel.regis(this, requestReg)

                        regisViewModel.dataUser.observe(this) {
                            Toast.makeText(
                                this@RegisActivity,
                                getString(R.string.register_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            if (!regisViewModel.isError) {
                                val moveIntent =
                                    Intent(this@RegisActivity, LoginActivity::class.java)
                                startActivity(moveIntent)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @SuppressLint("RestrictedApi")
    private fun setActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}