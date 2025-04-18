package com.example.final1.MainPages;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.example.final1.Users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private EditText name,age, Weight ,height ;
    private ImageView imgpr;
    private FirebaseServices fbs ;
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
    public void coneect ()
    {
        name=getView().findViewById(R.id.etNamePr);
        age=getView().findViewById(R.id.etAge);
        Weight=getView().findViewById(R.id.weightPr);
        height=getView().findViewById(R.id.hightPr);
        imgpr=getView().findViewById(R.id.imgPr);
        fbs=new FirebaseServices();
        FirebaseUser user =fbs.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name1 = getNameFromEmail(email);
            name.setText(name1);
            fbs.getUserDataByEmail(email, new OnSuccessListener<QueryDocumentSnapshot>() {
                @Override
                public void onSuccess(QueryDocumentSnapshot queryDocumentSnapshot) {
                    String photo = queryDocumentSnapshot.getString("photo");
                    if (photo == null || photo.isEmpty())
                        imgpr.setImageResource(R.drawable.blank_profile_picture_973460_1280);
                    else Picasso.get().load(photo).into(imgpr);
                    age.setText(queryDocumentSnapshot.getString("age"));
                    Weight.setText(queryDocumentSnapshot.getString("weight"));
                    height.setText(queryDocumentSnapshot.getString("length"));
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        };


    }
    public static String getNameFromEmail(String email) {
        if (email == null || !email.contains("@"))
            return "Invalid email";
        String namePart = email.split("@")[0];
        return namePart;
    }


    }
