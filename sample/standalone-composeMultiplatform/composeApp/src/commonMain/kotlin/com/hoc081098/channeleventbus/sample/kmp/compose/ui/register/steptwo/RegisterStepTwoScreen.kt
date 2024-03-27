package com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.steptwo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.Gender
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.RegisterSharedVM
import com.hoc081098.channeleventbus.sample.kmp.compose.ui.register.displayName
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers

@Composable
fun RegisterStepTwoScreen(
  registerSharedVM: RegisterSharedVM,
  modifier: Modifier = Modifier,
  vm: RegisterStepTwoVM = koinKmpViewModel(),
) {
  registerSharedVM.toString()

  val selectedGender by vm
    .genderStateFlow
    .collectAsStateWithLifecycle(context = Dispatchers.Main.immediate)

  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    GenderSection(
      modifier = Modifier.fillMaxWidth(),
      selectedGender = selectedGender,
      onGenderChanged = remember { vm::onGenderChanged },
    )

    Spacer(modifier = Modifier.weight(1f))

    ElevatedButton(onClick = { }) {
      Text(text = "Next")
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
private fun GenderSection(
  selectedGender: Gender?,
  onGenderChanged: (Gender) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier) {
    Gender.entries.forEach { item ->
      val onClick = { onGenderChanged(item) }
      val selected = selectedGender == item

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .selectable(
            selected = selected,
            onClick = onClick,
          )
          .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
      ) {
        RadioButton(
          selected = selected,
          onClick = onClick,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          modifier = Modifier.weight(1f),
          text = item.displayName,
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
