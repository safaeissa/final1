package com.example.final1.Users;

import static android.app.ProgressDialog.show;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.HomeFragment;
import com.example.final1.MainPages.RecipePage.Recipe;
import com.example.final1.R;
import com.example.final1.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class AddDataFragment extends Fragment {
    private EditText age, Weight ,height ;
    private FirebaseServices fbs ;
    private Button Start;
    private Uri selectedImageUri;
    private ImageView img;
private ArrayList<String> Recipes=new ArrayList<>();

    private ActivityResultLauncher<String> imagePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_data, container, false);

        Recipes = new ArrayList<>();
        fbs = FirebaseServices.getInstance();

        img = view.findViewById(R.id.imageViewProfile);
        Weight = view.findViewById(R.id.etWeight);
        height = view.findViewById(R.id.etHeight);
        age = view.findViewById(R.id.etAge);
        Start = view.findViewById(R.id.btnStart);

        String email = fbs.getAuth().getCurrentUser().getEmail();

        img.setOnClickListener(v -> openGallery());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        img.setImageURI(uri);
                        Utils.getInstance().uploadImage(getContext(), selectedImageUri);
                    } else {
                        Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Start.setOnClickListener(v -> {
            String name1 = getNameFromEmail(email);
            String weight1 = Weight.getText().toString();
            String height1 = height.getText().toString();
            String age1 = age.getText().toString();

            if (name1.isEmpty() || weight1.isEmpty() || height1.isEmpty() || age1.isEmpty()) {
                Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri imageUrl = fbs.getSelectedImageURL();
            if (imageUrl == null) {
                Toast.makeText(getContext(), "Please select an image!", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(imageUrl.toString(), name1, weight1, height1, age1, email, Recipes);
            fbs.getFire().collection("Users").add(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.main, new HomeFragment())
                                .commit();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to save data", Toast.LENGTH_SHORT).show();
                    });
        });
        return view;
    }

    public static String getNameFromEmail(String email) {
        if (email == null || !email.contains("@"))
            return "Invalid email";
        String namePart = email.split("@")[0];
        return namePart;
    }

    private void openGallery() {
        imagePickerLauncher.launch("image/*");
    }
}


