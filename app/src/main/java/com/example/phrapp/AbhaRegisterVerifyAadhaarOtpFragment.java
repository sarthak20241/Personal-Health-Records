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

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link AbhaRegisterVerifyAadhaarOtpFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class AbhaRegisterVerifyAadhaarOtpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button verify_otp_btn;
    private Button get_otp_btn;
    private EditText aadhaar_no_et;
    private String txnid;
    private EditText otp_et;
    private CountDownTimer cTimer;



    public AbhaRegisterVerifyAadhaarOtpFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AbhaRegisterVerifyAadhaarOtpFragment.
//     */
    // TODO: Rename and change types and number of parameters
    public static AbhaRegisterVerifyAadhaarOtpFragment newInstance() {
        AbhaRegisterVerifyAadhaarOtpFragment fragment = new AbhaRegisterVerifyAadhaarOtpFragment();


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
        return inflater.inflate(R.layout.fragment_abha_register_verify_aadhaar_otp_fragment, container, false);
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


        verify_otp_btn = getView().findViewById(R.id.abha_register_verify_aadhaar_verify_otp_btn);
//        login_btn.setEnabled(false);
        get_otp_btn = getView().findViewById(R.id.abha_register_verify_aadhaar_get_otp_btn);
//        login_btn.setEnabled(false);
        aadhaar_no_et = getView().findViewById(R.id.abha_register_verify_aadhaar_aadhaar_no_et);
        otp_et = getView().findViewById(R.id.abha_register_verfiy_aadhaar_otp_et);

//        to_registered_successfully_act= new Intent(this , User_Logged_in_via_abha_HomePage.class);
//        91-5780-8805-8677
//        91-6815-6342-0022


        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_otp();
            }

            private void get_otp() {

//                String token = "application/json";

//                RequestModel requestModel = new RequestModel(param1,param2);
                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("aadhaar", aadhaar_no_et.getText().toString());
//                    json_request.put("new", "aadhaar_no_et.getText().toString()");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.gen_otp_createPost(requestModel);
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
                Call<Response_Model> call = apiService.verify_otp_createPost(requestModel);


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

                                    Bundle bundle = new Bundle();
                                    bundle.putString("txnid" , txnid);
                                    AbhaRegisterVerifyMobileOtpFragment frag = new AbhaRegisterVerifyMobileOtpFragment();
                                    frag.setArguments(bundle);
                                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                                            .replace(R.id.abha_register_fragment_frame, frag).commit();
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

        aadhaar_no_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    aadhaar_no_et.setBackground(getActivity().getDrawable(R.drawable.et_in_focus));
                }
                else {
                    aadhaar_no_et.setBackground(getActivity().getDrawable(R.drawable.circularbordersolid_grey));
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