package com.hoc081098.channeleventbus.sample.android.ui.register.stepone

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterStepOneScreen(
  modifier: Modifier = Modifier,
  vm: RegisterStepOneVM = koinViewModel(),
) {
  val firstName by vm
    .firstNameStateFlow
    .collectAsStateWithLifecycle(context = Dispatchers.Main.immediate)

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = firstName.orEmpty())

    ElevatedButton(
      onClick = {
        vm.submitFirstName("Hoc")
      },
    ) {
    }
  }
}
