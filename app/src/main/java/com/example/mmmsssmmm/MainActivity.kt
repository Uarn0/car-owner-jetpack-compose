package com.example.mmmsssmmm

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mmmsssmmm.data.Graph
import com.example.mmmsssmmm.data.Graph.eventRepo
import com.example.mmmsssmmm.data.Graph.vehicleRepo
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem
import com.example.mmmsssmmm.eventScreen.EventInput
import com.example.mmmsssmmm.eventScreen.EventList
import com.example.mmmsssmmm.mainScreen.MainInput
import com.example.mmmsssmmm.mainScreen.MainList
import com.example.mmmsssmmm.statsScreen.StatsScreen
import com.example.mmmsssmmm.statsScreen.StatsViewModel
import com.example.mmmsssmmm.subEventsListScreen.SubEventInput
import com.example.mmmsssmmm.subEventsListScreen.SubEventsListScreen
import com.example.mmmsssmmm.ui.common.EventsVMFactory
import com.example.mmmsssmmm.ui.common.VehiclesVMFactory
import com.example.mmmsssmmm.ui.events.EventsViewModel
import com.example.mmmsssmmm.ui.events.SubEventViewModel
import com.example.mmmsssmmm.ui.vehicles.VehiclesViewModel
import kotlin.collections.emptyList

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Graph.init(applicationContext)
        setContent {
            Main()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "VehicleList") {

        composable("VehicleList") {
            val vm: VehiclesViewModel = viewModel(
                factory = VehiclesVMFactory(vehicleRepo)
            )
            val vehicles = vm.vehicles.collectAsStateWithLifecycle().value


            MainList(
                vehicles = vehicles,
                onDetailsClick = { id -> navController.navigate("EventList/$id")},
                onAddClick = { navController.navigate("AddVehicle") },
                onDelete = vm::delete,
                onStatsClick = { navController.navigate("StatsSubEvent") }
            )
        }

        composable("AddVehicle") {
            val vm: VehiclesViewModel = viewModel(factory = VehiclesVMFactory(vehicleRepo))

            val brands by vm.brands.collectAsStateWithLifecycle(initialValue = emptyList())
            val models by vm.modelsForSelectedBrand.collectAsStateWithLifecycle(initialValue = emptyList())

            MainInput(
                brands = brands,
                models = models,
                onBrandSelected = { brandId ->
                    vm.loadModelsForBrand(brandId)
                },
                onSave = { modelId, year, tank, plate, imageUri ->
                    vm.add(modelId, year, tank, plate, imageUri)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "EventList/{vehicleId}",
            arguments = listOf(
                navArgument("vehicleId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getLong("vehicleId") ?: 0L

            val vm: EventsViewModel = viewModel(
                factory = EventsVMFactory(eventRepo),
                viewModelStoreOwner = backStackEntry
            )

            val events = vm.baseEvents.collectAsStateWithLifecycle().value

            EventList(
                events = events,

                onEventClick = { eventId ->
                    navController.navigate("SubEventsList/$eventId")
                },

                onAddEventClick = {
                    navController.navigate("AddEvent/$vehicleId")
                },

                onDeleteEventClick = { eventId ->
                    vm.allDelete(eventId)
                }
            )
        }
        composable(
            route = "AddEvent/{vehicleId}",
            arguments = listOf(navArgument("vehicleId") { type = NavType.LongType })
        ) { backStackEntry ->
            val vm: EventsViewModel = viewModel(
                factory = EventsVMFactory(eventRepo),
                viewModelStoreOwner = backStackEntry
            )

            EventInput(
                vm = vm,
                onCancel = { navController.popBackStack() },
                onEventCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "SubEventsList/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L
            val vm: SubEventViewModel = viewModel(factory = EventsVMFactory(eventRepo))

            val subEvent = vm.currentSubEvent.collectAsStateWithLifecycle().value

            if (subEvent != null) {
                SubEventsListScreen(
                    subEvents = subEvent,
                    onAddSubEventClick = {
                        navController.navigate("AddSubEvent/$eventId")
                    },
                    onDeleteSubEventClick = { item ->
                        when(item) {
                            is VehicleHistoryItem.Trip -> vm.tripDelete()
                            is VehicleHistoryItem.Fueling -> vm.fuelDelete()
                            is VehicleHistoryItem.Service -> vm.serviceDelete()
                            else -> {}
                        }
                    }
                )
            }
        }
        composable(
            route = "AddSubEvent/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) {
            val vm: SubEventViewModel = viewModel(factory = EventsVMFactory(eventRepo))

            SubEventInput(
                vm = vm,
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "StatsSubEvent",
        ) {
            val vm: StatsViewModel = viewModel(factory = EventsVMFactory(eventRepo))
            StatsScreen(
                vm = vm
            )
        }
    }
}
