package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Abha_update_mobile_verify_mobile_or_aadhaar_otp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Abha_update_mobile_verify_mobile_or_aadhaar_otp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioButton send_otp_to_aadhaar_linked_rb;
    private RadioButton send_otp_to_abha_linked_mobile_rb;

    private String x_token;
    private String txnid;
    private String authMethod;

    private Button update_mobile_btn;
    private Button get_otp_btn;
    private EditText mobile_or_aadhaar_et;
    private EditText otp_et;

    private CountDownTimer cTimer;
    private String status_code;
    private Intent to_home_page;

    public Abha_update_mobile_verify_mobile_or_aadhaar_otp() {
        // Required empty public constructor
    }

    public static Abha_update_mobile_verify_mobile_or_aadhaar_otp newInstance(String param1, String param2) {
        Abha_update_mobile_verify_mobile_or_aadhaar_otp fragment = new Abha_update_mobile_verify_mobile_or_aadhaar_otp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(this.getArguments() != null){

            if(this.getArguments().getString("X_token") != null){
                x_token = this.getArguments().getString("X_token");
                Log.d("api", x_token);
            }
            else {
                Log.e("api" , "Error while fetching X_token details in user info section!");
                Toast.makeText(getActivity(), "Some Error Occurred while fetching your details!", Toast.LENGTH_SHORT).show();
            }
            if(this.getArguments().getString("txnId") != null){
                txnid = this.getArguments().getString("txnId");
                Log.d("api", txnid);
            }
            else {
                Log.e("api" , "Error while fetching txnId details in user info section!");
                Toast.makeText(getActivity(), "Some Error Occurred while fetching your details!", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            x_token = "";
            txnid = "";
            Toast.makeText(getActivity(), "Some Error Occurred while fetching your details!", Toast.LENGTH_SHORT).show();
            Log.d("api" , "nopes");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abha_update_mobile_verify_mobile_or_aadhaar_otp, container, false);
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

        to_home_page = new Intent(getActivity() , User_Logged_in_via_abha_HomePage.class);


        update_mobile_btn = getView().findViewById(R.id.abha_update_mobile_mobile_no_or_aadhaar_verify_otp_btn);
//        login_btn.setEnabled(false);
        get_otp_btn = getView().findViewById(R.id.abha_update_mobile_mobile_no_or_aadhaar_get_otp_btn);
//        login_btn.setEnabled(false);

        otp_et = getView().findViewById(R.id.abha_update_mobile_mobile_no_or_aadhaar_otp_et);


        send_otp_to_aadhaar_linked_rb = getView().findViewById(R.id.abha_update_mobile_send_aadhaar_otp_radioButton);
        send_otp_to_abha_linked_mobile_rb = getView().findViewById(R.id.abha_update_mobile_send_mobile_otp_radioButton);

        authMethod = "AADHAAR_OTP";

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
                    json_request.put("txnId", txnid);
                    json_request.put("X_Token", x_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call;
                if(authMethod == "AADHAAR_OTP"){
                    call = apiService.abha_update_mobile_get_aadhaar_otp_createPost(requestModel);
                }
                else{
                    call = apiService.abha_update_mobile_get_mobile_otp_createPost(requestModel);
                }

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
                                    update_mobile_btn.setEnabled(true);
                                    update_mobile_btn.getBackground().setTintList(
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
                                AlertDialog alertDialog3 = new AlertDialog.Builder(getActivity()).create();
                                alertDialog3.setTitle("Error");
                                alertDialog3.setIcon(R.drawable.baseline_error_24);
                                alertDialog3.setMessage("An Unknown Server Error has Occurred!");
                                alertDialog3.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog3.dismiss();
                                    }
                                });
                                alertDialog3.show();
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
                        Log.e("api" , t.toString());
                    }
                });


            }
        });

        update_mobile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("X_Token", x_token);
                    json_request.put("otp", otp_et.getText().toString());
                    json_request.put("txnId", txnid);
                    json_request.put("authMethod", authMethod);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_update_mobile_aadhaar_or_mobile_verify_otp_createPost(requestModel);


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
                                    status_code = obj.getString("status");
                                    Log.d("api" , status_code);

//                                    if(status_code.compareToIgnoreCase("204")==0){
                                        Toast.makeText(getActivity(), "Mobile Updated successfully !", Toast.LENGTH_SHORT).show();
//                                        try {
//                                            Thread.sleep(1500); //1000 milliseconds is one second.
//                                        }
//                                        catch (InterruptedException e)
//                                        {
//                                            e.printStackTrace();
//                                        }

                                        cTimer.cancel();

                                    AlertDialog alertDialog11 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog11.setTitle("Success");
                                    alertDialog11.setIcon(R.drawable.baseline_done_24);
                                    alertDialog11.setMessage("Your Mobile no. has been updated successfully!\n\nClick on go to homepage to see changes.");
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
//                                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
//                                            .replace(R.id.abha_update_mobile_fragment, new Abha_update_mobile_updated_successfully_fragment()).commit();
//                                    }

//                                    else{
//                                        Toast.makeText(getActivity(), "Some error occurred while updating mobile !", Toast.LENGTH_SHORT).show();
//                                    }


//
                                }
                                else{
                                    Toast.makeText(getActivity(), "SomeErrorOccured while taking response from backend!", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(AbhaRegisterVerifyAadhaarOtpFragment.this, "Some Error Ocurred!", Toast.LENGTH_SHORT).show();
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


    }
}