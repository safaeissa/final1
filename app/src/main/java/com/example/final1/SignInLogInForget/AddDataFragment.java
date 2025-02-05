package com.example.final1.SignInLogInForget;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDataFragment extends Fragment {
    private ImageView photo;
    private TextView Name;
    private TextView Weight;
    private TextView height;
    private TextView Age;
    private FirebaseServices fbs;
    private TextView Start;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDataFragment newInstance(String param1, String param2) {
        AddDataFragment fragment = new AddDataFragment();
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
        return inflater.inflate(R.layout.fragment_add_data, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
         conect();
    }
    public void conect ()
    {
        photo=getView().findViewById(R.id.imageView2);
        fbs=FirebaseServices.getInstance();
        Name=getView().findViewById(R.id.textName);
        Weight=getView().findViewById(R.id.etWeight);
        height=getView().findViewById(R.id.etHeight);
        Age=getView().findViewById(R.id.etage);
        Start=getView().findViewById(R.id.btnStart);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser();
            }
        });
    }
    public void AddUser()
    {
        String name1, weight1, height1, age1;
        ImageView photo11 ;
        name1 = Name.getText().toString();
        weight1 = Weight.getText().toString();
        height1 = height.getText().toString();
        age1 = Age.getText().toString();
        photo11=photo;
        if (name1.isEmpty() || weight1.isEmpty() || height1.isEmpty() || age1.isEmpty()) {
            Toast.makeText(getActivity(), " something is wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(photo11,name1,weight1,height1,age1 );
        fbs.getFire().collection("user").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Welcome!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }







}