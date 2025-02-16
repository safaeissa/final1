package com.example.final1.MainPages.RecipePage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipeFragment extends Fragment {
    private EditText titleEditText, descriptionEditText, ingredientsEditText;
    private ImageView recipeImageView;
    private Button addRecipeButton, chooseImageButton;
    private Uri imageUri;
    private FirebaseServices firebaseServices;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecipeFragment newInstance(String param1, String param2) {
        AddRecipeFragment fragment = new AddRecipeFragment();
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

        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        ingredientsEditText = view.findViewById(R.id.ingredientsEditText);
        recipeImageView = view.findViewById(R.id.recipeImageView);
        addRecipeButton = view.findViewById(R.id.addRecipeButton);
        chooseImageButton = view.findViewById(R.id.chooseImageButton);

        firebaseServices = new FirebaseServices();

        chooseImageButton.setOnClickListener(v -> openImagePicker());
        addRecipeButton.setOnClickListener(v -> uploadRecipe());

        return view;
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            recipeImageView.setImageURI(imageUri);
        }
    }

    private void uploadRecipe() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String ingredients = ingredientsEditText.getText().toString();

        if (title.isEmpty() || description.isEmpty() || ingredients.isEmpty() ) {
            Toast.makeText(getContext(), "empty ", Toast.LENGTH_SHORT).show();

            return;
        }

        firebaseServices.uploadRecipe(title, description, ingredients, imageUri, new FirebaseServices.UploadCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "the recipe adds", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "faild  " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}