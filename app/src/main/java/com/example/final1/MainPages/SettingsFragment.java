package com.example.final1.MainPages;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.example.final1.Users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private TextView name,age, Weight ,height ;
    private ImageView imgpr;
    private FirebaseServices fbs ;
    private ImageButton btnOut , btnUpDate,btnDelet;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        coneect();
    }
    public void coneect() {
        name = getView().findViewById(R.id.etNamePr);
        age = getView().findViewById(R.id.etAgePr);
        Weight = getView().findViewById(R.id.weightPr);
        height = getView().findViewById(R.id.hightPr);
        imgpr = getView().findViewById(R.id.imgPr);
        fbs = new FirebaseServices();
        String email = "";
        FirebaseUser user = fbs.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            String name1 = getNameFromEmail(email);
            name.setText(name1);
            fbs.getUserDataByEmail(email, new OnSuccessListener<QueryDocumentSnapshot>() {
                @Override
                public void onSuccess(QueryDocumentSnapshot queryDocumentSnapshot) {
                    String age1 = queryDocumentSnapshot.getString("age");
                    String weight1 = queryDocumentSnapshot.getString("weight");
                    String length = queryDocumentSnapshot.getString("length");
                    age.setText("age1");
                    Weight.setText("weight1");
                    height.setText("length");
                }
            }, onFailureListener -> {
                Toast.makeText(getContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            });
        }

    }

    public static String getNameFromEmail(String email) {
        if (email == null || !email.contains("@"))
            return "Invalid email";
        String namePart = email.split("@")[0];
        return namePart;
    }


    }
