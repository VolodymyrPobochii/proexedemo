package com.pobochii.someapp.userdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pobochii.someapp.domain.users.GetUserDetailsUseCase
import com.pobochii.someapp.domain.users.Result
import com.pobochii.someapp.domain.users.User
import com.pobochii.someapp.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    application: Application,
    private val getUser: GetUserDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    companion object {
        const val NO_ID = -1
        const val KEY_USER_ID = "selected_user_id"
        const val NO_INTERNET_CONNECTION = "No internet connection"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val user: StateFlow<com.pobochii.someapp.Result<UserDetailsItem>> =
        savedStateHandle.getStateFlow(KEY_USER_ID, NO_ID)
            .flatMapLatest { id ->
                if (id != NO_ID) {
                    loadUserDetails(id)
                } else {
                    emptyFlow()
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, com.pobochii.someapp.Result.Busy)

    private fun loadUserDetails(userId: Int) = flow {
        if (!getApplication<Application>().isInternetAvailable()) {
            emit(com.pobochii.someapp.Result.Error(NO_INTERNET_CONNECTION))
            return@flow
        }
        emit(
            when (val result = getUser(userId)) {
                is Result.Error -> com.pobochii.someapp.Result.Error(result.message)
                is Result.Success -> com.pobochii.someapp.Result.Success(result.data.asUserDetailsItem())
            }
        )
    }

    fun selectUser(userId: Int) {
        savedStateHandle[KEY_USER_ID] = userId
    }
}

data class UserDetailsItem(val userName: String = "", val profileImage: String = "")

fun User.asUserDetailsItem() = UserDetailsItem(name, profileImage)