package com.hoc081098.channeleventbus.sample.android.ui.home.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
  navigateToDetail: () -> Unit,
  modifier: Modifier = Modifier,
  vm: HomeVM = koinViewModel(),
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
        onClick = navigateToDetail,
      ) {
        Text(
          text = "Click to go to detail",
        )
      }

      LazyColumn(
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(all = 16.dp),
      ) {
        itemsIndexed(
          items = detailResults,
          key = { _, item -> item },
          contentType = { _, _ -> "DetailResult" },
        ) { index, result ->
          Text(
            modifier = Modifier.fillParentMaxWidth(),
            text = result,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )

          if (index != detailResults.lastIndex) {
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    }
  }
}
