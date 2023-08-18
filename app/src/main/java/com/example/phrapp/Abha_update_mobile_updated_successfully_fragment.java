package com.example.phrapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Abha_update_mobile_updated_successfully_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Abha_update_mobile_updated_successfully_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView timer_tv;
    private CountDownTimer cTimer;

    public Abha_update_mobile_updated_successfully_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Abha_update_mobile_updated_successfully_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Abha_update_mobile_updated_successfully_fragment newInstance(String param1, String param2) {
        Abha_update_mobile_updated_successfully_fragment fragment = new Abha_update_mobile_updated_successfully_fragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abha_update_mobile_updated_successfully_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timer_tv = getView().findViewById(R.id.abha_changed_mobile_successfully_timer_to_finish);

        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer_tv.setText("Taking back to profile page in "+String.valueOf(millisUntilFinished/1000)+"..");
            }
            public void onFinish() {
                cTimer.cancel();
//                getActivity().finishActivity(200);
                getActivity().finish();
            }
        };
        cTimer.start();

    }
}