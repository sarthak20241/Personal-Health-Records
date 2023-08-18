package com.example.phrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Abha_Delete_abha extends AppCompatActivity {

    private TextView delete_abha_using_aadhaar_see_tv;
    private TextView delete_abha_using_mobile_see_tv;
    private String profile_details;
    private String x_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abha_delete_abha);

        Intent intent = getIntent();
        profile_details = intent.getStringExtra("profile_details");
        x_token = intent.getStringExtra("X_token");

        Bundle bundle1 = new Bundle();
        bundle1.putString("profile_details" , profile_details);
        bundle1.putString("X_token" , x_token);
        delete_abha_id_via_aadhaar_fragment frag = new delete_abha_id_via_aadhaar_fragment();
        frag.setArguments(bundle1);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                .replace(R.id.abha_delete_abha_fragment_frame, frag ).commit();

        delete_abha_using_aadhaar_see_tv = findViewById(R.id.abha_delete_abha_using_aadhaar_btn_tv);
        delete_abha_using_mobile_see_tv = findViewById(R.id.abha_delete_abha_using_mobile_btn_tv);


        delete_abha_using_aadhaar_see_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_abha_using_aadhaar_see_tv.setBackground(getDrawable(R.drawable.circular_border_left_top_bottom));
                delete_abha_using_mobile_see_tv.setBackground(null);

//                Bundle bundle1 = new Bundle();
//                bundle1.putString("profile_details" , profile_details);
//                bundle1.putString("X_token" , x_token);
//                delete_abha_id_via_aadhaar_fragment frag = new delete_abha_id_via_aadhaar_fragment();
//                frag.setArguments(bundle1);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                        .replace(R.id.abha_delete_abha_fragment_frame, frag ).commit();

            }
        });

        delete_abha_using_mobile_see_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_abha_using_mobile_see_tv.setBackground(getDrawable(R.drawable.circular_border_left_top_bottom));
                delete_abha_using_aadhaar_see_tv.setBackground(null);

                Bundle bundle2 = new Bundle();
                bundle2.putString("profile_details" , profile_details);
                bundle2.putString("X_token" , x_token);
                delete_abha_id_via_mobile_fragment frag2 = new delete_abha_id_via_mobile_fragment();
                frag2.setArguments(bundle2);
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                        .replace(R.id.abha_delete_abha_fragment_frame, frag2 ).commit();

//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
//                        .replace(R.id.abha_delete_abha_fragment_frame, new delete_abha_id_via_mobile_fragment()).commit();

            }
        });
    }
}