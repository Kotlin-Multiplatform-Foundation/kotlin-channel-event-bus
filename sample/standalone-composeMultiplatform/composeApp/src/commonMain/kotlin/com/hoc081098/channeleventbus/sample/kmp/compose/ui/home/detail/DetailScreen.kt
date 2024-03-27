package com.hoc081098.channeleventbus.sample.kmp.compose.ui.home.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hoc081098.channeleventbus.sample.kmp.compose.common.CollectWithLifecycleEffect
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers

@Composable
fun DetailScreen(
  navigateBack: () -> Unit,
  modifier: Modifier = Modifier,
  vm: DetailVM = koinKmpViewModel(),
) {
  val currentNavigateBack by rememberUpdatedState(navigateBack)
  vm.singleEventFlow.CollectWithLifecycleEffect { event ->
    when (event) {
      DetailSingleEvent.Complete -> currentNavigateBack()
    }
  }

  val text by vm
    .textStateFlow
    .collectAsStateWithLifecycle(context = Dispatchers.Main.immediate)

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.matchParentSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top,
    ) {
      Text(
        text = "Detail",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
      )

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        value = text,
        onValueChange = remember { vm::onTextChanged },
        singleLine = true,
        maxLines = 1,
        label = { Text("Enter the text") },
        keyboardActions = KeyboardActions.Default,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      )

      Spacer(modifier = Modifier.height(16.dp))

      ElevatedButton(
        enabled = text.isNotBlank(),
        onClick = remember { vm::sendResultToHome },
      ) {
        Text(text = "Send to home screen")
      }
    }
  }
}
