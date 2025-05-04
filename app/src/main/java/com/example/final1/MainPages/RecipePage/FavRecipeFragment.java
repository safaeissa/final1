package com.example.final1.MainPages.RecipePage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.HomeFragment;
import com.example.final1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavRecipeFragment extends Fragment {
    private RecyclerView recyclerViewFavorites;
    private ImageButton imageButton;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> favoriteRecipes;
    private ArrayList<Recipe> recipesList;
    private FirebaseServices firebaseServices;
    private TextView ifhava;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavRecipeFragment newInstance(String param1, String param2) {
        FavRecipeFragment fragment = new FavRecipeFragment();
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
        return inflater.inflate(R.layout.fragment_fav_recipe, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        conecct();
    }
    public void conecct () {
        firebaseServices = new FirebaseServices();
        recyclerViewFavorites = getView().findViewById(R.id.recyclerViewFavorites);
        favoriteRecipes = new ArrayList<>();
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipesList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getActivity(),favoriteRecipes);
        recyclerViewFavorites.setAdapter(recipeAdapter);
        ifhava = getView().findViewById(R.id.isempty);
        firebaseServices.getFavoriteRecipes(new OnSuccessListener<ArrayList<Recipe>>() {
            @Override
            public void onSuccess(ArrayList<Recipe> recipes) {
                favoriteRecipes.clear();
                favoriteRecipes.addAll(recipes);
                recipeAdapter.notifyDataSetChanged();
                if (recipes.isEmpty()) {
                    ifhava.setText("No recipe in favorite");
                } else {
                    ifhava.setText("");
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ifhava.setText("Error loading favorites: " + e.getMessage());
            }
        });

        imageButton = getView().findViewById(R.id.ImgFavBack);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new RecipeListFragment());
                transaction.commit();
            }
        });

    }

}