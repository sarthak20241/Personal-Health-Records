package com.example.phrapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


public class retrieve_abha_id_via_mobile_verify_otp_and_demo extends Fragment {


    private Button verify;

    private EditText first_name_et;
    private EditText middle_name_et;
    private EditText last_name_et;
    private EditText health_id_et;
    private EditText otp_et;


    private String txnid;

    private String health_id_number;

    public retrieve_abha_id_via_mobile_verify_otp_and_demo() {
        // Required empty public constructor
    }


    public static retrieve_abha_id_via_mobile_verify_otp_and_demo newInstance(String param1, String param2) {
        retrieve_abha_id_via_mobile_verify_otp_and_demo fragment = new retrieve_abha_id_via_mobile_verify_otp_and_demo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        txnid = this.getArguments().getString("txnId");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_retrieve_abha_id_via_mobile_verify_otp_and_demo, container, false);
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


        verify = getView().findViewById(R.id.abha_register_via_aadhaar_create_health_id_with_pre_verified_Register_btn);
        first_name_et = getView().findViewById(R.id.abha_register_via_aadhaar_create_health_id_with_pre_verified_First_name_et);
        last_name_et = getView().findViewById(R.id.abha_register_via_aadhaar_create_health_id_with_pre_verified_Last_name_et);
        middle_name_et = getView().findViewById(R.id.abha_register_via_aadhaar_create_health_id_with_pre_verified_Middle_name_et);
        health_id_et = getView().findViewById(R.id.abha_register_via_aadhaar_create_health_id_with_pre_verified_Health_ID_et);
        otp_et = getView().findViewById(R.id.abha_register_via_aadhaar_create_health_id_with_pre_verified_Password_et);




        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_otp();
            }

            private void get_otp() {

                Log.d("api" , txnid);

                JSONObject json_request = new JSONObject();
                try {
                    json_request.put("email","");
                    json_request.put("firstName", first_name_et.getText().toString());
                    json_request.put("middleName", middle_name_et.getText().toString());
                    json_request.put("lastName", last_name_et.getText().toString());
                    json_request.put("password", otp_et.getText().toString());
                    json_request.put("profilePhoto", "");
                    json_request.put("healthId", health_id_et.getText().toString());
                    json_request.put("txnid", txnid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                String req = gson.toJson(json_request);

                Request_Model requestModel = new Request_Model(req);
                Call<Response_Model> call = apiService.abha_registration_via_aadhaar_create_health_id_with_pre_verified_createPost(requestModel);
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
                                    health_id_number = obj.getString("healthIdNumber");
//                                    isMobileLinkedWithAadhaar = obj.getBoolean("mobileLinked");
//                                    Log.d("api" , String.valueOf(isMobileLinkedWithAadhaar));
                                    Log.d("api" , health_id_number);

                                    Toast.makeText(getActivity(), "ABHA Registration completed Successfully !", Toast.LENGTH_SHORT).show();
                                    try {
                                        Thread.sleep(2000); //1000 milliseconds is one second.
                                    }
                                    catch (InterruptedException e)
                                    {
                                        e.printStackTrace();
                                    }


                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("User_data" , obj.toString());
                                    AbhaRegisterDoneSuccessfullyPageFragment frag = new AbhaRegisterDoneSuccessfullyPageFragment();
                                    frag.setArguments(bundle1);
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