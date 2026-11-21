package com.example.weatherapp.api;

import com.example.weatherapp.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getWeather(
            @Query("q") String cityName,
            @Query("appid") String apiKey
    );
}
