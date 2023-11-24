package com.hoc081098.channeleventbus.sample.android.ui.register.stepone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hoc081098.channeleventbus.sample.android.ui.register.RegisterSharedVM
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterStepOneScreen(
  registerSharedVM: RegisterSharedVM,
  navigateToRegisterStepTwo: () -> Unit,
  modifier: Modifier = Modifier,
  vm: RegisterStepOneVM = koinViewModel(),
) {
  registerSharedVM.toString()

  val firstName by vm
    .firstNameStateFlow
    .collectAsStateWithLifecycle(context = Dispatchers.Main.immediate)

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      value = firstName.orEmpty(),
      onValueChange = remember { vm::submitFirstName },
      singleLine = true,
      maxLines = 1,
      label = { Text("First name") },
      keyboardActions = KeyboardActions.Default,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    )

    Spacer(modifier = Modifier.height(16.dp))

    ElevatedButton(onClick = navigateToRegisterStepTwo) {
      Text(text = "Next")
    }
  }
}
