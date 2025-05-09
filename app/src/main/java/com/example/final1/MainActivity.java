package com.example.final1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.final1.MainPages.HomeFragment;
import com.example.final1.MainPages.RecipePage.AddRecipeFragment;
import com.example.final1.MainPages.RecipePage.RecipeListFragment;
import com.example.final1.MainPages.SportFragment;
import com.example.final1.SignInLogInForget.LogInFragment;
import com.example.final1.SignInLogInForget.SignUPFragment;
import com.example.final1.Users.AddDataFragment;
import com.example.final1.Users.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main, new SignUPFragment());
        ft.commit();
    }
}