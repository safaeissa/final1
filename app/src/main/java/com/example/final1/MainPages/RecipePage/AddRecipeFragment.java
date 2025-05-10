package com.example.final1.MainPages.RecipePage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipeFragment extends Fragment {
    private static final int GALLARY_REQUEST_CODE = 123;
    private EditText   recipetitle ,recipemethode ;
    private FirebaseServices fbs ;
    private TextView addrecipe,userName;
    private ImageView img;
    private ImageButton b;

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
     * @return A new instance of fragment AddRecipe.
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
        return inflater.inflate(R.layout.fragment_add_recipe, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        conect();
    }
    public void conect() {

        fbs = new FirebaseServices().getInstance();
        img = getView().findViewById(R.id.imageView);
        recipetitle = getView().findViewById(R.id.recipetitle);
        addrecipe = getView().findViewById(R.id.addRecipe);
        recipemethode = getView().findViewById(R.id.editTextTextMultiLine);
        String email = fbs.getAuth().getCurrentUser().getEmail();
        userName = getView().findViewById(R.id.UserNameP);
        b=getView().findViewById(R.id.Back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new RecipeListFragment());
                transaction.commit();
            }
        });
        userName.setText(getNameFromEmail(email));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallerIntent, GALLARY_REQUEST_CODE);
            }
        });addrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipetitle1 = recipetitle.getText().toString();
                String recipemethode1 = recipemethode.getText().toString();
               String email = fbs.getAuth().getCurrentUser().getEmail();
                String name = getNameFromEmail(email);
                if (recipetitle1.isEmpty() || recipemethode1.isEmpty()) {
                    Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri selectedImageUri=fbs.getSelectedImageURL();
                String imageUri ="";
                if(selectedImageUri!=null)
                    imageUri=selectedImageUri.toString();
                String recipeId = fbs.getFire().collection("Recipes").document().getId();
                Recipe recipe = new Recipe(recipetitle1, recipemethode1, imageUri, name, recipeId);
                fbs.getFire().collection("Recipes").add(recipe).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "thank you", Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.main ,new RecipeListFragment());
                        transaction.commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), " faild", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==  GALLARY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
            Utils.getInstance().uploadImage(getActivity(), selectedImageUri);
        }
    }
    public static String getNameFromEmail(String email) {
        if (email == null || !email.contains("@"))
            return "Invalid email";
        String namePart = email.split("@")[0];
        return namePart;
    }
}