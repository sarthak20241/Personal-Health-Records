package com.example.phrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

public class Vitals_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.vitals_frame_layout, new VitalsFragment() ).commit();

    }
}