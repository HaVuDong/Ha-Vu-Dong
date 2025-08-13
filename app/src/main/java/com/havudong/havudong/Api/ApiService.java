package com.havudong.havudong.Api;

import com.havudong.havudong.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Lấy user theo username
    @GET("users")
    Call<List<User>> getUserByUsername(@Query("username") String username);

    // Lấy user theo username và password
    @GET("users")
    Call<List<User>> getUsersByUsernameAndPassword(
            @Query("username") String username,
            @Query("password") String password
    );

    // Đăng ký user mới
    @POST("users")
    Call<User> registerUser(@Body User user);

    // Cập nhật user
    @PUT("users/{id}")
    Call<User> updatePassword(@Path("id") String id, @Body User updatedUser);
    @GET("users")
    Call<List<User>> getUsers(
            @Query("username") String username,
            @Query("phone") String phone,
            @Query("email") String email
    );



}
