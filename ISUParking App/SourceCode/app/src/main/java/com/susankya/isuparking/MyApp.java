package com.rluitel.isuparking;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApp extends Application {

    public Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();
         retrofit = new Retrofit.Builder()
                .baseUrl("https://teamlifa.org/parkingapp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
