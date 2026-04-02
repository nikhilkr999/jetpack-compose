package com.nikhil.flowcus

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nikhil.flowcus.service.AppBlockingService
import com.nikhil.flowcus.service.FocusLockManager
import com.nikhil.flowcus.service.FocusOverlayManager
import com.nikhil.flowcus.ui.feature_analytics.AnalyticsScreen
import com.nikhil.flowcus.ui.feature_tasks.TasksScreen
import com.nikhil.flowcus.ui.feature_timer.TimerScreen
import com.nikhil.flowcus.ui.feature_timer.TimerViewModel
import com.nikhil.flowcus.ui.theme.FlowcusTheme
import dagger.hilt.android.AndroidEntryPoint

private val BackgroundDark   = Color(0xFF111116)
private val SurfaceDark      = Color(0xFF1C1C24)
private val Amber            = Color(0xFFE8C47A)
private val TextMuted        = Color(0xFF555555)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val overlayManager by lazy { FocusOverlayManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup App Lock components
        AppBlockingService.overlayManager = overlayManager

        setContent {
            FlowcusTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { _ -> }
                )

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                val navController = rememberNavController()
                val timerViewModel: TimerViewModel = hiltViewModel()
                val timerState by timerViewModel.uiState.collectAsState()

                // Sync App Lock and DND state
                LaunchedEffect(timerState.isRunning, timerState.appLockEnabled) {
                    if (timerState.isRunning && timerState.appLockEnabled) {
                        FocusLockManager.lockToApp(this@MainActivity)
                        AppBlockingService.isTimerRunning.value = true
                        
                        if (!overlayManager.canDrawOverlays()) {
                            overlayManager.requestOverlayPermission(this@MainActivity)
                        }
                    } else {
                        FocusLockManager.unlockFromApp(this@MainActivity)
                        AppBlockingService.isTimerRunning.value = false
                        overlayManager.hideOverlay()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundDark,
                    bottomBar = {
                        NavigationBar(
                            containerColor = BackgroundDark,
                            contentColor = TextMuted
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination?.route

                            val items = listOf(
                                NavigationItem("Timer", "timer", Icons.Default.PlayArrow),
                                NavigationItem("Tasks", "tasks", Icons.AutoMirrored.Filled.List),
                                NavigationItem("Stats", "analytics", Icons.Default.DateRange)
                            )

                            items.forEach { item ->
                                val selected = currentDestination?.startsWith(item.route) == true
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { 
                                        Icon(
                                            item.icon, 
                                            contentDescription = item.label,
                                            tint = if (selected) Amber else TextMuted
                                        ) 
                                    },
                                    label = { 
                                        Text(
                                            item.label,
                                            color = if (selected) Amber else TextMuted
                                        ) 
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Amber,
                                        selectedTextColor = Amber,
                                        unselectedIconColor = TextMuted,
                                        unselectedTextColor = TextMuted,
                                        indicatorColor = SurfaceDark
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "timer",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = "timer?taskId={taskId}",
                            arguments = listOf(
                                navArgument("taskId") { 
                                    type = NavType.StringType
                                    defaultValue = "-1"
                                }
                            )
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: -1
                            if (taskId != -1) {
                                LaunchedEffect(taskId) {
                                    timerViewModel.onTaskIdReceived(taskId)
                                }
                            }
                            
                            TimerScreen(viewModel = timerViewModel) 
                        }
                        composable("tasks") { 
                            TasksScreen(
                                timerViewModel = timerViewModel,
                                onFocusTask = { task ->
                                    navController.navigate("timer?taskId=${task.id}") {
                                        popUpTo(navController.graph.findStartDestination().id)
                                        launchSingleTop = true
                                    }
                                }
                            ) 
                        }
                        composable("analytics") { AnalyticsScreen() }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        overlayManager.hideOverlay()
    }
}

data class NavigationItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
