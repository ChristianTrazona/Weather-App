package com.example.weatherapp.model;

import com.squareup.moshi.Json;
import java.util.List;

public class WeatherResponse {
    @Json(name = "name")
    private String name;

    @Json(name = "weather")
    private List<Weather> weather;

    public WeatherResponse() {
    }

    public WeatherResponse(String name, List<Weather> weather) {
        this.name = name;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
}
