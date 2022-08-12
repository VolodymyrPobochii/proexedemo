package com.pobochii.someapp.users

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pobochii.someapp.domain.users.GetUsersUseCase
import com.pobochii.someapp.domain.users.Result
import com.pobochii.someapp.utils.isInternetAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    application: Application,
    private val getUsers: GetUsersUseCase,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    companion object {
        const val NO_ID = -1
        const val KEY_USER_ID = "selected_user_id"
        const val NO_INTERNET_CONNECTION = "No internet connection"
    }

    private val _event: MutableStateFlow<Event?> = MutableStateFlow(null)
    val event: StateFlow<Event?> = _event

    @OptIn(ExperimentalCoroutinesApi::class)
    val users: StateFlow<com.pobochii.someapp.Result<List<UserListItem>>> =
        savedStateHandle.getStateFlow(KEY_USER_ID, NO_ID)
            .flatMapLatest { loadUsers(it) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, com.pobochii.someapp.Result.Busy)

    private fun loadUsers(selectedId: Int) = flow {
        if (!getApplication<Application>().applicationContext.isInternetAvailable()) {
            emit(com.pobochii.someapp.Result.Error(NO_INTERNET_CONNECTION))
            return@flow
        }
        emit(
            when (val result = getUsers()) {
                is Result.Error -> com.pobochii.someapp.Result.Error(result.message)
                is Result.Success -> com.pobochii.someapp.Result.Success(
                    result.data.asSequence()
                        .map { user ->
                            UserListItem(
                                user.id,
                                user.name,
                                user.profileImage,
                                user.id == selectedId
                            )
                        }.toList()
                )
            }
        )
    }

    fun selectUser(id: Int) {
        savedStateHandle[KEY_USER_ID] = id
        _event.value = Event.UserSelected(id)
    }

    fun selectFirstUserIfNeeded() {
        if (savedStateHandle.get<Int>(KEY_USER_ID) != NO_ID) {
            return
        }
        val value = users.value
        if (value is com.pobochii.someapp.Result.Success && value.data.isNotEmpty()) {
            val id = value.data.first().id
            selectUser(id)
        }
    }

    fun resetEvent() {
        savedStateHandle[KEY_USER_ID] = NO_ID
        _event.value = null
    }
}

sealed class Event {
    data class UserSelected(val id: Int) : Event()
}