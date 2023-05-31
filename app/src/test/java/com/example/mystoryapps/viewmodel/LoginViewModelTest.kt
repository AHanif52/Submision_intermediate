package com.example.mystoryapps.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapps.response.LoginResponse
import com.example.mystoryapps.utils.DataDummy
import com.example.mystoryapps.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLogin = DataDummy.generateDummyResponseLogin()

    @Before
    fun setUp(){
        loginViewModel = mock(LoginViewModel::class.java)
    }

    @Test
    fun `when login message should return the right data and Not Null`(){
        val expectedLoginMessage = MutableLiveData<String>()
        expectedLoginMessage.value = "Login Successfully"

        `when`(loginViewModel.message).thenReturn(expectedLoginMessage)

        val actualMessage = loginViewModel.message.getOrAwaitValue()
        verify(loginViewModel).message
        assertNotNull(actualMessage)
        assertTrue(actualMessage.isNotEmpty())
        assertEquals(expectedLoginMessage.value, actualMessage)
    }

    @Test
    fun `when login dataUser should return the right data and Not Null`(){
        val expectedLoginDataUser = MutableLiveData<LoginResponse>()
        expectedLoginDataUser.value = dummyLogin

        `when`(loginViewModel.dataUser).thenReturn(expectedLoginDataUser)

        val actualDataUser = loginViewModel.dataUser.getOrAwaitValue()

        verify(loginViewModel).dataUser
        assertNotNull(actualDataUser)
        assertEquals(expectedLoginDataUser.value, actualDataUser)
    }

    @Test
    fun `when loading state should return the right data and Not Null`(){
        val expectedLoadingData = MutableLiveData<Boolean>()
        expectedLoadingData.value = true

        `when`(loginViewModel.isLoading).thenReturn(expectedLoadingData)

        val actualLoading = loginViewModel.isLoading.getOrAwaitValue()

        verify(loginViewModel).isLoading
        assertNotNull(actualLoading)
        assertEquals(expectedLoadingData.value, actualLoading)
    }

    @Test
    fun `verify function login is working`(){
        val dummyRequestLogin = DataDummy.generateDummyRequestLogin()
        val dummyResponseLogin = DataDummy.generateDummyResponseLogin()

        val expectedResponseLogin = MutableLiveData<LoginResponse>()
        expectedResponseLogin.value = dummyResponseLogin
        val mockContext = mock(Context::class.java)

        loginViewModel.login(mockContext, dummyRequestLogin)

        verify(loginViewModel).login(mockContext, dummyRequestLogin)

        `when`(loginViewModel.dataUser).thenReturn(expectedResponseLogin)

        val actualData = loginViewModel.dataUser.getOrAwaitValue()

        verify(loginViewModel).dataUser
        assertNotNull(expectedResponseLogin)
        assertEquals(expectedResponseLogin.value, actualData)
    }
}