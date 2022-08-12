package com.pobochii.someapp.domain.users

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test class for [GetUserDetailsUseCase] unit tests
 */
class GetUserDetailsUseCaseTest {

    @Test
    fun getUserDetails_Success() = runBlocking {
        val usersRepo = mock<UsersRepository>()
        val testID = 111
        whenever(usersRepo.findById(testID)).doReturn(Result.Success(User(id = testID)))

        val getUserDetails = GetUserDetailsUseCase(usersRepo)
        val userDetails = getUserDetails(testID)

        assert(userDetails is Result.Success)
        val data = (userDetails as Result.Success).data
        assert(data.id == testID)
    }

    @Test
    fun getUserDetails_Error() = runBlocking {
        val usersRepo = mock<UsersRepository>()
        val testID = 1
        whenever(usersRepo.findById(testID)).doReturn(Result.Error("not found"))

        val getUserDetails = GetUserDetailsUseCase(usersRepo)
        val userDetails = getUserDetails(testID)

        assert(userDetails is Result.Error)
        assert((userDetails as Result.Error).message == "not found")
    }
}