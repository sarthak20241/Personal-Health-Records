package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mSLideViewPager;
    LinearLayout mDotLayout;
    Button backbtn, nextbtn, skipbtn;

    TextView[] dots;
    com.example.phrapp.ViewPagerAdapter viewPagerAdapter;
    public static  List<Document> documentList;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        documentList = new ArrayList<Document>();
        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        skipbtn = findViewById(R.id.skipButton);
        mAuth = FirebaseAuth.getInstance();
        Log.d("api" , "hiii");
        SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
        Boolean is_Abha_Logged_in = pref.getBoolean("isAbhaLoggedIn" , false);

        Log.d("api" , "helo 22"+pref.getString("token_details" , "NOT found"));
        Log.d("api" , "helo 22"+pref.getBoolean("isAbhaLoggedIn" , false));

        if(is_Abha_Logged_in){
            Intent to_abha_logged_in = new Intent(this , User_Logged_in_via_abha_HomePage.class);
            to_abha_logged_in.putExtra(EXTRA_MESSAGE , pref.getString("token_details" , ""));
            Log.d("api" , "helo 11"+pref.getString("token_details" , "NOT found"));
            Log.d("api" , "helo 11"+pref.getBoolean("isAbhaLoggedIn" , false));
            startActivity(to_abha_logged_in);

            finish();
            return;

        }


        if(mAuth.getCurrentUser()!=null){
            Log.d("api" , "hiii");

            Intent intent=new Intent(MainActivity.this,dashboard.class);
            startActivity(intent);
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) > 0){

                    mSLideViewPager.setCurrentItem(getitem(-1),true);

                }

            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) < 3)
                    mSLideViewPager.setCurrentItem(getitem(1),true);
                else {
                    Intent i = new Intent(MainActivity.this,login2.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, com.example.phrapp.mainscreen.class);
                startActivity(i);
                finish();
            }
        });

        mSLideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);
        viewPagerAdapter = new com.example.phrapp.ViewPagerAdapter(this);
        mSLideViewPager.setAdapter(viewPagerAdapter);
        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setUpindicator(int position){

        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(37);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                backbtn.setVisibility(View.VISIBLE);

            }else {

                backbtn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return mSLideViewPager.getCurrentItem() + i;
    }

}