package com.example.ssgdesking.Retrofit;

import com.example.ssgdesking.Data.LoginData;
import com.example.ssgdesking.Data.ReservationLoginData;
import com.example.ssgdesking.Data.RetrofitSeatData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Retrofit_interface {


    @GET("reservation/seat/{post}")
    Call<String> getPostsString(@Path("post") String post);

    @GET("review/{seatId}")
    Call<String> getReview(@Path("seatId") String seatId);

    @POST("user/empseat")
    Call<String> getEmpInfo(@Body LoginData loginData);

    @POST("user/signin")
    Call<String> loginPost(@Body ReservationLoginData reservationLoginData);

    @POST("reservation/seat/{seatID}")
    Call<String> reservePost(@Path("seatID") String empno, @Body LoginData loginData);
}