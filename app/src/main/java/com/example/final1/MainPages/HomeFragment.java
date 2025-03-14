package com.example.final1.MainPages;

import android.net.Uri;
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
import com.example.final1.MainPages.RecipePage.RecipeListFragment;
import com.example.final1.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ImageButton btnF,btnA,btnS,btnH;
    private TextView textuser;
    private ImageView imguser;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connect();

    }
    public void connect () {

        textuser = getView().findViewById(R.id.textNameUser);
        textuser.setText(FirebaseServices.getInstance().getCurrentUser().getName());
        btnH = getView().findViewById(R.id.GoToHealth);
        imguser = getView().findViewById(R.id.imageView2);
        imguser.setImageURI(Uri.parse(FirebaseServices.getInstance().getCurrentUser().getPhoto()));
        if (FirebaseServices.getInstance().getCurrentUser().getPhoto() == null || FirebaseServices.getInstance().getCurrentUser().getPhoto().isEmpty())
            imguser.setImageResource(R.drawable.blank_profile_picture_973460_1280);
        else Picasso.get().load(FirebaseServices.getInstance().getCurrentUser().getPhoto()).into(imguser);
        btnF = getView().findViewById(R.id.GotoFood);
        btnA = getView().findViewById(R.id.goToAi);
        btnS = getView().findViewById(R.id.GotoSport);
        btnH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new HealthFragment());
                transaction.commit();
            }
        });
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new RecipeListFragment());
                transaction.commit();
            }
        });
        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new AIFragment());
                transaction.commit();
            }
        });
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new SportFragment());
                transaction.commit();
            }
        });
    }

    }




