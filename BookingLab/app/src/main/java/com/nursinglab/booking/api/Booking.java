package com.nursinglab.booking.api;

import com.nursinglab.booking.component.BookingIdComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.ResultComponent;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Booking {

    @FormUrlEncoded
    @POST("booking_api/select")
    Call<ResponseComponent> select(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("booking_api/select_all")
    Call<ResponseComponent> select_all(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("booking_api/insert")
    Call<ResponseComponent> insert(
            @Field("id") String id,
            @Field("waktu_mulai") String waktu_mulai,
            @Field("waktu_selesai") String waktu_selesai,
            @Field("tanggal") String tanggal,
            @Field("id_dosen") String id_dosen,
            @Field("id_lab") String id_lab,
            @Field("id_praktikum") String id_praktikum
    );

    @GET("booking_api/delete/{id}")
    Call<ResponseComponent> delete(
            @Path("id") String id
    );

    @GET("booking_api/booking/{booking}")
    Call<ResponseComponent> booking(
            @Path("booking") String booking
    );
}
