package com.example.final1.MainPages;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.example.final1.SignInLogInForget.SignUPFragment;


import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.example.final1.SignInLogInForget.LogInFragment;
import com.example.final1.SignInLogInForget.SignUPFragment;
import com.example.final1.Users.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private EditText  age, Weight, height;
    private TextView name;
    private ImageView  imgprofile;
    private FirebaseServices fbs;
    private Uri imageUri;
    private Button btnOut, btnUpDate, btnDelet;
    private ImageButton imgba;
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
        imgprofile = getView().findViewById(R.id.imgPr);
        btnUpDate = getView().findViewById(R.id.btnUpdate);
        btnOut = getView().findViewById(R.id.btnLogout);
        btnDelet = getView().findViewById(R.id.DeleteAccount);
        btnOut=getView().findViewById(R.id.btnLogout);
        imgba=getView().findViewById(R.id.imgbutnSitting);
        imgba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new HomeFragment());
                transaction.commit();
            }
        });
        fbs = new FirebaseServices();
        String email = "";
        FirebaseUser user = fbs.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            String name1 = getNameFromEmail(email);
            name.setText(name1);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String age1 = document.getString("age").toString();
                                String weight1 = document.getString("weight").toString();
                                String length1 = document.getString("length").toString();
                                if (age1 != "" && weight1 != "" && length1 != "")
                                    age.setText("age= " + age1.toString());
                                Weight.setText("weight(kg)= " + weight1.toString());
                                height.setText("height(cm)= " + length1.toString());
                                String imageUrl = document.getString("photo");
                                if (imageUrl != null && !imageUrl.isEmpty())
                                    Picasso.get().load(imageUrl).into(imgprofile);
                                else
                                    imgprofile.setImageResource(R.drawable.blank_profile_picture_973460_1280);
                            }
                        }
                    });

        }
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new LogInFragment());
                transaction.commit();
            }
        });
        btnUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email2 = user.getEmail();
                String age2 = extractNumbersFromText(age.getText().toString());
                String weight2 = extractNumbersFromText(Weight.getText().toString());
                String length3 = extractNumbersFromText(height.getText().toString());
                String imageUrl = imgprofile.toString();
                fbs.updateUserByEmail(email2, imageUrl, age2, weight2, length3);
                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        btnDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletac();
            }
        });

    }

    public static String getNameFromEmail(String email) {
        if (email == null || !email.contains("@"))
            return "Invalid email";
        String namePart = email.split("@")[0];
        return namePart;
    }

    public String extractNumbersFromText(String text) {
        return text.replaceAll("[^0-9]", "");
    }

    public void deletac() {
        fbs.deleteCurrentUserAccount(new FirebaseServices.DeletAccountCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new SignUPFragment());
                transaction.commit();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Failed to delete account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}