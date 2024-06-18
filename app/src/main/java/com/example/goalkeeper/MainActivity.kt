package com.example.goalkeeper

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.goalkeeper.component.GoalKeeperAlertDialog
import com.example.goalkeeper.component.GoalKeeperButton
import com.example.goalkeeper.nav.Routes
import com.example.goalkeeper.notification.createNotificationChannel
import com.example.goalkeeper.screen.MainScreen
import com.example.goalkeeper.screen.RegisterScreen
import com.example.goalkeeper.screen.WelcomeScreen
import com.example.goalkeeper.style.AppStyles.loginTextStyle
import com.example.goalkeeper.ui.theme.GoalKeeperTheme
import com.example.goalkeeper.viewmodel.GoalKeeperViewModel
import com.example.goalkeeper.viewmodel.GoalKeeperViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoalKeeperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var requestAlarmPermission by remember {
                        mutableStateOf(false)
                    }

                    if (!requestAlarmPermission) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            val alarmManager =
                                getSystemService(Context.ALARM_SERVICE) as AlarmManager
                            if (!alarmManager.canScheduleExactAlarms()) {
                                GoalKeeperAlertDialog(
                                    title = "알림 권한 필요",
                                    text = "알림 권한이 필요합니다",
                                    onDismissRequest = { requestAlarmPermission = true },
                                    confirmButton = {
                                        GoalKeeperButton(
                                            width = 80,
                                            height = 40,
                                            text = "허용",
                                            textStyle = loginTextStyle
                                        ) {
                                            val intent =
                                                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                                            startActivity(intent)
                                            requestAlarmPermission = true
                                        }
                                    },
                                    dismissButton = {
                                        GoalKeeperButton(
                                            width = 80,
                                            height = 40,
                                            text = "허용 안함",
                                            textStyle = loginTextStyle
                                        ) {
                                            requestAlarmPermission = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { context as ViewModelStoreOwner }
}

val LocalNavGraphViewModelStoreOwner =
    staticCompositionLocalOf<ViewModelStoreOwner> {
        error("Undefined")
    }

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val channelId = "routine_channel"

    val navStoreOwner = rememberViewModelStoreOwner()
    val goalKeeperDB = Firebase.database.getReference("goalkeeper")

    val viewModel: GoalKeeperViewModel =
        viewModel(factory = GoalKeeperViewModelFactory(goalKeeperDB))

    createNotificationChannel(context, channelId)

    CompositionLocalProvider(
        LocalNavGraphViewModelStoreOwner provides navStoreOwner
    ) {
        NavHost(navController, startDestination = Routes.Welcome.route) {
            composable(Routes.Welcome.route) { WelcomeScreen(navController) }
            composable(Routes.Main.route) { MainScreen() }
            composable(Routes.Register.route) { RegisterScreen(navController) }
        }
    }
}