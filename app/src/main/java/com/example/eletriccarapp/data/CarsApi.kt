package com.example.eletriccarapp.data

import com.example.eletriccarapp.domain.Carro
import retrofit2.Call
import retrofit2.http.GET

interface CarsApi {

//https://igorbag.github.io/cars-api/cars.json
    @GET("cars.json")
    fun getAllCars() : Call<List<Carro>>

}