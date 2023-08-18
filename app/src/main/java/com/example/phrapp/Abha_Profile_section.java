package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phrapp.sampledata.Request_Model_Login;
import com.example.phrapp.sampledata.Response_Model_Login;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Abha_Profile_section extends AppCompatActivity {

    private String profile_details;
    private String x_token;

    private ImageView profile_photo;
    private byte[] profile_photo_bytes;
    private String profile_photo_base_64_string;
    private String user_name_str;
    private TextView abha_profile_section_user_name;
    private TextView abha_id_see_tv;
    private TextView user_details_see_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abha_profile_section);



//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.abha_forgot_abha_fragment_frame, new AbhaRegisterVerifyAadhaarOtpFragment() ).commit();


        Intent intent = getIntent();
        profile_details = intent.getStringExtra(EXTRA_MESSAGE);
        x_token = intent.getStringExtra("X_token");

        Bundle bundle1 = new Bundle();
        bundle1.putString("profile_details" , profile_details);
        bundle1.putString("X_token" , x_token);
        Abha_profile_section_user_info_fragment frag = new Abha_profile_section_user_info_fragment();
        frag.setArguments(bundle1);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.abha_profile_section_fragment_frame, frag).commit();


        profile_photo = findViewById(R.id.abha_profile_section_user_profile_photo_iv);
        abha_profile_section_user_name = findViewById(R.id.abha_profile_section_user_name_tv);
        abha_id_see_tv = findViewById(R.id.abha_profile_section_see_abha_card_info_btn_tv);
        user_details_see_tv = findViewById(R.id.abha_profile_section_user_info_btn_tv);


        try{
            JSONObject obj = new JSONObject(profile_details);
            user_name_str = obj.getString("name");
            abha_profile_section_user_name.setText(user_name_str);
            profile_photo_base_64_string = obj.getString("profilePhoto");
            profile_photo_bytes = Base64.decode(profile_photo_base_64_string.getBytes() , Base64.DEFAULT);
            profile_photo.setImageBitmap(BitmapFactory.decodeByteArray(profile_photo_bytes, 0, profile_photo_bytes.length));

//            Base64.(profile_photo_base_64_string.getBytes())
        }
        catch (JSONException e) {
            Toast.makeText(this, "Some Error Occurred while fetching your profile !", Toast.LENGTH_SHORT).show();
        }

        abha_id_see_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abha_id_see_tv.setBackground(getDrawable(R.drawable.circular_border_left_top_bottom));
                user_details_see_tv.setBackground(null);

                Bundle bundle2 = new Bundle();
//                bundle2.putString("profile_details" , profile_details);
                bundle2.putString("X_token" , x_token);
                Abha_profile_section_get_abha_id_card_fragment frag1 = new Abha_profile_section_get_abha_id_card_fragment();
                frag1.setArguments(bundle2);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.abha_profile_section_fragment_frame, frag1).commit();

            }
        });

        user_details_see_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_details_see_tv.setBackground(getDrawable(R.drawable.circular_border_left_top_bottom));
                abha_id_see_tv.setBackground(null);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.abha_profile_section_fragment_frame, frag).commit();

            }
        });




    }
}