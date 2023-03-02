package com.example.agify.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.agify.domain.model.ResultModel

@Composable
fun MainView(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    Box(modifier = Modifier.fillMaxSize()){
        TextField(
            modifier = Modifier.align(Alignment.BottomCenter),
            value = viewModel.searchQuery.value,
            onValueChange = viewModel::onSearch,
            placeholder = { Text(text = "Enter name...")}
        )
        state.result?.let { result ->
            MainItem(resultModel = result)
        }
        }
    }