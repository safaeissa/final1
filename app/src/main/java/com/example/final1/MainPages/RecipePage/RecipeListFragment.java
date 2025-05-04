package com.example.final1.MainPages.RecipePage;

import static android.system.Os.connect;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.HealthFragment;
import com.example.final1.MainPages.HomeFragment;
import com.example.final1.R;
import com.example.final1.Users.AddDataFragment;
import com.example.final1.Users.User;
import com.example.final1.Users.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeListFragment extends Fragment {
    Context context;
    private ImageButton b;
    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipesList, filteredList;
    private FirebaseServices firebaseServices;
    private RecipeAdapter recipeAdapter;
    private ImageButton btmadd,btnShowFavorites;
private EditText recipeSearch;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeListFragment newInstance(String param1, String param2) {
        RecipeListFragment fragment = new RecipeListFragment();
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
        View view= inflater.inflate(R.layout.fragment_recipe_list, container, false);

            return view;
        }




    @Override
    public void onStart() {
        super.onStart();
        coneect();
    }

    public void coneect () {
        recyclerView = getView().findViewById(R.id.RecyclerViewRecipe);
        b=getView().findViewById(R.id.BackList);
        recipeSearch=getView().findViewById(R.id.RcipeSearch);
        firebaseServices = FirebaseServices.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipesList = new ArrayList<>();
        filteredList = new ArrayList<>();
        btnShowFavorites=getView().findViewById(R.id.btnShowFavorites);
        btnShowFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new FavRecipeFragment());
                transaction.commit();
            }

        });
        recipeAdapter = new RecipeAdapter(getActivity(), recipesList);
        btmadd=getView().findViewById(R.id.btnAddRecipe);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new HomeFragment());
                transaction.commit();
            }
        });
        btmadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new AddRecipeFragment());
                transaction.commit();
            }
        });
        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedItem = recipesList.get(position).getTitle();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("Recipes", (Parcelable) recipesList.get(position)); // or use Parcelable for better performance
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new RecipeDetiailsFragment();
                fragment.setArguments(args);
                ft.replace(R.id.main, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        firebaseServices.getFire().collection("Recipes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Recipe recipe = snapshot.toObject(Recipe.class);
                    recipesList.add(recipe);
                }
                recipeAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "erorr " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + e.getMessage());
            }
        });
        recyclerView.setAdapter(recipeAdapter);

        recipeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }
    private void filterRecipes(String text ) {
        if (text.trim().isEmpty()) {
            recipeAdapter=new RecipeAdapter(getContext(),recipesList);
            recyclerView.setAdapter(recipeAdapter);
         return;
        }
        filteredList.clear();
            for (Recipe recipe : recipesList) {
                if (recipe.getTitle().toLowerCase().contains(text.toLowerCase())||recipe.getUserName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(recipe);
                }
            }
            if (filteredList.size()==0) {
                showNoDataDialogue();
                return;
            }
       recipeAdapter=new RecipeAdapter(getContext(),filteredList);
            recyclerView.setAdapter(recipeAdapter);
        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedItem = filteredList.get(position).getTitle();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("Recipes", (Parcelable) filteredList.get(position)); // or use Parcelable for better performance
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new RecipeDetiailsFragment();
                fragment.setArguments(args);
                ft.replace(R.id.main, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        }



    private void showNoDataDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Results");
        builder.setMessage("Try again!");
        builder.show();
    }


    }
