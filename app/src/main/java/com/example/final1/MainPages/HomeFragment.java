package com.example.final1.MainPages;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.RecipePage.RecipeListFragment;
import com.example.final1.R;
import com.example.final1.Users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView textuser;
    private ImageView imguser,RecipeBtn,HealthBtn,SportBtn,SettingBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connect();
    }
    public void connect () {
        RecipeBtn = getView().findViewById(R.id.RecipeBtn);
        SettingBtn = getView().findViewById(R.id.SettingBtn);
        HealthBtn = getView().findViewById(R.id.HealthBtn);
        SportBtn = getView().findViewById(R.id.SportBtn);
        RecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new RecipeListFragment());
                transaction.commit();
            }
        });
        SettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new SettingsFragment());
                transaction.commit();
            }
        });
        HealthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new HealthFragment());
                transaction.commit();
            }
        });
        SportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new SportFragment());
                transaction.commit();
            }
        });
        textuser = getView().findViewById(R.id.textNameUser);
        FirebaseServices fbs = new FirebaseServices().getInstance();
        imguser = getView().findViewById(R.id.imageView2);
        FirebaseUser user = fbs.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name = getNameFromEmail(email);
            textuser.setText(name);
            fbs.getUserDataByEmail(email, new OnSuccessListener<QueryDocumentSnapshot>() {
                @Override
                public void onSuccess(QueryDocumentSnapshot queryDocumentSnapshot) {
                    String photo = queryDocumentSnapshot.getString("photo");
                    if (photo == null || photo.isEmpty())
                        imguser.setImageResource(R.drawable.blank_profile_picture_973460_1280);
                    else Picasso.get().load(photo).into(imguser);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
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




