package com.example.ama.ui.screens.equipo

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ama.data.viewmodel.EquipoViewModel
import com.example.ama.data.viewmodel.EquipoViewModelFactory

@Composable
fun EquipoList(modifier: Modifier = Modifier) {
    val equipoListViewModel: EquipoViewModel = viewModel(
        factory = EquipoViewModelFactory()
    )
    val equipoList by equipoListViewModel.equipoList.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
//                Log.d("ProductList", "Index: ${(index?:0)+1}, Size: ${productList.size}")
                if ((index?:0)+1 == equipoList.size) {
                    equipoListViewModel.getNextInteger()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
    ){
        items(equipoList.size) { index ->
            EquipoCard(equipoList[index])
        }

    }

}