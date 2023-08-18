package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phrapp.sampledata.Request_Model;
import com.example.phrapp.sampledata.Response_Model;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class fragment_User_logged_in_via_abha_Homescreen_fragment extends Fragment {

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
    private ImageView medical_timeline_btn;
    private ImageView my_account_btn;
    private String refreshToken;

    public fragment_User_logged_in_via_abha_Homescreen_fragment() {
        // Required empty public constructor
    }


    public static fragment_User_logged_in_via_abha_Homescreen_fragment newInstance(String param1, String param2) {
        fragment_User_logged_in_via_abha_Homescreen_fragment fragment = new fragment_User_logged_in_via_abha_Homescreen_fragment();
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

            if(this.getArguments().getString("token_details") != null){
                token_details = this.getArguments().getString("token_details");
            }
            else {
                Log.e("api" , "Error while fetching token_details!");
                Toast.makeText(getActivity(), "Some Error Occurred while fetching your token details!", Toast.LENGTH_SHORT).show();
            }
//            Log.d("api" , "hello " + profile_details);
        }
        else{
            token_details = "";
            Toast.makeText(getActivity(), "Some Error Occurred while Loading Profile data! Reopen the App!", Toast.LENGTH_SHORT).show();
            Log.d("api" , "nopes");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__user_logged_in_via_abha__homescreen_fragment, container, false);
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // Blocking Touch events as until the objects are loaded.
//        ((User_Logged_in_via_abha_HomePage) getActivity()).enable(false);




        to_profile_section_intent = new Intent(getActivity() , Abha_Profile_section.class);
        to_login_page = new Intent(getActivity() , Abha_Login.class);
//        to_medical_timeline


        vitals_button = getView().findViewById(R.id.user_logged_in_via_abha_vitals_button);
        medical_timeline_btn = getView().findViewById(R.id.abha_logged_in_medicalTimeLineButton);
        my_account_btn = getView().findViewById(R.id.abha_logged_in_MyaccountBuutton);

        get_profile_section = getView().findViewById(R.id.abha_user_logged_in_homepage_profile_icon_iv);

        textView = getView().findViewById(R.id.user_logged_in_via_abha_homepage_greetings_tb);

        // FOR API CALLS
        String BASE_URL = ApiService.url;
//                OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);



        try {

            JSONObject obj = new JSONObject(token_details);

            // Refreshing the X-Token
            refreshToken = obj.getString("refreshToken");
            JSONObject json_request1 = new JSONObject();

            try {
                json_request1.put("refreshToken",refreshToken);

            } catch (JSONException e) {
                AlertDialog alertDialog6 = new AlertDialog.Builder(getActivity()).create();
                alertDialog6.setTitle("Error");
                alertDialog6.setIcon(R.drawable.baseline_error_24);
                alertDialog6.setMessage("Some Error has Occurred with your Login Token/session !");
                alertDialog6.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog6.dismiss();
                    }
                });
                alertDialog6.show();
            }
            Gson gson = new Gson();
            String req = gson.toJson(json_request1);

            Request_Model requestModel = new Request_Model(req);
            Call<Response_Model> call = apiService.refresh_X_Token(requestModel);
            call.enqueue(new Callback<Response_Model>() {
                @Override
                public void onResponse(Call<Response_Model> call, Response<Response_Model> response) {
                    if (response.isSuccessful()) {
                        // handle success response
                        Response_Model m = response.body();
                        try {

                            JSONObject obj = new JSONObject(m.getData());

                            if(obj != null){
                                Log.d("api", "re = "+obj.toString());
                                token = obj.getString("accessToken");
                                Log.d("api" , "NEW TOKEN = "+token);

                                // update token details with refreshed token
                                JSONObject token_details_in_json = new JSONObject(token_details);

                                token_details_in_json.put("token" , token);

                                token_details = token_details_in_json.toString();


                                ///////   Logging IN by fetching profile details of the user.

                                JSONObject json_request = new JSONObject();
                                try {
                                    json_request.put("X_Token",token);
//                                    Log.d("api" , "token recvd = "+token);

                                } catch (JSONException e) {
                                    AlertDialog alertDialog1 = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog1.setTitle("Error");
                                    alertDialog1.setIcon(R.drawable.baseline_error_24);
                                    alertDialog1.setMessage("Some Error has Occurred with your Login Token/session !");
                                    alertDialog1.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            alertDialog1.dismiss();
                                        }
                                    });
                                    alertDialog1.show();
                                }
                                Gson gson1 = new Gson();
                                String req1 = gson1.toJson(json_request);

                                Request_Model requestModel1 = new Request_Model(req1);
                                Call<Response_Model> call1 = apiService.abha_get_profile(requestModel1);

//                                Log.d("api" , "token recvd = "+token);
                                call1.enqueue(new Callback<Response_Model>() {
                                    @Override
                                    public void onResponse(Call<Response_Model> call, Response<Response_Model> response) {
                                        if (response.isSuccessful()) {
                                            // handle success response
                                            Response_Model m = response.body();
                                            try {

                                                JSONObject obj = new JSONObject(m.getData());

                                                if(obj != null){
                                                    Log.d("api", obj.toString());
                                                    profile_details = obj.toString();
                                                    firstName = obj.getString("firstName");

                                                    Log.d("api" , firstName);
                                                    textView.setText(firstName);

                                                    // filling profile photo
                                                    profile_photo_base_64_string = obj.getString("profilePhoto");
                                                    profile_photo_bytes = Base64.decode(profile_photo_base_64_string.getBytes() , Base64.DEFAULT);
                                                    get_profile_section.setImageBitmap(BitmapFactory.decodeByteArray(profile_photo_bytes, 0, profile_photo_bytes.length));

                                                    Toast.makeText(getActivity(), "Welcome, "+firstName+" !", Toast.LENGTH_SHORT).show();

                                                }
                                                else{
                                                    AlertDialog alertDialog5 = new AlertDialog.Builder(getActivity()).create();
                                                    alertDialog5.setTitle("Error");
                                                    alertDialog5.setIcon(R.drawable.baseline_error_24);
                                                    alertDialog5.setMessage("Some Error has Occurred with your Login Token/session !");
                                                    alertDialog5.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            alertDialog5.dismiss();
                                                        }
                                                    });
                                                    alertDialog5.show();
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
                                        Toast.makeText(getActivity(), "Error occurred while fetching your profile! Logging out....", Toast.LENGTH_SHORT).show();

                                        Log.e("api" , t.toString());
                                        startActivity(to_login_page);
                                    }
                                });

                            }
                            else{
                                AlertDialog alertDialog7 = new AlertDialog.Builder(getActivity()).create();
                                alertDialog7.setTitle("Error");
                                alertDialog7.setIcon(R.drawable.baseline_error_24);
                                alertDialog7.setMessage("Some Error has Occurred with your Login Token/session !");
                                alertDialog7.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        alertDialog7.dismiss();
                                    }
                                });
                                alertDialog7.show();
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
                            AlertDialog alertDialog8 = new AlertDialog.Builder(getActivity()).create();
                            alertDialog8.setTitle("Error");
                            alertDialog8.setIcon(R.drawable.baseline_error_24);
                            alertDialog8.setMessage(jObjError.getString("message"));
                            alertDialog8.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog8.dismiss();
                                    to_login_page.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(to_login_page);
                                }
                            });
                            alertDialog8.show();

                        } catch (Exception e) {
                            AlertDialog alertDialog9 = new AlertDialog.Builder(getActivity()).create();
                            alertDialog9.setTitle("Error");
                            alertDialog9.setIcon(R.drawable.baseline_error_24);
                            alertDialog9.setMessage("An Unknown Error has Occurred!");
                            alertDialog9.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    alertDialog9.dismiss();
                                }
                            });
                            alertDialog9.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response_Model> call, Throwable t) {



                    // handle failure
                    Toast.makeText(getActivity(), "Error occurred while fetching your profile in refreshing token! Logging out....", Toast.LENGTH_SHORT).show();

                    Log.e("api" , t.toString());
                    startActivity(to_login_page);
                }
            });



            // Logging IN



//            Log.d("api" , "token recvd = "+token);


        } catch (Throwable t) {


            AlertDialog alertDialog4 = new AlertDialog.Builder(getActivity()).create();
            alertDialog4.setTitle("Error");
            alertDialog4.setIcon(R.drawable.baseline_error_24);
            alertDialog4.setMessage("Some Error has Occurred with your Login Token/session !");
            alertDialog4.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog4.dismiss();
                }
            });
            alertDialog4.show();
        }












        get_profile_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String token_details = obj.toString();
                to_profile_section_intent.putExtra(EXTRA_MESSAGE , profile_details);
                to_profile_section_intent.putExtra("X_token" , token_details);

                startActivity(to_profile_section_intent);

            }

        });

        vitals_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                        .replace(R.id.user_logged_in_via_abha_homepage_fragment_frame , new VitalsFragment()).commit();
            }
        });
        medical_timeline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                        .replace(R.id.user_logged_in_via_abha_homepage_fragment_frame , new DateTimelineFragment()).commit();
            }
        });
        my_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in , R.anim.fade_out)
                        .replace(R.id.user_logged_in_via_abha_homepage_fragment_frame , new EditAccountFragment()).commit();
            }
        });
    }
}