package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.stepthree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hoc081098.channeleventbus.sample.kmp.compose.common.CollectWithLifecycleEffect
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.RegisterSharedVM
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.RegisterUiState
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.displayName
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle

@Suppress("LongMethod")
@Composable
fun RegisterStepThreeScreen(
  registerSharedVM: RegisterSharedVM,
  modifier: Modifier = Modifier,
  vm: RegisterStepThreeVM = koinKmpViewModel(),
) {
  val registerUiState by registerSharedVM
    .uiStateFlow
    .collectAsStateWithLifecycle()

  val uiState by vm.uiStateFlow.collectAsStateWithLifecycle()

  vm.singleEventFlow.CollectWithLifecycleEffect { event ->
    when (event) {
      is RegisterStepThreeSingleEvent.Failure -> {
        // TODO: Show toast
      }

      RegisterStepThreeSingleEvent.Success -> {
        // TODO: Show toast
      }
    }
  }

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    when (val s = registerUiState) {
      is RegisterUiState.Filled -> {
        RegisterInfo(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          registerUiState = s,
        )
      }

      RegisterUiState.Unfilled -> {
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          text = "Please fill all information",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
        )
      }
    }

    Spacer(modifier = Modifier.weight(1f))

    when (uiState) {
      RegisterStepThreeUiState.Idle,
      is RegisterStepThreeUiState.Failure,
      RegisterStepThreeUiState.Success,
      -> Unit

      RegisterStepThreeUiState.Registering ->
        CircularProgressIndicator(
          modifier = Modifier.padding(16.dp),
        )
    }

    ElevatedButton(
      enabled = registerUiState is RegisterUiState.Filled,
      onClick = {
        vm.register(
          registerSharedVM
            .uiStateFlow
            .value,
        )
      },
    ) {
      Text(text = "Register")
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun RegisterInfo(
  registerUiState: RegisterUiState.Filled,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier,
  ) {
    SimpleTile(
      title = "First name: ",
      content = registerUiState.firstName,
    )

    Spacer(modifier = Modifier.height(8.dp))

    SimpleTile(
      title = "Last name: ",
      content = registerUiState.lastName,
    )

    Spacer(modifier = Modifier.height(8.dp))

    SimpleTile(
      title = "Gender: ",
      content = registerUiState.gender.displayName,
    )

    Spacer(modifier = Modifier.height(8.dp))
  }
}

@Composable
private fun SimpleTile(
  title: String,
  content: String,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth(),
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Start,
    )

    Spacer(modifier = Modifier.width(8.dp))

    Text(
      modifier = Modifier
        .weight(1f),
      text = content,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Normal,
      textAlign = TextAlign.End,
    )
  }
}
