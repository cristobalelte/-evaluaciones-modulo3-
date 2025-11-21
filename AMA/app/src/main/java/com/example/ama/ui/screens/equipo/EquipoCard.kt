package com.example.ama.ui.screens.equipo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ama.data.dataclass.EquipoAmaItem

@Composable
fun EquipoCard(equipoAmaItem: EquipoAmaItem) {
    Card(
        modifier = Modifier

            .fillMaxWidth()
            .padding(8.dp)
    ) {

        equipoAmaItem.nombre?.let {
            Text(
                text = it, style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        equipoAmaItem.rut?.let {
//            it se refiere a la variable rut
            Text(
                text = it, style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        equipoAmaItem.area?.let {
            Text(
                text = it, style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        equipoAmaItem.lider?.let {
//            it se refier a la variable boolean lider
            if (it) {
                Text(
                    text = "Este integrante es lider", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Text(
                    text = "Este integrante es miembro", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

            }
        }


    }
}