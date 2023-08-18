package com.example.phrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class option_login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_login);

        Button loginButton = findViewById(R.id.LoginusingABHA);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(option_login.this, Abha_Login.class);
                startActivity(intent);
            }
        });

        Button dashboardButton = findViewById(R.id.continueToDashboard);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(option_login.this, dashboard.class);
                startActivity(intent);
            }
        });

    }
}
