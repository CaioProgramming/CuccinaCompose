@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.ilustris.cuccina

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ilustris.cuccina.feature.home.ui.HOME_ROUTE
import com.ilustris.cuccina.feature.profile.ui.PROFILE_ROUTE
import com.ilustris.cuccina.feature.recipe.form.ui.NEW_RECIPE_ROUTE
import com.ilustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE
import com.ilustris.cuccina.feature.recipe.ui.component.getStateComponent
import com.ilustris.cuccina.navigation.BottomNavigation
import com.ilustris.cuccina.navigation.NavigationGraph
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appName = LocalContext.current.getString(R.string.app_name)
            CuccinaTheme {
                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()
                val systemUiController = rememberSystemUiController()

                var title by remember {
                    mutableStateOf(appName)
                }
                var showNavigation by remember {
                    mutableStateOf(true)
                }

                var bottomPadding by remember {
                    mutableStateOf(50.dp)
                }

                val appState = viewModel.state.observeAsState()

                val signInLauncher = rememberLauncherForActivityResult(
                    FirebaseAuthUIActivityResultContract()
                ) { result ->
                    viewModel.validateLogin(result)
                }

                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(AppModule.loginProviders)
                    .build()


                fun showAppBar(state: MainViewModel.MainState?): Boolean {
                    Log.i(javaClass.simpleName, "showAppBar: actual state -> $state")
                    return state != MainViewModel.MainState.HideNavigation
                }

                showNavigation = showAppBar(appState.value)

                Scaffold(bottomBar = {
                    AnimatedVisibility(
                        visible = showNavigation,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        BottomNavigation(navController = navController)
                    }
                }) {
                    if (appState.value == MainViewModel.MainState.RequireLogin) {
                        getStateComponent(state = ViewModelBaseState.RequireAuth, action = {
                            signInLauncher.launch(signInIntent)
                        })
                    } else {
                        NavigationGraph(navController = navController, bottomPadding)
                    }
                }
                LaunchedEffect(navController) {
                    viewModel.checkUser()
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        title = getRouteTitle(backStackEntry.destination.route)
                        bottomPadding = getPaddingForRoute(backStackEntry.destination.route)
                        showNavigation =
                            (backStackEntry.destination.route != START_RECIPE_ROUTE && backStackEntry.destination.route != PROFILE_ROUTE)
                        systemUiController.isStatusBarVisible =
                            (backStackEntry.destination.route != START_RECIPE_ROUTE && backStackEntry.destination.route != PROFILE_ROUTE)

                    }
                }
            }
        }
    }
}

fun getPaddingForRoute(route: String?) =
    when (route) {
        HOME_ROUTE, NEW_RECIPE_ROUTE -> 50.dp
        else -> 0.dp
    }


fun getRouteTitle(route: String?): String {
    if (route == null) return "Cuccina"
    return when (route) {
        HOME_ROUTE -> "Cuccina"
        NEW_RECIPE_ROUTE -> "Nova receita"
        START_RECIPE_ROUTE -> ""
        else -> "Cuccina"
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    CuccinaTheme {


        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painterResource(id = R.drawable.cherry),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            contentDescription = "category",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp)
                        )
                        Text(
                            text = LocalContext.current.getString(R.string.app_name),
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
            bottomBar = { BottomNavigation(navController = navController) }) {
            NavigationGraph(navController = navController, 50.dp)
        }
    }
}