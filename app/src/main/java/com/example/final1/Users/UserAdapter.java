package com.example.final1.Users;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final1.FirebaseServices;
import com.example.final1.R;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
      Context context;
   ArrayList<User> userList;
    private FirebaseServices firebaseServices;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
        this.firebaseServices = new FirebaseServices().getInstance();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.useritem, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
User user = userList.get(position);
holder.nameTextView.setText(user.getName());
holder.ageTextView.setText(user.getAge());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView,ageTextView;
        ImageView profileImageView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            ageTextView = itemView.findViewById(R.id.AgeTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }
    }
}

