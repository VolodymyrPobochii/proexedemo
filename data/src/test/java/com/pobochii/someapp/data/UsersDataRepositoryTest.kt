package com.pobochii.someapp.data

import com.pobochii.someapp.domain.users.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Response
import retrofit2.mock.Calls

class UsersDataRepositoryTest {

    @Test
    fun findAll_Success_EmptyList() {
        val success = Response.success(UsersResponse())
        val fetchUsersResponse = Calls.response(success)
        val service = mock<UsersService> {
            on { fetchUsers() } doReturn fetchUsersResponse
        }

        val repository = UsersDataRepository(service)

        val findAllResponse = runBlocking { repository.findAll() }

        verify(service, times(1)).fetchUsers()
        assert(
            (findAllResponse is Result.Success)
                .and((findAllResponse as Result.Success).data.isEmpty())
        )
    }

    @Test
    fun findAll_Success() {
        val userData = UserData("Peter", "https://image")
        val success = Response.success(UsersResponse().apply {
            add(userData)
        })
        val fetchUsersResponse = Calls.response(success)
        val service = mock<UsersService> {
            on { fetchUsers() } doReturn fetchUsersResponse
        }

        val repository = UsersDataRepository(service)

        val findAllResponse = runBlocking { repository.findAll() }

        verify(service, times(1)).fetchUsers()
        assert(
            (findAllResponse is Result.Success)
                .and((findAllResponse as Result.Success).data.isNotEmpty())
                .and(findAllResponse.data[0].name == userData.name)
                .and(findAllResponse.data[0].profileImage == userData.image)
        )
    }

    @Test
    fun findById_Error_NoUser() {
        val userId = 0
        val service = mock<UsersService> {
            on { fetchUsers() } doReturn Calls.response(UsersResponse())
        }
        val repository = UsersDataRepository(service)

        val findUserResponse = runBlocking { repository.findById(userId) }

        assert(findUserResponse is Result.Error)
        val result = findUserResponse as Result.Error
        assertEquals(result.message, "not found")
    }
}