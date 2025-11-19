package com.example.listapitillos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listapitillos.ui.theme.ListaPitillosTheme

class MainActivity : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager(applicationContext)
        viewModelFactory = MainViewModelFactory(dataStoreManager)

        setContent {
            ListaPitillosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        factory = viewModelFactory,
                        paddingValues = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(factory: ViewModelProvider.Factory, paddingValues: PaddingValues,
             modifier: Modifier = Modifier) {

    val viewModel: DateViewModel = viewModel(factory = factory)
    val savedDates by viewModel.datesState.collectAsState()

    Column(
        modifier = modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Registro de Momentos Pitillo",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                // Llama a la función para guardar la nueva fecha
                onClick = viewModel::saveNextDate,
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar Momento")
            }
        }

            Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Fechas Guardadas (${savedDates.size}):",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

            if (savedDates.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        "No hay fechas registradas. ¡Guarda tu primer momento!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            } else {
                // LazyColumn es eficiente para listas
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    // Itera sobre la lista de fechas observadas
                    items(items = savedDates, key = { it.time }) { date ->

                        ListItem(
                            { Text(text = date.toStringFormat()) }
                        )
                    }
                }
            }


    }

}
