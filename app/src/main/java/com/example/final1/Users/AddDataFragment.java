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
    private static final int GALLARY_REQUEST_CODE = 1;
    private EditText name,age, Weight ,height ;
    private FirebaseServices fbs ;
    private Button Start;
    private ImageView img;
private ArrayList<String> Recipes=new ArrayList<>();
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
    public void conect() {
        Recipes=new ArrayList<String>();
        fbs=new FirebaseServices().getInstance();
        img = getView().findViewById(R.id.imageViewProfile);
        Weight = getView().findViewById(R.id.etWeight);
        height = getView().findViewById(R.id.etHeight);
        age = getView().findViewById(R.id.etAge);
        String email = fbs.getAuth().getCurrentUser().getEmail();
        Start = getView().findViewById(R.id.btnStart);
        img.setOnClickListener(v -> openGallery());
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = getNameFromEmail(email);
                String weight1 = Weight.getText().toString();
                String height1 = height.getText().toString();
                String age1 = age.getText().toString();
                if (name1.isEmpty() || weight1.isEmpty() || height1.isEmpty() || age1.isEmpty())
                {
                    Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
               Uri selectedImageUri=img.getTag() instanceof Uri ? (Uri) img.getTag() : null;
                String imageUri = "";
                if(selectedImageUri!=null)
                    imageUri=selectedImageUri.toString();
                User user = new User(imageUri,name1, weight1, height1, age1, fbs.getAuth().getCurrentUser().getEmail(),Recipes);
                fbs.getFire().collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "welcome", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.main ,new HomeFragment());
                        transaction.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "faild ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public static String getNameFromEmail(String email) {
        if (email == null || !email.contains("@"))
            return "Invalid email";
        String namePart = email.split("@")[0];
        return namePart;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLARY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }
}


