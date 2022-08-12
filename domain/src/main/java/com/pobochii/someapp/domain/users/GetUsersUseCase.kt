package com.pobochii.someapp.domain.users

/**
 * Use case for retrieving users
 * @property usersRepo - Users repository [UsersRepository]
 */
class GetUsersUseCase(private val usersRepo: UsersRepository) {
    /**
     * Invokes the use case
     * @return [Result.Success] with [User] list or [Result.Error]
     */
    suspend operator fun invoke() = usersRepo.findAll()
}