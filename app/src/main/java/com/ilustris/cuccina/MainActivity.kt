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
import androidx.compose.foundation.layout.*
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
import com.ilustris.cuccina.feature.home.ui.HOME_ROUTE
import com.ilustris.cuccina.feature.recipe.form.ui.NEW_RECIPE_ROUTE
import com.ilustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE
import com.ilustris.cuccina.navigation.BottomNavigation
import com.ilustris.cuccina.navigation.NavigationGraph
import com.ilustris.cuccina.ui.theme.CuccinaTheme
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

                var title by remember {
                    mutableStateOf(appName)
                }
                var showNavigation by remember {
                    mutableStateOf(true)
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


                if (appState.value == MainViewModel.MainState.RequireLogin) {
                    signInLauncher.launch(signInIntent)
                }


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
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Você precisa estar logado para acessar o app...")
                            Button(onClick = {
                                signInLauncher.launch(signInIntent)
                            }) {
                                Text(text = "Fazer login")
                            }
                        }
                    } else {
                        NavigationGraph(navController = navController, paddingValues = it)
                    }
                }
                LaunchedEffect(navController) {
                    viewModel.checkUser()
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        title = getRouteTitle(backStackEntry.destination.route)
                        showNavigation = (backStackEntry.destination.route != START_RECIPE_ROUTE)
                    }
                }
            }
        }
    }
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
            NavigationGraph(navController = navController, paddingValues = it)
        }
    }
}