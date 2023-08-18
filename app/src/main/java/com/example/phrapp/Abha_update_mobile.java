package com.example.phrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Abha_update_mobile extends AppCompatActivity {

    private String x_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abha_update_mobile);

        x_token = getIntent().getStringExtra("X_token");

        Bundle bundle = new Bundle();
        bundle.putString("X_token" , x_token);
        Abha_update_mobile_verify_new_mobile_fragment frag = new Abha_update_mobile_verify_new_mobile_fragment();
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                .replace(R.id.abha_update_mobile_fragment, frag ).commit();

    }

}