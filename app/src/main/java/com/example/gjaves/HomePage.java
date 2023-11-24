package com.example.gjaves;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class HomePage extends AppCompatActivity {
Button z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



Intent intent = new Intent(getApplicationContext(),findmyWeather.class);
startActivity(intent);

    }
}