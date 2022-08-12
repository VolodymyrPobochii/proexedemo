package com.pobochii.someapp.data

import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.mock.Calls

class UsersServiceTest {
    lateinit var openMocks: AutoCloseable

    @Mock
    lateinit var service: UsersService

    @Before
    fun setup() {
        openMocks = MockitoAnnotations.openMocks(this)
    }

    @After
    fun teardown() {
        openMocks.close()
    }

    @Test
    fun service_FetchAll_Await() {
        val fetchUsersResponse = UsersResponse()
        val response = Calls.response(fetchUsersResponse)

        whenever(service.fetchUsers()).doReturn(response)

        val fetchUsers = service.fetchUsers()
        val fetchResponse = runBlocking { fetchUsers.await() }

        assertEquals(fetchUsers, response)
        assertEquals(fetchResponse, fetchUsersResponse)
    }

    @Test
    fun service_FetchAll_AwaitResponse() {
        val fetchUsersResponse = Response.success(UsersResponse())
        val response = Calls.response(fetchUsersResponse)

        whenever(service.fetchUsers()).doReturn(response)

        val fetchUsers = service.fetchUsers()
        val fetchResponse = runBlocking { fetchUsers.awaitResponse() }

        assertEquals(fetchUsers, response)
        assertEquals(fetchResponse, fetchUsersResponse)
    }
}