package com.example.agify.presentation.main

import com.example.agify.common.Resource
import com.example.agify.domain.model.ResultModel
import org.junit.Assert.assertEquals
import org.junit.Test

class MainReducerTest {

    private val result = ResultModel(count = 1, gender = "male", name = "zaur", probability = 0.99)

    @Test
    fun `query change only updates query`() {
        val state = MainState(result = result)
        assertEquals(state.copy(query = "abc"), MainReducer.onQueryChanged(state, "abc"))
    }

    @Test
    fun `success replaces result and clears loading and error`() {
        val state = MainState(isLoading = true, error = "x")
        val reduced = MainReducer.onResult(state, Resource.Success(result))
        assertEquals(MainState(result = result), reduced)
    }

    @Test
    fun `loading and error keep previous result`() {
        val state = MainState(result = result)
        assertEquals(result, MainReducer.onResult(state, Resource.Loading()).result)
        val error = MainReducer.onResult(state, Resource.Error("boom"))
        assertEquals(result, error.result)
        assertEquals("boom", error.error)
    }
}
