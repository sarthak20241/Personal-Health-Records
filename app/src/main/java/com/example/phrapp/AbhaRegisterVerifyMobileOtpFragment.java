package com.example.phrapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class AbhaRegisterVerifyMobileOtpFragment extends Fragment {

    private Button verify_otp_btn;
    private Button get_otp_btn;
    private EditText mobile_no_et;

    private boolean isMobileLinkedWithAadhaar;
    private String txnid;
    private EditText otp_et;
    private CountDownTimer cTimer;

//    private static final String ARG_PARAM1 = "param1";
//    private String mParam1;




    public AbhaRegisterVerifyMobileOtpFragment() {
        // Required empty public constructor

    }


    public static AbhaRegisterVerifyMobileOtpFragment newInstance(String param1) {
        AbhaRegisterVerifyMobileOtpFragment fragment = new AbhaRegisterVerifyMobileOtpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        txnid = this.getArguments().getString("txnid");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abha_register_verify_mobile_otp, container, false);


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


        verify_otp_btn = getView().findViewById(R.id.abha_register_verify_mobile_verify_otp_btn);
//        login_btn.setEnabled(false);
        get_otp_btn = getView().findViewById(R.id.abha_register_verify_mobile_get_otp_btn);
//        login_btn.setEnabled(false);
        mobile_no_et = getView().findViewById(R.id.abha_register_verify_mobile_mobile_no_et);
        otp_et = getView().findViewById(R.id.abha_register_verfiy_mobile_otp_et);

        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_otp();
            }

            private void get_otp() {

                Log.d("api" , txnid);

                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("mobile", mobile_no_et.getText().toString());
                    json_request.put("txnid", txnid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_registration_via_aadhaar_verify_mobile_gen_mobile_otp_createPost(requestModel);
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
                                  get_otp_btn.setEnabled(false);
                                    verify_otp_btn.setEnabled(true);
                                    verify_otp_btn.getBackground().setTintList(
                                            ContextCompat.getColorStateList(getActivity(), R.color.btn_theme));

                                    get_otp_btn.setCompoundDrawablesWithIntrinsicBounds(getActivity().getDrawable(R.drawable.baseline_refresh_24) , null,null,null);
//                            get_otp_btn.setBackgroundTintList(get_otp_btn.getResources().getColorStateList(R.color.your_xml_name));
                                    get_otp_btn.getBackground().setTintList(
                                            ContextCompat.getColorStateList(getActivity(), R.color.btn_inactive_theme));
                                    cTimer = new CountDownTimer(30000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            get_otp_btn.setText(String.valueOf(millisUntilFinished/1000));
                                        }
                                        public void onFinish() {
//                                    tv.setText("Re send OTP!");
                                            get_otp_btn.setEnabled(true);
                                            get_otp_btn.getBackground().setTintList(
                                                    ContextCompat.getColorStateList(getActivity(), R.color.btn_theme));
                                            get_otp_btn.setText("GET OTP");
//                                    get_otp_btn.setback(getColor("#C0E862"));
                                            get_otp_btn.setCompoundDrawablesWithIntrinsicBounds(null, null,null,null);
                                        }
                                    };
                                    cTimer.start();
                                    Log.d("api" , txnid);

                                    Toast.makeText(getActivity(), "OTP sent successfully!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), "Some Error Occurred while taking response from backend!", Toast.LENGTH_SHORT).show();
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

        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("otp", otp_et.getText().toString());
                    json_request.put("txnid", txnid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_registration_via_aadhaar_verify_mobile_verify_mobile_otp_createPost(requestModel);


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

                                    Toast.makeText(getActivity(), "OTP verified successfully !", Toast.LENGTH_SHORT).show();
                                    try {
                                        Thread.sleep(1500); //1000 milliseconds is one second.
                                    }
                                    catch (InterruptedException e)
                                    {
                                        e.printStackTrace();
                                    }

                                    cTimer.cancel();


                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("txnid" , txnid);
                                    AbhaRegisterCreateHealthIdPreVerifiedFragment frag = new AbhaRegisterCreateHealthIdPreVerifiedFragment();
                                    frag.setArguments(bundle1);
                                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                                            .replace(R.id.abha_register_fragment_frame, frag).commit();
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

        mobile_no_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mobile_no_et.setBackground(getActivity().getDrawable(R.drawable.et_in_focus));
                }
                else {
                    mobile_no_et.setBackground(getActivity().getDrawable(R.drawable.circularbordersolid_grey));
                }
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

    }
}