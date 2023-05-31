package com.example.mystoryapps.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapps.response.RegResponse
import com.example.mystoryapps.utils.DataDummy
import com.example.mystoryapps.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class RegisViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var regisViewModel: RegisViewModel
    private val dummyRegis = DataDummy.generateDummyRequestRegister()
    private val dummyRegisResponse = DataDummy.generateDummyResponseReg()
    @Before
    fun setUp(){
        regisViewModel = mock(RegisViewModel::class.java)
    }

    @Test
    fun `when loading state should return the right data and Not Null`(){
        val expectedLoadingData = MutableLiveData<Boolean>()
        expectedLoadingData.value = true

        `when`(regisViewModel.isLoading).thenReturn(expectedLoadingData)

        val actualLoading = regisViewModel.isLoading.getOrAwaitValue()

        verify(regisViewModel).isLoading
        assertNotNull(actualLoading)
        assertEquals(expectedLoadingData.value, actualLoading)
    }

    @Test
    fun `verify function register is working`(){
        val expectedResponseRegis = MutableLiveData<RegResponse>()
        expectedResponseRegis.value = dummyRegisResponse
        val mockContext = mock(Context::class.java)

        regisViewModel.regis(mockContext ,dummyRegis)

        verify(regisViewModel).regis(mockContext , dummyRegis)

        `when`(regisViewModel.dataUser).thenReturn(expectedResponseRegis)

        val actualData = regisViewModel.dataUser.getOrAwaitValue()

        verify(regisViewModel).dataUser
        assertNotNull(actualData)
        assertEquals(expectedResponseRegis.value, actualData)
    }
}