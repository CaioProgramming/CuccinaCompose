package com.ilustris.cuccina

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.ilustris.cuccina.feature.profile.domain.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val userService: UserService) : ViewModel() {

    sealed class MainState {
        object Loading : MainState()
        object Error : MainState()
        object Success : MainState()
        object RequireLogin : MainState()
        object HideNavigation : MainState()
        object ShowNavigation : MainState()
    }

    val state = MutableLiveData<MainState>()

    fun checkUser() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(javaClass.simpleName, "checkUser: checking user")
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                Log.e(javaClass.simpleName, "checkUser: require auth no user founded -> $user")
                state.postValue(MainState.RequireLogin)
            } else {
                Log.i(javaClass.simpleName, "checkUser: user founded -> ${user.uid}")
                state.postValue(MainState.Success)
            }
        }

    }

    private fun updateState(state: MainState) {
        this.state.postValue(state)
    }

    fun validateLogin(result: FirebaseAuthUIAuthenticationResult?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (result == null) {
                updateState(MainState.RequireLogin)
            } else {
                userService.saveUser()
                updateState(MainState.Success)
            }
        }
    }

    fun hideNavigation() {
        Log.i(javaClass.simpleName, "hideNavigation: updating state to hide navigation")
        updateState(MainState.HideNavigation)
    }

    fun showNavigation() {
        Log.i(javaClass.simpleName, "showNavigation: updating state to show navigation")
        updateState(MainState.ShowNavigation)
    }


}