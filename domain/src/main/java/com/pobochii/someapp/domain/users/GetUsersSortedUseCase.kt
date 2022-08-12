package com.pobochii.someapp.domain.users

/**
 * Use case for retrieving users in sorted order
 * @property getUsers - Get users use case [GetUsersUseCase]
 */
class GetUsersSortedUseCase(private val getUsers: GetUsersUseCase) {
    /**
     * Invokes the use case
     * @return [Result.Success] with [User] list or [Result.Error]
     */
    suspend operator fun invoke(): Result<List<User>> {
        return when (val result = getUsers()) {
            is Result.Error -> result
            is Result.Success -> Result.Success(result.data.sortedBy { it.name })
        }
    }
}