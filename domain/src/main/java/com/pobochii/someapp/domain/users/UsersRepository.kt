package com.pobochii.someapp.domain.users

/**
 * Users repository interface
 */
interface UsersRepository {
    /**
     * Finds and returns all the users
     * @return [Result.Success] with [User] list or [Result.Error]
     */
    suspend fun findAll(): Result<List<User>>

    /**
     * Finds user by ID
     * @return [Result.Success] with [User] or [Result.Error]
     */
    suspend fun findById(id: Int): Result<User>
}