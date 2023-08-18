package com.example.phrapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phrapp.sampledata.Request_Model;
import com.example.phrapp.sampledata.Response_Model;
import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Abha_profile_section_get_abha_id_card_fragment extends Fragment {

    private ImageView abha_id_card_iv;
    private String token_details;
    private String token;
    private String abha_id_card_base_64_string;

    private byte[] abha_id_card_bytes;


    public Abha_profile_section_get_abha_id_card_fragment() {
        // Required empty public constructor
    }


    public static Abha_profile_section_get_abha_id_card_fragment newInstance(String param1, String param2) {
        Abha_profile_section_get_abha_id_card_fragment fragment = new Abha_profile_section_get_abha_id_card_fragment();
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
            token_details = this.getArguments().getString("X_token");
            Log.d("api" , "hello " + token_details);
        }
        else{
            token_details = "";
            Toast.makeText(getActivity(), "Some Error Occurred while Loading Profile data!", Toast.LENGTH_SHORT).show();
            Log.d("api" , "nopes");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abha_profile_section_get_abha_id_card_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // FOR API CALLS
        String BASE_URL = ApiService.url;
//                OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        abha_id_card_iv = getView().findViewById(R.id.abha_profile_section_abha_id_card_iv);
//        abha_id_card_iv.setImageResource(R.drawable.ic_error_foreground);


//        token_details = intent.getStringExtra(EXTRA_MESSAGE);



        try {

            JSONObject obj = new JSONObject(token_details);

            token = obj.getString("token");
            Log.d("api" , "token:  "+token);

            JSONObject json_request = new JSONObject();
            try {
                json_request.put("X_Token",token);

            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Some Error Ocurred with your Login Token !", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            Gson gson = new Gson();
            String req = gson.toJson(json_request);

            Request_Model requestModel = new Request_Model(req);
            Call<ResponseBody> call = apiService.abha_get_abha_id_card(requestModel);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // handle success response
//                        Response_Model m = response.body();


                            Log.d("api" , response.body().toString());

                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                byte[] imageData = response.body().bytes();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                                abha_id_card_iv.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Handle the error
                        }
//                                if (m.getData()=="null" || m.getData() == null || m.getData() == "") {
//                                    Toast.makeText(getActivity(), "Some error occured while taking response from the server!", Toast.LENGTH_SHORT).show();
//                                    Log.d("api", "server returned null in svg card");
//
//                                } else {

//                                    ImageView imageView = getfindViewById(R.id.my_image_view);
//                                    String imageUrl = "http://192.168.0.169:3000//get_abha_id_card";
//
//                                    Glide.with(getActivity())
//                                            .load(imageUrl)
//                                            .into(abha_id_card_iv);

//                        Picasso.get().load("http://192.168.0.169:3000/image").into(abha_id_card_iv);



//                                    OkHttpClient client = new OkHttpClient();
//
//// Create a new request object
//                                    Request request22 = new Request.Builder()
//                                            .url("http://192.168.0.169:3000/image")
//                                            .build();
//
//// Send the request and receive the response
//                                    okhttp3.Response response2 = null;
//                                    try {
//                                        response2 = client.newCall(request22).execute();
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
//
//// Check if the response was successful
//                                    if (response2.isSuccessful() && response2.body() != null) {
//                                        // Get the response body as a byte array
//                                        byte[] responseBody = new byte[0];
//                                        try {
//                                            responseBody = response2.body().bytes();
//                                        } catch (IOException e) {
//                                            throw new RuntimeException(e);
//                                        }
//
//                                        // Convert the response body to a Bitmap object
//                                        Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//
//                                        // Display the Bitmap object in an ImageView
////                                        ImageView imageView = getView().findViewById(R.id.abha_id_card_iv);
//                                        abha_id_card_iv.setImageBitmap(bitmap);
//                                    }


//                                    byte[] imageData = new byte[m.getData().getBytes().limit()];
//                                    imageBuffer.get(imageData);
//
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//                                    Picasso.get().load("http://192.168.0.169:3000/image").into(abha_id_card_iv);

//
//                                    abha_id_card_base_64_string = m.getData();
//                                    int a = m.getData().length();
//                                    Log.d("m.get datta = = "  , String.valueOf(a));
//
//
//
//                                    byte[] abha_id_card_bytes = abha_id_card_base_64_string.getBytes();
//
//                                    Log.d("m.get datta = = "  , abha_id_card_bytes.toString());

//                                    abha_id_card_iv.setImageBitmap(BitmapFactory.decodeByteArray(abha_id_card_bytes, 0, abha_id_card_bytes.length));
//                                }

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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // handle failure
                    Toast.makeText(getActivity(), "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();
                }
            });

//            Request_Model requestModel = new Request_Model(req);
//            Call<Response_Model> call = apiService.abha_get_abha_id_card(requestModel);
//            call.enqueue(new Callback<Response_Model>() {
//                @Override
//                public void onResponse(Call<Response_Model> call, Response<Response_Model> response) {
//                    if (response.isSuccessful()) {
//                        // handle success response
//                        Response_Model m = response.body();
//
//
//                            Log.d("api" , m.getData());
//                                if (m.getData()=="null" || m.getData() == null || m.getData() == "") {
//                                    Toast.makeText(getActivity(), "Some error occured while taking response from the server!", Toast.LENGTH_SHORT).show();
//                                    Log.d("api", "server returned null in svg card");
//
//                                } else {
//
//                                    byte[] imageData = new byte[m.getData().getBytes().limit()];
//                                    imageBuffer.get(imageData);
//
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//
////
////                                    abha_id_card_base_64_string = m.getData();
////                                    int a = m.getData().length();
////                                    Log.d("m.get datta = = "  , String.valueOf(a));
////
////
////
////                                    byte[] abha_id_card_bytes = abha_id_card_base_64_string.getBytes();
////
////                                    Log.d("m.get datta = = "  , abha_id_card_bytes.toString());
//
//                                    abha_id_card_iv.setImageBitmap(BitmapFactory.decodeByteArray(abha_id_card_bytes, 0, abha_id_card_bytes.length));
//                                }
//
//                    } else {
//                        // handle error response
//                        try {
//                            JSONObject jObjError = new JSONObject(response.errorBody().string());
////                                Toast.makeText(Abha_Login.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
//                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                            alertDialog.setTitle("Error");
//                            alertDialog.setIcon(R.drawable.baseline_error_24);
//                            alertDialog.setMessage(jObjError.getString("message"));
//                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    alertDialog.dismiss();
//                                }
//                            });
//                            alertDialog.show();
//
//                        } catch (Exception e) {
//                            AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
//                            alertDialog2.setTitle("Error");
//                            alertDialog2.setIcon(R.drawable.baseline_error_24);
//                            alertDialog2.setMessage("An Unknown Error has Occurred!");
//                            alertDialog2.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    alertDialog2.dismiss();
//                                }
//                            });
//                            alertDialog2.show();
//                        }
//                    }
//                }

//                @Override
//                public void onFailure(Call<Response_Model> call, Throwable t) {
//                    // handle failure
//                    Toast.makeText(getActivity(), "Can't Connect to the server! Please Retry.", Toast.LENGTH_SHORT).show();
//                }
//            });


        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Some Error occurred while fetching your details !", Toast.LENGTH_SHORT).show();
            Log.e("api", "Some Error occurred while fetching your details !");
        }



    }
}