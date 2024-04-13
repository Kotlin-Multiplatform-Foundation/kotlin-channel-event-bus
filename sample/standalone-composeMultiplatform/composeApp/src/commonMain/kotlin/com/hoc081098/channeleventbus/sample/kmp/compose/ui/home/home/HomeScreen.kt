package com.hoc081098.channeleventbus.sample.kmp.compose.ui.home.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hoc081098.kmp.viewmodel.koin.compose.koinKmpViewModel
import com.hoc081098.solivagant.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  vm: HomeVM = koinKmpViewModel(),
) {
  val detailResults by vm.detailResultsStateFlow.collectAsStateWithLifecycle()

  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      modifier = Modifier.matchParentSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        text = "Home",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
      )

      Spacer(modifier = Modifier.height(8.dp))

      ElevatedButton(
        onClick = remember(vm) { vm::navigateToDetail },
      ) {
        Text(
          text = "Click to go to detail",
        )
      }

      LazyColumn(
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(
          items = detailResults,
          key = { it.asString() },
          contentType = { "DetailResult" },
        ) { result ->
          Text(
            modifier = Modifier.fillParentMaxWidth(),
            text = result.asString(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
  }
}
