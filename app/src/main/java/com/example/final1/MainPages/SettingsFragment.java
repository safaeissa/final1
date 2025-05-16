package com.example.final1.MainPages;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import com.example.final1.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class SettingsFragment extends Fragment {

    private EditText  age, Weight, height;
    private Uri selectedImageUri;
    private TextView name;
    private ImageView  imgprofile;
    private FirebaseServices fbs;
    private Button btnOut, btnUpDate, btnDelet;
    private ImageButton imgba;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        name = view.findViewById(R.id.etNamePr);
        age = view.findViewById(R.id.etAgePr);
        Weight = view.findViewById(R.id.weightPr);
        height = view.findViewById(R.id.hightPr);
        imgprofile = view.findViewById(R.id.imgPr);
        btnUpDate = view.findViewById(R.id.btnUpdate);
        btnOut = view.findViewById(R.id.btnLogout);
        btnDelet = view.findViewById(R.id.DeleteAccount);
        imgba = view.findViewById(R.id.imgbutnSitting);
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
        imgprofile.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {openGallery();}});
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            selectedImageUri = uri;
                            imgprofile.setImageURI(uri);
                            Utils.getInstance().uploadImage(getContext(), selectedImageUri);
                        } else {
                            Toast.makeText(getContext(), "not done", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        btnUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email2 = user.getEmail();
                String age2 = extractNumbersFromText(age.getText().toString());
                String weight2 = extractNumbersFromText(Weight.getText().toString());
                String length3 = extractNumbersFromText(height.getText().toString());
                fbs.updateUserByEmail(email2, selectedImageUri.toString(), age2, weight2, length3);
                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        btnDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletac();
            }
        });
        return view;
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
    private void openGallery() {
        imagePickerLauncher.launch("image/*");
    }
}