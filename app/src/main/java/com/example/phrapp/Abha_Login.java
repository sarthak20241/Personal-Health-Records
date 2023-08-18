package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

import com.example.phrapp.sampledata.Request_Model_Get_Otp;
import com.example.phrapp.sampledata.Request_Model_Login;
import com.example.phrapp.sampledata.Response_Model_Get_Otp;
import com.example.phrapp.sampledata.Response_Model_Login;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Abha_Login extends AppCompatActivity {

    private Button login_btn;
    private Button get_otp_btn;
    private EditText healthid_et;
    private String txnid;
    private EditText otp_et;
    private Intent to_logged_in;
    private Intent to_register_for_abha;
    private Intent to_forgot_abha;
    private CountDownTimer cTimer;
    private TextView register_here;
    private TextView forgot_abha;
    private RadioButton send_otp_to_aadhaar_linked_rb;
    private RadioButton send_otp_to_abha_linked_mobile_rb;
    private String authMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abha_login);

        // FOR API CALLS
        String BASE_URL = ApiService.url;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);


        login_btn = findViewById(R.id.abha_login_btn);
        get_otp_btn = findViewById(R.id.abha_login_get_otp_btn);
        healthid_et = findViewById(R.id.abha_login_healthid);
        otp_et = findViewById(R.id.abha_login_otp);
        forgot_abha = findViewById(R.id.abha_login_abha_forgot_abha_tv);
        register_here = findViewById(R.id.abha_login_abha_register_here_tv);

        send_otp_to_aadhaar_linked_rb = findViewById(R.id.abha_login_abha_send_aadhaar_otp_radioButton);
        send_otp_to_abha_linked_mobile_rb = findViewById(R.id.abha_login_abha_send_mobile_otp_radioButton);

        authMethod = "AADHAAR_OTP";


        to_logged_in = new Intent(this , User_Logged_in_via_abha_HomePage.class);
        to_register_for_abha = new Intent(this , Abha_Register.class);
        to_forgot_abha = new Intent(this , Abha_Forgot_Abha.class);



        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_otp();
            }

            private void get_otp() {

                Request_Model_Get_Otp requestModel = new Request_Model_Get_Otp(healthid_et.getText().toString());
                Call<Response_Model_Get_Otp> call;

                if(authMethod == "AADHAAR_OTP"){
                    call = apiService.abha_Login_with_aadhaar_gen_otp_createPost(requestModel);
                }
                else{
                    call = apiService.abha_Login_with_mobile_gen_otp_createPost(requestModel);
                }

                call.enqueue(new Callback<Response_Model_Get_Otp>() {
                    @Override
                    public void onResponse(Call<Response_Model_Get_Otp> call, Response<Response_Model_Get_Otp> response) {
                        if (response.isSuccessful()) {
                            // handle success response
                            Response_Model_Get_Otp m = response.body();
                            txnid = m.getTxnId();
                            Log.d("api" , m.getTxnId());
//                            Log.d("api" ,response.);

//                            tv = (TextView) findViewById(R.id.timer);
                            get_otp_btn.setEnabled(false);
                            login_btn.setEnabled(true);
                            login_btn.getBackground().setTintList(
                                    ContextCompat.getColorStateList(Abha_Login.this, R.color.btn_theme));

                            get_otp_btn.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.baseline_refresh_24) , null,null,null);
//                            get_otp_btn.setBackgroundTintList(get_otp_btn.getResources().getColorStateList(R.color.your_xml_name));
                            get_otp_btn.getBackground().setTintList(
                                    ContextCompat.getColorStateList(Abha_Login.this, R.color.btn_inactive_theme));
                            cTimer = new CountDownTimer(30000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    get_otp_btn.setText(String.valueOf(millisUntilFinished/1000));
                                }
                                public void onFinish() {
//                                    tv.setText("Re send OTP!");
                                    get_otp_btn.setEnabled(true);
                                    get_otp_btn.getBackground().setTintList(
                                            ContextCompat.getColorStateList(Abha_Login.this, R.color.btn_theme));
                                    get_otp_btn.setText("GET OTP");

//                                    get_otp_btn.setback(getColor("#C0E862"));
                                    get_otp_btn.setCompoundDrawablesWithIntrinsicBounds(null, null,null,null);
                                }
                            };
                            cTimer.start();

                            Toast.makeText(Abha_Login.this, "OTP sent successfully !", Toast.LENGTH_SHORT).show();

                        } else {
                            // handle error response
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                AlertDialog alertDialog = new AlertDialog.Builder(Abha_Login.this).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setIcon(R.drawable.baseline_error_24);
                                alertDialog.setMessage(jObjError.getString("message"));
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.show();

                            } catch (Exception e) {
                                AlertDialog alertDialog2 = new AlertDialog.Builder(Abha_Login.this).create();
                                alertDialog2.setTitle("Error");
                                alertDialog2.setIcon(R.drawable.baseline_error_24);
                                alertDialog2.setMessage("An Unknown Error has Occurred!");
                                alertDialog2.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog2.dismiss();
                                    }
                                });
                                alertDialog2.show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Response_Model_Get_Otp> call, Throwable t) {
                        // handle failure
                        Log.e("api" , t.toString());
                        Toast.makeText(Abha_Login.this, "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Request_Model_Login requestModel = new Request_Model_Login(otp_et.getText().toString() , txnid);
                    Call<Response_Model_Login> call;
                    if(authMethod == "AADHAAR_OTP"){
                        call = apiService.abha_Login_with_aadhaar_verify_otp_createPost(requestModel);
                    }
                    else{
                        call = apiService.abha_Login_with_mobile_verify_otp_createPost(requestModel);
                    }
                    call.enqueue(new Callback<Response_Model_Login>() {
                        @Override
                        public void onResponse(Call<Response_Model_Login> call, Response<Response_Model_Login> response) {
                            if (response.isSuccessful()) {
                                // handle success response
                                Response_Model_Login m = response.body();

                                try {

                                    JSONObject obj = new JSONObject(m.getData());
//                                    Log.d("api", m.getData());
                                    Log.d("api", obj.toString());
                                    Log.d("api", obj.getString("token"));

                                    if(obj != null){
                                        String token_details = obj.toString();
                                        to_logged_in.putExtra(EXTRA_MESSAGE , token_details);

                                        Toast.makeText(Abha_Login.this, "Logging In ....", Toast.LENGTH_SHORT).show();

                                        // saving login login info for next time login.....
                                        SharedPreferences pref = getSharedPreferences("login" , MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putBoolean("isAbhaLoggedIn" , true);
                                        editor.putString("token_details" , token_details);
                                        editor.apply();

                                        Log.d("api" , "helo "+pref.getString("token_details" , "NOT found"));
                                        Log.d("api" , "helo "+pref.getBoolean("isAbhaLoggedIn" , false));

                                        try {
                                            Thread.sleep(1500); //1000 milliseconds is one second.
                                        }
                                        catch (InterruptedException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        cTimer.cancel();
                                        to_logged_in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(to_logged_in);
                                        overridePendingTransition(R.anim.slide_in_right  , R.anim.slide_out_left);

                                    }
                                    else{
                                        AlertDialog alertDialog1 = new AlertDialog.Builder(Abha_Login.this).create();
                                        alertDialog1.setTitle("Error");
                                        alertDialog1.setIcon(R.drawable.baseline_error_24);
                                        alertDialog1.setMessage("An Unknown Server Error has Occurred!");
                                        alertDialog1.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                alertDialog1.dismiss();
                                            }
                                        });
                                        alertDialog1.show();
                                    }

                                } catch (Throwable t) {
                                    Log.e("api", "Could not parse malformed JSON: \"" + m.getData() + "\"");
                                }
                            } else {
                                // handle error response
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                Toast.makeText(Abha_Login.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                    AlertDialog alertDialog = new AlertDialog.Builder(Abha_Login.this).create();
                                    alertDialog.setTitle("Error");
                                    alertDialog.setIcon(R.drawable.baseline_error_24);
                                    alertDialog.setMessage(jObjError.getString("message"));
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();

                                } catch (Exception e) {
                                        AlertDialog alertDialog2 = new AlertDialog.Builder(Abha_Login.this).create();
                                        alertDialog2.setTitle("Error");
                                        alertDialog2.setIcon(R.drawable.baseline_error_24);
                                        alertDialog2.setMessage("An Unknown Error has Occurred!");
                                        alertDialog2.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                alertDialog2.dismiss();
                                            }
                                        });
                                        alertDialog2.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response_Model_Login> call, Throwable t) {
                            // handle failure
                            Log.e("api", t.toString());
                            Toast.makeText(Abha_Login.this, "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        });

        send_otp_to_aadhaar_linked_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // write here your code for example ...
                if(isChecked){
                    // do somtheing when is checked
                    authMethod = "AADHAAR_OTP";

//                    Toast.makeText(MainActivity.this, stat, Toast.LENGTH_SHORT).show();
//                    Log.d("api", "onCheckedChanged: rb2  "+stat);
                }else{
                    // do somthing when is removed the check**strong text**
                }

            }
        });
        send_otp_to_abha_linked_mobile_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // write here your code for example ...
                if(isChecked){
                    // do somtheing when is checked
                    authMethod = "MOBILE_OTP";

//                    Toast.makeText(MainActivity.this, stat, Toast.LENGTH_SHORT).show();
//                    Log.d("api", "onCheckedChanged: rb2  "+stat);
                }else{
                    // do somthing when is removed the check**strong text**
                }

            }
        });


        forgot_abha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(to_forgot_abha);
                overridePendingTransition(R.anim.slide_in_right  , R.anim.slide_out_left);

            }
        });
        register_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(to_register_for_abha);
                overridePendingTransition(R.anim.slide_in_right  , R.anim.slide_out_left);

            }
        });

        otp_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    otp_et.setBackground(getDrawable(R.drawable.et_in_focus));
                }
                else {
                    otp_et.setBackground(getDrawable(R.drawable.circularbordersolid_grey));
                }
            }
        });
        healthid_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    healthid_et.setBackground(getDrawable(R.drawable.et_in_focus));
                }
                else {
                    healthid_et.setBackground(getDrawable(R.drawable.circularbordersolid_grey));
                }
            }
        });
    }

}
