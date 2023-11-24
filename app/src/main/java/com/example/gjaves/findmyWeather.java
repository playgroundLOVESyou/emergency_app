package com.example.gjaves;

import static com.example.gjaves.R.drawable.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.gjaves.databinding.ActivityFindmyWeatherBinding;
import com.example.gjaves.databinding.ActivityLocationViewerBinding;
import com.example.gjaves.weather.main;
import com.example.gjaves.weather.sys;
import com.example.gjaves.weather.weather;
import com.example.gjaves.weather.weatherdata;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class findmyWeather extends AppCompatActivity {

    ActivityFindmyWeatherBinding binding;
    ActivityLocationViewerBinding binding2;
    TextView feeling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findmy_weather);
        feeling = findViewById(R.id.condition);
        binding = ActivityFindmyWeatherBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());






        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyy");
        String currentdate= simpleDateFormat.format(new Date());

        binding.date.setText(currentdate);

        fetchWeather("Philippines");

        if (feeling.equals("Clear Sky") || feeling.equals("clear sky") || feeling.equals("Sunny") || feeling.equals("sunny") || feeling.equals("Clear") || feeling.equals("clear")) {
            binding.backgroundimage.setBackgroundResource(sunny_background);
            binding.animationweathericon.setAnimation(R.raw.sun);

            // Handle other weather conditions here
        } else if (feeling.equals("Partly Clouds") || feeling.equals("partly clouds") || feeling.equals("Clouds") || feeling.equals("clouds") || feeling.equals("Overcast Clouds") || feeling.equals("overcast clouds") || feeling.equals("Mist") || feeling.equals("mist") || feeling.equals("Foggy") || feeling.equals("foggy") || feeling.equals("Mostly Clouds") || feeling.equals("mostly clouds") || feeling.equals("Scattered Clouds") || feeling.equals("scattered clouds") || feeling.equals("Broken Clouds") || feeling.equals("broken clouds")) {
            binding.backgroundimage.setBackgroundResource(R.drawable.colud_background);
            binding.animationweathericon.setAnimation(R.raw.cloud);
        } else if (feeling.equals("Light Rain") || feeling.equals("Drizzles") || feeling.equals("Moderate Rain") || feeling.equals("Showers") || feeling.equals("Heavy Rain")) {
            binding.backgroundimage.setBackgroundResource(rain_background);
            binding.animationweathericon.setAnimation(R.raw.rain);
        } else if (feeling.equals("Light Snow") || feeling.equals("Moderate Snow") || feeling.equals("Heavy Snow") || feeling.equals("Blizzard")) {
            binding.backgroundimage.setBackgroundResource(snow_background);
            binding.animationweathericon.setAnimation(R.raw.snow);
        }
        binding.animationweathericon.playAnimation();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.searchvieweditText.getText().toString())){
                    binding.searchvieweditText.setError("please enter City");
                    return;

                }

                String City_Name = binding.searchvieweditText.getText().toString();
                fetchWeather(City_Name);
            }
        });
    }



    void fetchWeather(String cityname){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
        Call<weatherdata> call =  interfaceAPI.getData(cityname,"efe9ab8fe58f9117ba01ce1e2e88a9d6", "metric");

        call.enqueue(new Callback<weatherdata>() {
            @Override
            public void onResponse(Call<weatherdata> call, Response<weatherdata> response) {

                if(response.isSuccessful()){
                    weatherdata dataweather = response.body();


                    List<weather> des = dataweather.getWeather();
                    main to = dataweather.getMain();
                    sys me = dataweather.getSys();





                    binding.temp.setText(String.valueOf(to.getTemp()));
                    binding.maxTemp.setText(String.valueOf(to.getTemp_max()));
                    binding.minTemp.setText(String.valueOf(to.getTemp()));
                    binding.wind.setText(String.valueOf(to.getPressure()));
                    binding.seaLevel.setText(String.valueOf(to.getSea_level()));
                    binding.humidity.setText(String.valueOf(to.getHumidity()));
                    binding.cityName.setText(dataweather.getName());
                    binding.sunsrise.setText(String.valueOf(me.getSunrise()));
                    binding.sunset.setText(String.valueOf(me.getSunset()));




                    for(weather data : des){


                        binding.weather.setText(data.getDescription());
                        binding.condition.setText(data.getDescription());





                    }




                }

            }

            @Override
            public void onFailure(Call<weatherdata> call, Throwable t) {

            }
        });




        }



    }
