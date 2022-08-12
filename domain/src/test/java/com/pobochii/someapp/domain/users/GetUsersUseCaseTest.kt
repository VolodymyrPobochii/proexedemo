package com.pobochii.someapp.domain.users

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for [GetUsersUseCase] unit tests
 */
class GetUsersUseCaseTest {

    @Test
    fun getUsers_Success() = runBlocking {
        val usersRepo = mock<UsersRepository>()
        val testUsers = listOf(User(id = 111))
        whenever(usersRepo.findAll()).doReturn(Result.Success(testUsers))

        val getUsers = GetUsersUseCase(usersRepo)
        val users = getUsers()

        assert(users is Result.Success)
        val data = (users as Result.Success).data
        assert(data.isNotEmpty())
        assert(data[0].id == testUsers[0].id)
    }

    @Test
    fun getUsers_Error() = runBlocking {
        val usersRepo = mock<UsersRepository>()
        whenever(usersRepo.findAll()).doReturn(Result.Error("unknown"))

        val getUsers = GetUsersUseCase(usersRepo)
        val users = getUsers()

        assert(users is Result.Error)
        assert((users as Result.Error).message == "unknown")
    }
}