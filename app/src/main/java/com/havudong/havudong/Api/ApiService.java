package com.havudong.havudong.Api;

import com.havudong.havudong.Model.Product;
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

    // ================= User API =================
    @GET("users")
    Call<List<User>> getUserByUsername(@Query("username") String username);

    @GET("users")
    Call<List<User>> getUsersByUsernameAndPassword(
            @Query("username") String username,
            @Query("password") String password
    );

    @POST("users")
    Call<User> registerUser(@Body User user);

    @PUT("users/{id}")
    Call<User> updatePassword(@Path("id") String id, @Body User updatedUser);

    @GET("users")
    Call<List<User>> getUsers(
            @Query("username") String username,
            @Query("phone") String phone,
            @Query("email") String email
    );

    // ================= Product API =================
    @GET("products")
    Call<List<Product>> getProducts(); // Lấy tất cả sản phẩm

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") String id); // Lấy sản phẩm theo id

    @POST("products")
    Call<Product> createProduct(@Body Product product); // Thêm sản phẩm mới

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") String id, @Body Product product); // Cập nhật sản phẩm

    // Optional: filter theo category
    @GET("products")
    Call<List<Product>> getProductsByCategory(@Query("category") String category);
}
