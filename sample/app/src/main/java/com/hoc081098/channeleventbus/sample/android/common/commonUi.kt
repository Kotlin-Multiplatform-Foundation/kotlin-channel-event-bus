@file:OptIn(ExperimentalMaterialApi::class)

package com.hoc081098.channeleventbus.sample.android.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun EmptyProducts(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(text = "No products")
  }
}

@Composable
internal fun LoadingIndicator(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator()
  }
}

@Composable
internal fun ErrorMessageAndRetryButton(
  errorMessage: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(text = errorMessage)

      Spacer(modifier = Modifier.height(16.dp))

      Button(onClick = onRetry) {
        Text(text = "Retry")
      }
    }
  }
}
