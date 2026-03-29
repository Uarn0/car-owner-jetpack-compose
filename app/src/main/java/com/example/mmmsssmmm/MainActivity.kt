package com.example.mmmsssmmm

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
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
import com.example.mmmsssmmm.eventScreen.EventInput
import com.example.mmmsssmmm.eventScreen.EventList
import com.example.mmmsssmmm.mainScreen.MainInput
import com.example.mmmsssmmm.mainScreen.MainList
import com.example.mmmsssmmm.ui.common.EventsVMFactory
import com.example.mmmsssmmm.ui.common.VehiclesVMFactory
import com.example.mmmsssmmm.ui.events.EventsViewModel
import com.example.mmmsssmmm.ui.vehicles.VehiclesViewModel

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
                onDelete = vm::delete
            )
        }

        composable("AddVehicle") {
            val vm: VehiclesViewModel = viewModel(
                factory = VehiclesVMFactory(vehicleRepo)
            )

            MainInput(
                onSave = { name, type, image ->
                    vm.add(1, "X5", 2008, 30.2, "CA2930CI", 2)
                    navController.popBackStack()
                }
            )
        }

        composable(route = "EventList/{vehicleId}",
            arguments = listOf(
                navArgument("vehicleId") { type = NavType.LongType }
            )
        )
        { backStackEntry ->
            val vm: EventsViewModel = viewModel(
                factory = EventsVMFactory(eventRepo),
                viewModelStoreOwner = backStackEntry
            )

            val events = vm.events.collectAsStateWithLifecycle().value

            EventList(
                events = events,
                onAddClick = {
                    val id = backStackEntry.arguments?.getLong("vehicleId")!!
                    navController.navigate("AddEvent/$id")
                },
                onDeleteClick = vm::allDelete
            )
        }

        composable(
            route = "AddEvent/{vehicleId}",
            arguments = listOf(
                navArgument("vehicleId") { type = NavType.LongType}
            )
        ) { backStackEntry ->
            val vm: EventsViewModel = viewModel(
                factory = EventsVMFactory(eventRepo),
                viewModelStoreOwner = backStackEntry
            )

            EventInput(
                onSaveTrip = { name, date, odometer, cost, start, end, dist, isBus ->

                    vm.addTrip(
                        name = name,
                        date = date,
                        odometer = odometer,
                        totalCost = cost,
                        startPoint = start,
                        endPoint = end,
                        distanceKM = dist,
                        isBusiness = isBus
                    )
                    navController.popBackStack()

                },
                onSaveFuel = { name, date, odometer, cost, fuelId, vol, price, isFull ->

                    vm.addFueling(name, date, odometer, cost, fuelId, vol, price, isFull)
                    navController.popBackStack()

                },
                onSaveService = {name, date, odometer, cost, title, station  ->
                    vm.addService(
                        name = name,
                        date = date,
                        odometer = odometer,
                        totalCost = cost,
                        workTitle = title,
                        serviceStation = station
                    )
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
