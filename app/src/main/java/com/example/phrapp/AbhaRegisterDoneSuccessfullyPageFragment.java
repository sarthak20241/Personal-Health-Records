package com.example.phrapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class AbhaRegisterDoneSuccessfullyPageFragment extends Fragment {

    private String h_id;

    private TextView page_head_tv;
    private TextView greet_tv;

    private Button to_login_page_btn;

    private Intent to_login_page_intent;
    private TextView health_id_tv;
    private ImageView copy_health_id_iv;


    public AbhaRegisterDoneSuccessfullyPageFragment() {
        // Required empty public constructor
    }


    public static AbhaRegisterDoneSuccessfullyPageFragment newInstance(String param1, String param2) {
        AbhaRegisterDoneSuccessfullyPageFragment fragment = new AbhaRegisterDoneSuccessfullyPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        h_id = this.getArguments().getString("health_id");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abha_register_done_successfully_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        to_login_page_intent = new Intent(getActivity() , Abha_Login.class);
        Log.d("api" , "data = "+h_id);
        page_head_tv = getView().findViewById(R.id.abha_register_done_successfully_head_tv);
        to_login_page_btn = getView().findViewById(R.id.abha_register_done_successfully_to_login_page_btn);

        health_id_tv = getView().findViewById(R.id.abha_register_done_successfully_health_id_tv);
        copy_health_id_iv = getView().findViewById(R.id.abha_register_done_successfully_copy_health_id_iv);

        health_id_tv.setText(h_id);



        to_login_page_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(to_login_page_intent);
            }
        });

        copy_health_id_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("healthId",health_id_tv.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(), "Copied!", Toast.LENGTH_LONG).show();
            }
        });




    }
}