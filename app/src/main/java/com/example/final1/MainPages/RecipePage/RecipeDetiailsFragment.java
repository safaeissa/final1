package com.example.final1.MainPages.RecipePage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.final1.FirebaseServices;
import com.example.final1.MainPages.HomeFragment;
import com.example.final1.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetiailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetiailsFragment extends Fragment {
private FirebaseServices fbs;
private     Recipe recipe;
private ImageView imageView3;
private ImageButton imgb;
private TextView DetiailsNsmeRecipe,DatiailsMethod, DetilsUserName;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeDetiailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetiailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetiailsFragment newInstance(String param1, String param2) {
        RecipeDetiailsFragment fragment = new RecipeDetiailsFragment();
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
        return inflater.inflate(R.layout.fragment_recipe_detiails, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        coneect();

    }
    public void coneect() {
        imageView3 = getView().findViewById(R.id.imageView3);
        DetiailsNsmeRecipe = getView().findViewById(R.id.DetiailsNsmeRecipe);
        DatiailsMethod = getView().findViewById(R.id.DatiailsMethod);
        DetilsUserName = getView().findViewById(R.id.DetilsUserName);
        imgb=getView().findViewById(R.id.imageButtonDetiails);
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main ,new RecipeListFragment());
                transaction.commit();
            }
        });
        Bundle args = getArguments();
        if (args != null) {
            recipe = args.getParcelable("recipe");
            if (recipe != null) {
                DetiailsNsmeRecipe.setText(recipe.getTitle());
                DatiailsMethod.setText(recipe.getMethod());
                if (recipe.getImageUrl() == null || recipe.getImageUrl().isEmpty())
                    Picasso.get().load(R.drawable.food).into(imageView3);
                else
                    Picasso.get().load(recipe.getImageUrl()).into(imageView3);
            }
        }
    }

}