package com.example.phrapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phrapp.sampledata.Request_Model;
import com.example.phrapp.sampledata.Response_Model;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class delete_abha_id_via_aadhaar_fragment extends Fragment {


    private Button delete_abha_btn;
    private Button get_otp;

    private Intent to_login_page_intent;

//    private EditText aadhaar_no_et;
    private EditText otp_et;
    private String txn_id;
    private String health_id;
    private CountDownTimer cTimer;
    private String profile_details;
    private String x_token;


    public delete_abha_id_via_aadhaar_fragment() {
        // Required empty public constructor
    }


    public static delete_abha_id_via_aadhaar_fragment newInstance(String param1, String param2) {
        delete_abha_id_via_aadhaar_fragment fragment = new delete_abha_id_via_aadhaar_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.getArguments() != null){

            if(this.getArguments().getString("profile_details") != null){
                profile_details = this.getArguments().getString("profile_details");
            }
            else {
                Log.e("api" , "Error while fetching profile details in user info section!");
            }
            if(this.getArguments().getString("X_token") != null){
                x_token = this.getArguments().getString("X_token");
            }
            else {
                Log.e("api" , "Error while fetching X_token details in user info section!");
            }
            Log.d("api" , "hello " + profile_details);
        }
        else{
            profile_details = "";
            x_token = "";
            Toast.makeText(getActivity(), "Some Error Occurred while Loading Profile data!", Toast.LENGTH_SHORT).show();
            Log.d("api" , "nopes");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_abha_id_via_aadhaar_fragment, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String BASE_URL = ApiService.url;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);


        delete_abha_btn = getView().findViewById(R.id.abha_delete_abha_via_aadhaar_delete_btn);
//        aadhaar_no_et = getView().findViewById(R.id.abha_forgot_abha_via_aadhaar_aadhaar_no_et);
        otp_et = getView().findViewById(R.id.abha_delete_abha_via_aadhaar_otp_et);
        get_otp = getView().findViewById(R.id.abha_delete_abha_via_aadhaar_get_otp_btn);

        to_login_page_intent = new Intent(getActivity() , Abha_Login.class);





        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_otp();
            }

            private void get_otp() {


                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("X_Token", x_token);
//                    json_request.put("otp", otp_et.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_delete_via_aadhaar_get_otp_createPost(requestModel);
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
                                    txn_id = obj.getString("txnId");
//                                    isMobileLinkedWithAadhaar = obj.getBoolean("mobileLinked");
//                                    Log.d("api" , String.valueOf(isMobileLinkedWithAadhaar));
                                    Log.d("api" , txn_id);



                                    Toast.makeText(getActivity(), "OTP sent Successfully !", Toast.LENGTH_SHORT).show();

                                    get_otp.setEnabled(false);
                                    delete_abha_btn.setEnabled(true);
                                    delete_abha_btn.getBackground().setTintList(
                                            ContextCompat.getColorStateList(getActivity(), R.color.btn_theme));

                                    get_otp.setCompoundDrawablesWithIntrinsicBounds(getActivity().getDrawable(R.drawable.baseline_refresh_24) , null,null,null);
//                            get_otp_btn.setBackgroundTintList(get_otp_btn.getResources().getColorStateList(R.color.your_xml_name));
                                    get_otp.getBackground().setTintList(
                                            ContextCompat.getColorStateList(getActivity(), R.color.btn_inactive_theme));
                                    cTimer = new CountDownTimer(30000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            get_otp.setText(String.valueOf(millisUntilFinished/1000));
                                        }
                                        public void onFinish() {
//                                    tv.setText("Re send OTP!");
                                            get_otp.setEnabled(true);
                                            get_otp.getBackground().setTintList(
                                                    ContextCompat.getColorStateList(getActivity(), R.color.btn_theme));
                                            get_otp.setText("GET OTP");

//                                    get_otp_btn.setback(getColor("#C0E862"));
                                            get_otp.setCompoundDrawablesWithIntrinsicBounds(null, null,null,null);
                                        }
                                    };
                                    cTimer.start();
//
//
                                }
                                else{
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
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
                                Toast.makeText(getActivity(), "SomeErrorOccured while taking response from backend!", Toast.LENGTH_SHORT).show();
                                Log.e("api", "Could not parse malformed JSON: \"" + m.getData()+ "\"");
                            }
//
                        } else {
                            // handle error response
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                Toast.makeText(Abha_Login.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
                                AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
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
                        Log.e("api", t.toString());
                        Toast.makeText(getActivity(), "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        delete_abha_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_otp();
            }

            private void verify_otp() {


                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("X_Token", x_token);
                    json_request.put("otp", otp_et.getText().toString());
                    json_request.put("txnId", txn_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_delete_via_aadhaar_verify_otp_and_delete_createPost(requestModel);
                call.enqueue(new Callback<Response_Model>() {
                    @Override
                    public void onResponse(Call<Response_Model> call, Response<Response_Model> response) {
                        if (response.isSuccessful()) {
                            // handle success response
                            Response_Model m = response.body();
                            try {

                                JSONObject obj = new JSONObject(m.getData());

                                if(obj != null) {
                                    Log.d("api", obj.toString());
                                    if (obj.getString("status").equals("deleted")) {
                                        Toast.makeText(getActivity(), "ABHA Deleted Successfully !", Toast.LENGTH_SHORT).show();
                                        AlertDialog alertDialog12 = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog12.setTitle("Success");
                                        alertDialog12.setIcon(R.drawable.baseline_done_24);
                                        alertDialog12.setMessage("ABHA Deleted Successfully !\n Click on 'Go to Login Page'.");
                                        alertDialog12.setButton(DialogInterface.BUTTON_POSITIVE, "Go to Login Page", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                alertDialog12.dismiss();
                                                to_login_page_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(to_login_page_intent);

                                            }
                                        });
                                        alertDialog12.show();

                                    }
                                }
                                else{
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
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
                                Toast.makeText(getActivity(), "SomeErrorOccured while taking response from backend!", Toast.LENGTH_SHORT).show();
                                Log.e("api", "Could not parse malformed JSON: \"" + m.getData()+ "\"");
                            }
//
                        } else {
                            // handle error response
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
//                                Toast.makeText(Abha_Login.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
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
                                AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
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
                        Log.e("api", t.toString());
                        Toast.makeText(getActivity(), "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        otp_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    otp_et.setBackground(getActivity().getDrawable(R.drawable.et_in_focus));
                }
                else {
                    otp_et.setBackground(getActivity().getDrawable(R.drawable.circularbordersolid_grey));
                }
            }
        });
//        aadhaar_no_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus) {
//                    aadhaar_no_et.setBackground(getActivity().getDrawable(R.drawable.et_in_focus));
//                }
//                else {
//                    aadhaar_no_et.setBackground(getActivity().getDrawable(R.drawable.circularbordersolid_grey));
//                }
//            }
//        });
    }
}