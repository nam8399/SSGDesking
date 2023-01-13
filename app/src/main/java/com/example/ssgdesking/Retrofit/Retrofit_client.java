package com.example.ssgdesking.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieManager;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Retrofit_client {
    public static String BASE_URL = "http://192.168.189.32:80/api/v1/";


    public static Retrofit_interface getApiService(){return getStringApiClient().create(Retrofit_interface.class);}

    private static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Retrofit getStringApiClient()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(new CookieManager()))
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
//                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

}