package com.example.agify.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.agify.domain.model.ResultModel

@Composable
fun MainItem(
    resultModel: ResultModel
) {
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = resultModel.name)
            Text(text = resultModel.gender?: "")
            Text(text = resultModel.probability.toString())
        }
    }
}