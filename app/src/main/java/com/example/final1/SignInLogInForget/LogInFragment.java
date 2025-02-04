package com.example.final1.SignInLogInForget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.HomeFragment;
import com.example.final1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {
    private EditText etEmail, etPassword;
    private TextView textView4;
    private TextView btnlogin;
    private FirebaseServices fbs;
    private TextView signup;
    private TextView forget;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }
    @Override
    public void onStart()
    {
        super.onStart();
        connect();
    }
    public void connect(){
        //connecting components
        fbs=FirebaseServices.getInstance();
        etEmail=getView().findViewById(R.id.EtEmailLogin);
        etPassword=getView().findViewById(R.id.etPasswordLogin);
        btnlogin=getView().findViewById(R.id.btnLogin);
        signup=getView().findViewById(R.id.etGotoLoginfromPass);
        forget=getView().findViewById(R.id.etGoToSigUpFromPass);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main ,new SignUPFragment());
                transaction.commit();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main ,new ForgetPasswordFragment());
                transaction.commit();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Data validation
                String Email=etEmail.getText().toString();
                String password=etPassword.getText().toString();
                if (Email.isEmpty()&&password.isEmpty())
                {
                    Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();

                }
                //Login procedure
                fbs.getAuth().signInWithEmailAndPassword(Email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(), "Successfully Login !", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.main ,new HomeFragment());
                        transaction.commit();
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }
}
