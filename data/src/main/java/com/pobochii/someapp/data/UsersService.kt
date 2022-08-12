package com.pobochii.someapp.data

import retrofit2.Call
import retrofit2.http.GET

interface UsersService {

    @GET("json.json")
    fun fetchUsers(): Call<UsersResponse>
}

class UsersResponse : ArrayList<UserData>()