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


public class retrieve_abha_id_via_mobile_generate_otp extends Fragment {


    private Button get_otp;
    private Button verify_otp;


    private EditText mobile_no_et;
    private EditText otp_et;

    private String txn_id;
    private String health_id;
    private CountDownTimer cTimer;




    public retrieve_abha_id_via_mobile_generate_otp() {
        // Required empty public constructor
    }

    public static retrieve_abha_id_via_mobile_generate_otp newInstance(String param1, String param2) {
        retrieve_abha_id_via_mobile_generate_otp fragment = new retrieve_abha_id_via_mobile_generate_otp();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retrieve_abha_id_via_mobile_generate_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        478620151139

        String BASE_URL = ApiService.url;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);


//        verify_otp = getView().findViewById(R.id.abha_forgot_abha_via_aadhaar_verify_otp_btn);
        mobile_no_et = getView().findViewById(R.id.abha_forgot_abha_via_mobile_mobile_no_et);
        otp_et = getView().findViewById(R.id.abha_forgot_abha_via_mobile_otp_et);
//        otp_et = getView().findViewById(R.id.abha_forgot_abha_via_aadhaar_otp_et);
        get_otp = getView().findViewById(R.id.abha_forgot_abha_via_mobile_get_otp_btn);
        verify_otp = getView().findViewById(R.id.abha_forgot_abha_via_mobile_verify_otp_btn);



        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_otp();
            }

            private void get_otp() {


                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("mobile", mobile_no_et.getText().toString());
//                    json_request.put("otp", otp_et.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_forgot_abha_retrieve_via_mobile_get_otp_createPost(requestModel);
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
                                    verify_otp.setEnabled(true);
                                    verify_otp.getBackground().setTintList(
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

        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_otp();
            }

            private void verify_otp() {


                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("otp", otp_et.getText().toString());
                    json_request.put("txnId", txn_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_forgot_abha_retrieve_via_mobile_verify_otp_createPost(requestModel);
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
                                    health_id = obj.getString("healthIdNumber");
//                                    isMobileLinkedWithAadhaar = obj.getBoolean("mobileLinked");
//                                    Log.d("api" , String.valueOf(isMobileLinkedWithAadhaar));
                                    Log.d("api" , health_id);

                                    Toast.makeText(getActivity(), "OTP Verified Successfully !", Toast.LENGTH_SHORT).show();

                                    try {
                                        Thread.sleep(1500); //1000 milliseconds is one second.
                                    }
                                    catch (InterruptedException e)
                                    {
                                        e.printStackTrace();
                                    }


                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("txnId" , obj.toString());
                                    retrieve_abha_id_via_mobile_verify_otp_and_demo frag = new retrieve_abha_id_via_mobile_verify_otp_and_demo();
                                    frag.setArguments(bundle1);
                                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                                            .replace(R.id.abha_forgot_abha_fragment_frame, frag).commit();

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
    }
}