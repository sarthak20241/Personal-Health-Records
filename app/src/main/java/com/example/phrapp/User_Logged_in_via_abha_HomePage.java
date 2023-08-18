package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phrapp.sampledata.Request_Model;
import com.example.phrapp.sampledata.Response_Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.stringtemplate.v4.ST;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User_Logged_in_via_abha_HomePage extends AppCompatActivity {
    private TextView textView;
    private String token_details;
    private String token;

    private String firstName;
    private byte[] profile_photo_bytes;
    private String profile_photo_base_64_string;
    private String profile_details;

    private ShapeableImageView get_profile_section;

    private Intent to_profile_section_intent;
    private Intent to_login_page;

    private Intent to_vitals_page;

    private ImageView vitals_button;
    private String refreshToken;

    protected boolean enabled = true;

    public void enable(boolean b) {
        enabled = b;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return enabled ?
                super.dispatchTouchEvent(ev) :
                true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logged_in_home_page);
        

        Intent intent = getIntent();
        token_details = intent.getStringExtra(EXTRA_MESSAGE);



        Bundle bundle2 = new Bundle();
        bundle2.putString("token_details" , token_details);
        fragment_User_logged_in_via_abha_Homescreen_fragment frag1 = new fragment_User_logged_in_via_abha_Homescreen_fragment();
        frag1.setArguments(bundle2);



        BottomNavigationView bottomNavigationView=findViewById(R.id.user_logged_in_via_abha_homepage_bottomNavigationView);
        // Set Home selected
        //bottomNavigationView.setSelectedItemId(R.id.home);
        replaceFragment(frag1);
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        replaceFragment(frag1);
                        break;
                    case R.id.camera:
                        replaceFragment(new UploadImageFragment());
                        break;
                    case R.id.prescription:
                        replaceFragment(new TimelineFragment());
                        break;

                }
                return  true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.user_logged_in_via_abha_homepage_fragment_frame,fragment);
        fragmentTransaction.commit();
    }
        
        



}