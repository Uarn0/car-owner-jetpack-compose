package com.example.mmmsssmmm

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.EventEntity
import com.example.mmmsssmmm.data.VehiclesEntity
import com.example.mmmsssmmm.data.toDomain
import com.example.mmmsssmmm.eventScreen.EventInput
import com.example.mmmsssmmm.eventScreen.EventList
import com.example.mmmsssmmm.mainScreen.MainInput
import com.example.mmmsssmmm.mainScreen.MainList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Main()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }

    val vehiclesDao = remember { db.vehicleDao() }
    val eventDao = remember { db.eventDao() }

    val scope = rememberCoroutineScope()

    val vehicles = vehiclesDao.observeVehicles()
        .map { list -> list.map { it.toDomain() } }
        .collectAsState(initial = emptyList())
        .value

    NavHost(navController = navController, startDestination = "VehicleList") {

        composable("VehicleList") {
            MainList(
                vehicles = vehicles,
                onDetailsClick = { v -> navController.navigate("EventList/${v}") },
                onAddClick = { navController.navigate("AddVehicle") },
                onDelete = { v -> scope.launch { vehiclesDao.deleteById(v) } }
            )
        }

        composable("AddVehicle") {
            MainInput(onSave = { name, type, image ->
                scope.launch {
                    vehiclesDao.insert(
                        VehiclesEntity(name = name, type = type, image = image)
                    )
                    navController.popBackStack()
                }
            })
        }

        composable("EventList/{vehicleId}") { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId")!!.toLong()

            val events = eventDao.observeEvents(vehicleId)
                .map { list -> list.map { it.toDomain() } }
                .collectAsState(initial = emptyList())
                .value

            EventList(
                events = events,
                onAddClick = { navController.navigate("AddEvent/$vehicleId") },
                onDeleteClick = { e -> scope.launch { eventDao.deleteById(e) } }
            )
        }

        composable("AddEvent/{vehicleId}") { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId")!!.toLong()
            EventInput(onSave = { name, type, date ->
                scope.launch {
                    eventDao.insert(
                        EventEntity(vehicleId = vehicleId, name = name, type = type, time = date)
                    )
                    navController.popBackStack()
                }
            })
        }
    }
}
