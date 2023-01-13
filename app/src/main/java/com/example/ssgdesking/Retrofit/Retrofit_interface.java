package com.example.ssgdesking.Retrofit;

import com.example.ssgdesking.Data.ReservationLoginData;
import com.example.ssgdesking.Data.RetrofitSeatData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Retrofit_interface {

    @GET("seat/{post}")
    Call<RetrofitSeatData> getPosts(@Path("post") String post);

    @GET("reservation/seat/{post}")
    Call<String> getPostsString(@Path("post") String post);

    @POST("user/signin")
    Call<ReservationLoginData> loginPost(@Body ReservationLoginData reservationLoginData);

    @POST("reservation/seat/{seatID}")
    Call<String> reservePost(@Path("seatID") String empno);
}