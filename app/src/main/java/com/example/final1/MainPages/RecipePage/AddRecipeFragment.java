package com.example.final1.MainPages.RecipePage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.HomeFragment;
import com.example.final1.R;
import com.example.final1.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

public class AddRecipeFragment extends Fragment {
    private EditText   recipetitle ,recipemethode ;
    private FirebaseServices fbs ;
    private TextView addrecipe,userName;
    private ImageButton b;
    private ImageView imageRecipe;
    private Uri selectedImageUri;

    private ActivityResultLauncher<String> imagePickerLauncher;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_recipe, container, false);

        fbs = new FirebaseServices().getInstance();
        recipetitle = view.findViewById(R.id.recipetitle);
        addrecipe = view.findViewById(R.id.addRecipe);
        recipemethode = view.findViewById(R.id.editTextTextMultiLine);
        String email = fbs.getAuth().getCurrentUser().getEmail();
        userName = view.findViewById(R.id.UserNameP);
        b=view.findViewById(R.id.Back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new RecipeListFragment());
                transaction.commit();
            }
        });
        userName.setText(getNameFromEmail(email));
        imageRecipe = view.findViewById(R.id.imageRecipe);
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            selectedImageUri = uri;
                            imageRecipe.setImageURI(uri);
                            Utils.getInstance().uploadImage(getContext(), selectedImageUri);
                        } else {
                            Toast.makeText(getContext(), "not done", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        imageRecipe.setOnClickListener(v -> openGallery());

        addrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecipe();
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
    private void openGallery() {
        imagePickerLauncher.launch("image/*");
    }
    private void AddRecipe()
    {
        String recipetitle1 = recipetitle.getText().toString();
        String recipemethode1 = recipemethode.getText().toString();
        String email = fbs.getAuth().getCurrentUser().getEmail();
        String name = getNameFromEmail(email);
        if (recipetitle1.isEmpty() || recipemethode1.isEmpty()) {
            Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        String recipeId = fbs.getFire().collection("Recipes").document().getId();
            Uri imageUrl = fbs.getSelectedImageURL();
            Recipe recipe = new Recipe(recipetitle1, recipemethode1, imageUrl.toString(), name, recipeId);
            fbs.getFire().collection("Recipes").add(recipe).addOnSuccessListener(documentReference -> {
                Toast.makeText(getContext(), "Recipe added successfully", Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new RecipeListFragment());
                transaction.commit();
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to add", Toast.LENGTH_SHORT).show();
            });

    }

}