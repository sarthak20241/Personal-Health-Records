package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phrapp.sampledata.Request_Model;
import com.example.phrapp.sampledata.Request_Model_Get_Otp;
import com.example.phrapp.sampledata.Request_Model_Login;
import com.example.phrapp.sampledata.Response_Model;
import com.example.phrapp.sampledata.Response_Model_Get_Otp;
import com.example.phrapp.sampledata.Response_Model_Login;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Abha_update_email extends AppCompatActivity {

    private Button verify_otp_btn;
    private Button get_otp_btn;
    private EditText email_et;
    private String txnid;
    private EditText otp_et;
    private Intent to_home_page;

    private CountDownTimer cTimer;
    private String x_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abha_update_email);

        // FOR API CALLS
        String BASE_URL = ApiService.url;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);


        verify_otp_btn = findViewById(R.id.abha_update_email_verify_otp_btn);
        get_otp_btn = findViewById(R.id.abha_update_email_get_otp_btn);
        email_et = findViewById(R.id.abha_update_email_email_id_et);
        otp_et = findViewById(R.id.abha_update_email_otp_et);


        to_home_page = new Intent(this , User_Logged_in_via_abha_HomePage.class);



        x_token = getIntent().getStringExtra("X_token");




        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_otp();
            }

            private void get_otp() {

                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("X_Token", x_token);
//                    Log.d("api" , " here = >  "+x_token);
                    json_request.put("email", email_et.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_update_email_get_otp_createPost(requestModel);

                call.enqueue(new Callback<Response_Model>() {
                    @Override
                    public void onResponse(Call<Response_Model> call, Response<Response_Model> response) {
                        if (response.isSuccessful()) {
                            // handle success response
                            Response_Model m = response.body();
                            try {

                                JSONObject obj = new JSONObject(m.getData());

                                if(obj != null){
                                    Log.d("api", obj.toString());
                                    txnid = obj.getString("txnId");
                                    Log.d("api" , txnid);

                                    get_otp_btn.setEnabled(false);
                                    verify_otp_btn.setEnabled(true);
                                    verify_otp_btn.getBackground().setTintList(
                                            ContextCompat.getColorStateList(Abha_update_email.this, R.color.btn_theme));

                                    get_otp_btn.setCompoundDrawablesWithIntrinsicBounds(Abha_update_email.this.getDrawable(R.drawable.baseline_refresh_24) , null,null,null);
//                            get_otp_btn.setBackgroundTintList(get_otp_btn.getResources().getColorStateList(R.color.your_xml_name));
                                    get_otp_btn.getBackground().setTintList(
                                            ContextCompat.getColorStateList(Abha_update_email.this, R.color.btn_inactive_theme));
                                    cTimer = new CountDownTimer(30000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            get_otp_btn.setText(String.valueOf(millisUntilFinished/1000));
                                        }
                                        public void onFinish() {
//                                    tv.setText("Re send OTP!");
                                            get_otp_btn.setEnabled(true);
                                            get_otp_btn.getBackground().setTintList(
                                                    ContextCompat.getColorStateList(Abha_update_email.this, R.color.btn_theme));
                                            get_otp_btn.setText("GET OTP");
//                                    get_otp_btn.setback(getColor("#C0E862"));
                                            get_otp_btn.setCompoundDrawablesWithIntrinsicBounds(null, null,null,null);
                                        }
                                    };
                                    cTimer.start();

                                    Toast.makeText(Abha_update_email.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(Abha_update_email.this, "SomeErrorOccured while taking response from backend!", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(AbhaRegisterVerifyAadhaarOtpFragment.this, "Some Error Ocurred!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Throwable t) {
                                Toast.makeText(Abha_update_email.this, "SomeErrorOccured while taking response from backend!", Toast.LENGTH_SHORT).show();
                                Log.e("api", "Could not parse malformed JSON: \"" + m.getData()+ "\"");
                            }

                        } else {
                            // handle error response
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                AlertDialog alertDialog = new AlertDialog.Builder(Abha_update_email.this).create();
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
                                AlertDialog alertDialog2 = new AlertDialog.Builder(Abha_update_email.this).create();
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
                    public void onFailure(Call<Response_Model> call, Throwable t) {
                        // handle failure
                        Log.e("api" , t.toString());
                        Toast.makeText(Abha_update_email.this, "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("X_Token", x_token);
                    json_request.put("otp", otp_et.getText().toString());
                    json_request.put("txnId", txnid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_update_email_verify_otp_createPost(requestModel);


                call.enqueue(new Callback<Response_Model>() {
                    @Override
                    public void onResponse(Call<Response_Model> call, Response<Response_Model> response) {
                        if (response.isSuccessful()) {
                            // handle success response
                            Response_Model m = response.body();
                            try {

                                String status = m.getData();

                                if(status.equals("true")){

//

                                    Toast.makeText(Abha_update_email.this, "OTP verified successfully !", Toast.LENGTH_SHORT).show();
//

                                    cTimer.cancel();
                                    AlertDialog alertDialog11 = new AlertDialog.Builder(Abha_update_email.this).create();
                                    alertDialog11.setTitle("Success");
                                    alertDialog11.setIcon(R.drawable.baseline_done_24);
                                    alertDialog11.setMessage("Your Email has been updated successfully!\n\nClick on go to homepage to see changes.");
                                    alertDialog11.setButton(DialogInterface.BUTTON_POSITIVE, "Go To Homepage", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alertDialog11.dismiss();
                                            to_home_page.putExtra(EXTRA_MESSAGE , x_token);
                                            to_home_page.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(to_home_page);
                                        }
                                    });
                                    alertDialog11.show();

//
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("X_token" , x_token);
//                                    bundle.putString("txnId" , txnid);
//                                    Abha_update_mobile_verify_mobile_or_aadhaar_otp frag = new Abha_update_mobile_verify_mobile_or_aadhaar_otp();
//                                    frag.setArguments(bundle);
//                                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
//                                            .replace(R.id.abha_update_mobile_fragment, frag).commit();
//
                                } else {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(Abha_update_email.this).create();
                                    alertDialog1.setTitle("Error");
                                    alertDialog1.setIcon(R.drawable.baseline_error_24);
                                    alertDialog1.setMessage("An Unknown Server Error has Occurred! \nEmail not Updated!");
                                    alertDialog1.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
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
//
                        } else {
                            // handle error response
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                Toast.makeText(Abha_Login.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                AlertDialog alertDialog = new AlertDialog.Builder(Abha_update_email.this).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setIcon(R.drawable.baseline_error_24);
                                alertDialog.setMessage(jObjError.getString("message"));
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.show();

                            } catch (Exception e) {
                                AlertDialog alertDialog2 = new AlertDialog.Builder(Abha_update_email.this).create();
                                alertDialog2.setTitle("Error");
                                alertDialog2.setIcon(R.drawable.baseline_error_24);
                                alertDialog2.setMessage("An Unknown Error has Occurred!");
                                alertDialog2.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
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
                    public void onFailure(Call<Response_Model> call, Throwable t) {
                        // handle failure
                        Log.e("api", t.toString());
                        Toast.makeText(Abha_update_email.this, "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        register_here.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(to_register_for_abha);
//                overridePendingTransition(R.anim.slide_in_right  , R.anim.slide_out_left);
//
//            }
//        });

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
        email_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    email_et.setBackground(getDrawable(R.drawable.et_in_focus));
                }
                else {
                    email_et.setBackground(getDrawable(R.drawable.circularbordersolid_grey));
                }
            }
        });

    }
}
