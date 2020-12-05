package com.example.testapp.network;

import com.example.testapp.model.UserResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

interface NetworkService {
    @GET("/public-api/users")
    Observable<UserResponse> callTestApi();

}
