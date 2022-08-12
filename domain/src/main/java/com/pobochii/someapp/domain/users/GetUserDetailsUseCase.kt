package com.pobochii.someapp.domain.users

/**
 * Use case for retrieving user's details
 * @property usersRepo - Users repository [UsersRepository]
 */
class GetUserDetailsUseCase(private val usersRepo: UsersRepository) {
    /**
     * Invokes the use case
     * @param userId ID of the user to find
     * @return [Result.Success] with [User] or [Result.Error]
     */
    suspend operator fun invoke(userId: Int) = usersRepo.findById(userId)
}