package com.example.phrapp;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class Abha_profile_section_user_info_fragment extends Fragment {

    private String profile_details;
    private String x_token;
    private String user_mobile_str;
    private String user_email_str;
    private String user_address_str;
    private String health_id_str;
    private String phr_address_str;
    private String dob_date_str;
    private String dob_month_str;
    private String dob_year_str;
    private String gender_str;

    private TextView user_mobile_tv;
    private TextView user_address_tv;
    private TextView user_email_tv;
    private TextView health_id_tv;
    private TextView dob_tv;
    private TextView phr_address_tv;
    private TextView gender_tv;
    private Button abha_delete_profile_btn;
    private Button abha_logout_btn;
    private TextView abha_update_mobile_btn;
    private TextView abha_update_email_btn;
    private TextView abha_edit_contact_btn;
    private Intent to_abha_delete_page;
    private Intent to_abha_update_mobile_page;
    private Intent to_abha_update_email_page;
    private Intent to_login_with_email_page;

    public Abha_profile_section_user_info_fragment() {
        // Required empty public constructor
    }


    public static Abha_profile_section_user_info_fragment newInstance(String param1, String param2) {
        Abha_profile_section_user_info_fragment fragment = new Abha_profile_section_user_info_fragment();
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
                Toast.makeText(getActivity(), "Some Error Occurred while fetching your details!", Toast.LENGTH_SHORT).show();
            }
            if(this.getArguments().getString("X_token") != null){
                x_token = this.getArguments().getString("X_token");
            }
            else {
                Log.e("api" , "Error while fetching X_token details in user info section!");
                Toast.makeText(getActivity(), "Some Error Occurred while fetching your details!", Toast.LENGTH_SHORT).show();
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
        return inflater.inflate(R.layout.fragment_abha_profile_section_user_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        to_abha_delete_page = new Intent(getActivity() , Abha_Delete_abha.class);
        to_abha_update_mobile_page = new Intent(getActivity() , Abha_update_mobile.class);
        to_abha_update_email_page = new Intent(getActivity() , Abha_update_email.class);
        to_login_with_email_page = new Intent(getActivity() , login2.class);
        user_mobile_tv = getView().findViewById(R.id.abha_profile_section_mobile_tv);
        user_address_tv = getView().findViewById(R.id.abha_profile_section_address_tv);
        user_email_tv = getView().findViewById(R.id.abha_profile_section_email_tv);
        health_id_tv = getView().findViewById(R.id.abha_profile_section_health_id_tv);
        phr_address_tv = getView().findViewById(R.id.abha_profile_section_phr_address_tv);
        dob_tv = getView().findViewById(R.id.abha_profile_section_dob_tv);
        gender_tv = getView().findViewById(R.id.abha_profile_section_gender_tv);
        abha_delete_profile_btn = getView().findViewById(R.id.abha_profile_section_delete_abha_btn);
        abha_edit_contact_btn = getView().findViewById(R.id.abha_profile_section_user_info_edit_contact_tv);
        abha_update_email_btn = getView().findViewById(R.id.abha_profile_section_update_email_tv);
        abha_update_mobile_btn = getView().findViewById(R.id.abha_profile_section_user_info_update_mobile_btn);
        abha_logout_btn = getView().findViewById(R.id.abha_profile_section_logout_btn);


        try{
            JSONObject obj = new JSONObject(profile_details);
            user_mobile_str = obj.getString("mobile");
            user_email_str = obj.getString("email");
            if(user_email_str == "null" || user_email_str==null){
                Log.d("api" , "no email");
                user_email_tv.setText("Email Not Registered");
            }
            else{
//                Log.d("api" , user_email_str);
                user_email_tv.setText(user_email_str);
            }
            user_address_str = obj.getString("address");
            if(user_address_str == null || user_address_str=="null"){
                Log.d("api" , "no address");
                user_address_tv.setText(" - ");
            }
            else{
                user_address_tv.setText(user_address_str);
            }
            health_id_str = obj.getString("healthIdNumber");
            if(health_id_str == null || health_id_str=="null"){
                Log.d("api" , "no address");
                health_id_tv.setText(" - ");
            }
            else{
                health_id_tv.setText(health_id_str);
            }
            phr_address_str = obj.getString("phrAddress");
            if(phr_address_str == null || phr_address_str=="null"){
                Log.d("api" , "no address");
                phr_address_tv.setText(" - ");
            }
            else{
                phr_address_tv.setText(phr_address_str);
            }
            dob_date_str = obj.getString("dayOfBirth");
            dob_month_str = obj.getString("monthOfBirth");
            dob_year_str = obj.getString("yearOfBirth");
            if(dob_date_str == null || dob_date_str=="null"||dob_month_str == null || dob_month_str=="null"||dob_year_str == null || dob_year_str=="null"){
//                Log.d("api" , "no address");
                dob_tv.setText(" Can't Load! ");
//                dob_date_str =
            }
            else{
                dob_tv.setText(dob_date_str +"-"+dob_month_str+"-"+dob_year_str);
            }
            gender_str = obj.getString("gender");
            if(gender_str == null || gender_str=="null"){
                Log.d("api" , "no address");
                gender_tv.setText(" - ");
            }
            else{
                if(gender_str == "M")
                    gender_tv.setText("Male");
                if(gender_str == "F")
                    gender_tv.setText("Female");
                else
                    gender_tv.setText(gender_str);
            }
            if(user_mobile_str == null || user_mobile_str == "null"){
                user_mobile_tv.setText(" - ");
            }
            else{
                user_mobile_tv.setText(user_mobile_str);
            }


//            abha_profile_section_user_name.setText(user_name_str);
//            profile_photo_base_64_string = obj.getString("profilePhoto");
//            profile_photo_bytes = Base64.decode(profile_photo_base_64_string.getBytes() , Base64.DEFAULT);
//            profile_photo.setImageBitmap(BitmapFactory.decodeByteArray(profile_photo_bytes, 0, profile_photo_bytes.length));
//            Base64.(profile_photo_base_64_string.getBytes())
        }
        catch (JSONException e) {
            Toast.makeText(getActivity(), "Some Error Occurred while fetching your profile !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        abha_delete_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_abha_delete_page.putExtra("profile_details" , profile_details);
                to_abha_delete_page.putExtra("X_token" , x_token);
                startActivity(to_abha_delete_page);
            }
        });
        abha_update_mobile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                to_abha_delete_page.putExtra("profile_details" , profile_details);
                to_abha_update_mobile_page.putExtra("X_token" , x_token);
                startActivity(to_abha_update_mobile_page);
            }
        });

        abha_update_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                to_abha_delete_page.putExtra("profile_details" , profile_details);
                to_abha_update_email_page.putExtra("X_token" , x_token);
                startActivity(to_abha_update_email_page);
            }
        });

        abha_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // deleting login info for next time login.....
                SharedPreferences pref = getActivity().getSharedPreferences("login" , MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isAbhaLoggedIn" , false);
                editor.putString("token_details" , "");
                editor.apply();

                Toast.makeText(getActivity(), "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                to_login_with_email_page.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                FirebaseAuth.getInstance().signOut();
                startActivity(to_login_with_email_page);
            }
        });

        abha_edit_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                abha_edit_contact_btn.setVisibility(View.GONE);
                if(abha_edit_contact_btn.getText().equals("Edit")){
                    abha_edit_contact_btn.setText("Edit ⛌");
                }
                else abha_edit_contact_btn.setText("Edit");

                if(abha_update_mobile_btn.getVisibility()==View.VISIBLE){
                    abha_update_mobile_btn.setVisibility(View.GONE);
                }
                else abha_update_mobile_btn.setVisibility(View.VISIBLE);

                if(user_email_tv.getVisibility()==View.VISIBLE){
                    user_email_tv.setVisibility(View.GONE);
                }
                else user_email_tv.setVisibility(View.VISIBLE);

                if(user_mobile_tv.getVisibility()==View.VISIBLE){
                    user_mobile_tv.setVisibility(View.GONE);
                }
                else user_mobile_tv.setVisibility(View.VISIBLE);

                if(abha_update_email_btn.getVisibility()==View.VISIBLE){
                    abha_update_email_btn.setVisibility(View.GONE);
                }
                else abha_update_email_btn.setVisibility(View.VISIBLE);


//                abha_update_email_btn.setVisibility(View.VISIBLE);
//                user_mobile_tv.setVisibility(View.GONE);
//                user_email_tv.setVisibility(View.GONE);
            }
        });






    }
}