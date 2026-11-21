package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.weatherapp.api.OpenWeatherMapService;
import com.example.weatherapp.model.Weather;
import com.example.weatherapp.model.WeatherResponse;
import com.squareup.moshi.Moshi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String API_KEY = "6a843fb4fd0ae43e51019f1d94c83a4d";
    private static final String CITY_NAME = "New York";

    private TextView tvCityName;
    private TextView tvMainWeather;
    private TextView tvDescriptionWeather;
    private ImageView ivWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        fetchWeatherData();
    }

    private void initViews() {
        tvCityName = findViewById(R.id.tvCityName);
        tvMainWeather = findViewById(R.id.tvMainWeather);
        tvDescriptionWeather = findViewById(R.id.tvDescriptionWeather);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);
    }

    private void fetchWeatherData() {
        Moshi moshi = new Moshi.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);

        Call<WeatherResponse> call = service.getWeather(CITY_NAME, API_KEY);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    displayWeather(weatherResponse);
                } else {
                    handleFailure("Error response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                handleFailure("Failed to fetch weather data: " + t.getMessage());
            }
        });
    }

    private void displayWeather(WeatherResponse weatherResponse) {
        if (weatherResponse != null) {
            tvCityName.setText(weatherResponse.getName());

            if (weatherResponse.getWeather() != null && !weatherResponse.getWeather().isEmpty()) {
                Weather weather = weatherResponse.getWeather().get(0);
                tvMainWeather.setText(weather.getMain());
                tvDescriptionWeather.setText(weather.getDescription());

                String iconCode = weather.getIcon();
                if (iconCode != null && !iconCode.isEmpty()) {
                    String iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";

                    Glide.with(MainActivity.this)
                            .load(iconUrl)
                            .into(ivWeatherIcon);
                }
            }
        }
    }

    private void handleFailure(String errorMessage) {
        Log.e(TAG, errorMessage);
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
