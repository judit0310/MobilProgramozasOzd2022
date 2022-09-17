package com.example.myapplication.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PersonService {
    @GET("dolgozok")
    Call<List<PersonDTO>> szemelyekListazasa();

    @GET("dolgozok/{id}")
    Call<PersonDTO> getSzemelyById(@Path("id") int id);

    @POST("dolgozok")
    Call<PersonDTO> createSzemely(@Body PersonDTO person);

}
