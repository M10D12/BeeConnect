package com.example.beeconnect

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar OpenStreetMap
        Configuration.getInstance().load(
            applicationContext,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContent {
            BeeConnectApp()
        }
    }
}

@Composable
fun BeeConnectApp() {
    Scaffold(
        topBar = { BeeConnectTopBar() },
        bottomBar = { BeeConnectBottomNavigation() },
        floatingActionButton = { AddApiaryButton() }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ApiaryList(
                listOf(
                    Apiary("Apiário Lamego", "Lamego", R.drawable.ic_launcher_foreground),
                    Apiary("Apiário Aveiro", "Aveiro", null)
                )
            )
        }
    }
}

@Composable
fun BeeConnectTopBar() {
    TopAppBar(
        title = { Text("BeeConnect") },
        navigationIcon = {
            IconButton(onClick = { /* Handle menu click */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        backgroundColor = Color(0xFFFFC107), // Cor amarela
        contentColor = Color.Black
    )
}

@Composable
fun AddApiaryButton() {
    FloatingActionButton(onClick = { /* Adicionar apiário */ }, backgroundColor = Color.Black) {
        Icon(Icons.Default.Add, contentDescription = "Adicionar Apiário", tint = Color.White)
    }
}

@Composable
fun OpenStreetMapView() {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx: Context ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK) // Fonte do mapa
                    setMultiTouchControls(true) // Permitir zoom com gestos
                    controller.setZoom(15.0) // Nível de zoom inicial
                    controller.setCenter(GeoPoint(40.6413, -8.6531)) // Posição inicial (Aveiro)
                }
            }
        )
    }
}

@Composable
fun ApiaryList(apiaries: List<Apiary>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(apiaries.size) { index ->
            ApiaryCard(apiary = apiaries[index])
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ApiaryCard(apiary: Apiary) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (apiary.imageRes != null) {
                Image(
                    painter = painterResource(id = apiary.imageRes),
                    contentDescription = apiary.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Mapa")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = apiary.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = apiary.location, fontSize = 16.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Ver mais */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Ver mais")
            }
        }
    }
}

@Composable
fun BeeConnectBottomNavigation() {
    BottomNavigation(
        backgroundColor = Color(0xFFFFC107)
    ) {
        BottomNavigationItem(
            selected = true,
            onClick = { /* Navegar para apiários */ },
            icon = { Icon(Icons.Default.Menu, contentDescription = "Apiários") },
            label = { Text("Apiários") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { /* Navegar para estatísticas */ },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Estatísticas") },
            label = { Text("Estatísticas") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = { /* Navegar para configurações */ },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Configurações") },
            label = { Text("Configurações") }
        )
    }
}

data class Apiary(val name: String, val location: String, val imageRes: Int?)

@Preview(showBackground = true)
@Composable
fun PreviewBeeConnectApp() {
    BeeConnectApp()
}
