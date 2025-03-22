package com.example.final1.MainPages.RecipePage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.HealthFragment;
import com.example.final1.R;
import com.example.final1.Users.AddDataFragment;
import com.example.final1.Users.User;
import com.example.final1.Users.UserAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipesList;
    private FirebaseServices firebaseServices;
    private RecipeAdapter recipeAdapter;
    private ImageButton btmadd;

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
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
     coneect();
    }
    public void coneect () {
        recyclerView = getView().findViewById(R.id.RecyclerViewRecipe);
        firebaseServices = FirebaseServices.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recipesList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getActivity(), recipesList);
        recyclerView.setAdapter(recipeAdapter);
        btmadd=getView().findViewById(R.id.btnaddd);
        btmadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new AddRecipeFragment());
                transaction.commit();
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
        recipeAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedItem = recipesList.get(position).getTitle();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("Recipes", (Parcelable) recipesList.get(position)); // or use Parcelable for better performance
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = new AddRecipeFragment();
                fragment.setArguments(args);
                ft.replace(R.id.main, fragment);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        }

    }
