package com.pobochii.someapp.data

import android.util.LruCache
import com.pobochii.someapp.domain.users.Result
import com.pobochii.someapp.domain.users.User
import com.pobochii.someapp.domain.users.UsersRepository
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [UsersRepository]
 * @property dataSource Users remote data source [UsersService]
 */
@Singleton
class UsersDataRepository @Inject constructor(private val dataSource: UsersService) : UsersRepository {

    private val cache: LruCache<Int, User?> = LruCache(100)

    override suspend fun findAll(): Result<List<User>> {
        val fetchUsers = dataSource.fetchUsers()
        val awaitResponse = fetchUsers.awaitResponse()
        val response = awaitResponse.body() ?: return Result.Error("unknown")
        val mapped = response.asSequence()
            .mapIndexed{ index, item ->
                item.asUser(index).also {
                    cache.put(it.id, it)
                }
            }
            .toList()
        return Result.Success(mapped)
    }

    override suspend fun findById(id: Int): Result<User> {
        if (cache.size() == 0){
            findAll()
        }
        cache[id]?.let {
            return Result.Success(it)
        }
        return Result.Error("not found")
    }
}