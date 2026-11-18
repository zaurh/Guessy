package com.example.agify.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.agify.domain.model.ResultModel
import com.example.agify.ui.theme.AgifyTheme

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel()
) {
    MainScreen(
        state = viewModel.state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun MainScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        TextField(
            modifier = Modifier.align(Alignment.BottomCenter),
            value = state.query,
            onValueChange = { onIntent(MainIntent.QueryChanged(it)) },
            placeholder = { Text(text = "Enter name...") }
        )
        state.result?.let { result ->
            ResultContent(resultModel = result)
        }
    }
}

@Composable
private fun ResultContent(
    resultModel: ResultModel
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = resultModel.name)
            Text(text = resultModel.gender ?: "")
            Text(text = resultModel.probability.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    AgifyTheme {
        MainScreen(
            state = MainState(
                query = "Zaur",
                result = ResultModel(count = 1, gender = "male", name = "Zaur", probability = 0.99)
            ),
            onIntent = {}
        )
    }
}
