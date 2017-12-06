package com.rluitel.isuparking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ram  on 11/21/2017.
 */

public interface CallMethods {
    @GET("Router/?method_id=16&passkey=2055-05-10")
    Call<List<Category>> getCategories();

    @FormUrlEncoded
    @POST("Router/?method_id=15&passkey=2055-05-10")
    Call<Boolean> updateAvail(@Field("spot_sn")int spotSn,@Field("set")int available);

    @FormUrlEncoded
    @POST("Router/?method_id=14&passkey=2055-05-10")
    Call<List<ParkingLot>> getNearestLots(@Field("latitude")String lat,@Field("longitude") String longi,@Field("category_sn")int cat_sn);
}
