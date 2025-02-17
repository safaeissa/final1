package com.example.final1.Users;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final1.FirebaseServices;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    ArrayList<User>user ;
    private FirebaseServices fbs;
    public UserAdapter(Context context, ArrayList<User> user) {
        this.context = context;
        this.user = user;
    }
}
