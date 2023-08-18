package com.example.phrapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    DatabaseReference ref;

    GoogleSignInClient mGoogleSignInClient;

    private Button logout_btn;
    private Intent to_user_login_page;

    public EditAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditAccountFragment newInstance(String param1, String param2) {
        EditAccountFragment fragment = new EditAccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);
        logout_btn  = view.findViewById(R.id.user_logout);
        to_user_login_page = new Intent(getActivity() , login2.class);
        Button updateBtn = view.findViewById(R.id.btnEditAccount);
        TextInputLayout name = view.findViewById(R.id.edit_name);
        TextInputLayout mobile= view.findViewById(R.id.edit_mobile);
        TextInputLayout email=  view.findViewById(R.id.edit_email);
        String nameEnteredold = name.getEditText().getText().toString();
        String mobileEnteredold = mobile.getEditText().getText().toString();
        String emailEnteredold = email.getEditText().getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("name").getValue().toString()!=null){
                    name.getEditText().setText( snapshot.child("name").getValue().toString());
                }
                if(snapshot.child("mobile").getValue().toString()!=null){
                    mobile.getEditText().setText( snapshot.child("mobile").getValue().toString());
                }
                if(snapshot.child("email").getValue().toString()!=null){
                    email.getEditText().setText( snapshot.child("email").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameEnterednew = name.getEditText().getText().toString();
                String mobileEnterednew = mobile.getEditText().getText().toString();
                String emailEnterednew = email.getEditText().getText().toString();
                DatabaseReference ref_child = ref.child(userid);
                if(!nameEnterednew.equals(nameEnteredold))
                    ref_child.child("name").setValue(nameEnterednew);
                if(!emailEnterednew.equals(emailEnteredold))
                    ref_child.child("email").setValue(emailEnterednew);
                if(!mobileEnterednew.equals(mobileEnteredold))
                    ref_child.child("mobile").setValue(mobileEnterednew);

                Toast.makeText(getContext(),"Details Updated Successfully",Toast.LENGTH_SHORT).show();
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(to_user_login_page);
            }
        });
        return view;
    }
}