package com.pobochii.someapp.domain.users

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for [GetUsersSortedUseCase] unit tests
 */
class GetUsersSortedUseCaseTest {

    @Test
    fun getUsersSorted_Success() = runBlocking {
        val usersRepo = mock<UsersRepository>()
        val testUsers = listOf(User(name = "Bill"), User(name = "Adam"), User(name = "Aron"))
        whenever(usersRepo.findAll()).doReturn(Result.Success(testUsers))

        val getUsersSorted = GetUsersSortedUseCase(GetUsersUseCase(usersRepo))
        val users = getUsersSorted()

        assert(users is Result.Success)
        val data = (users as Result.Success).data
        assert(data.isNotEmpty())
        assert(data[0].name != testUsers[0].name)
        assert(data[0].name == "Adam")
    }

    @Test
    fun getUsersSorted_Error() = runBlocking {
        val usersRepo = mock<UsersRepository>()
        whenever(usersRepo.findAll()).doReturn(Result.Error("unknown"))

        val getUsersSorted = GetUsersSortedUseCase(GetUsersUseCase(usersRepo))
        val users = getUsersSorted()

        assert(users is Result.Error)
        assert((users as Result.Error).message == "unknown")
    }
}