package com.example.final1.Users;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.squareup.picasso.Picasso;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
      Context context;
   ArrayList<User> userList;
    private OnItemClickListener itemClickListener;


    public UserAdapter( Context context ,ArrayList<User> userList) {
      this.context = context;
        this.userList = userList;
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
       if (user.getPhoto() == null || user.getPhoto().isEmpty())
          holder.profileImageView.setImageURI(Uri.parse(user.getPhoto()));
       else Picasso.get().load(user.getPhoto()).into(holder.profileImageView);
        holder.nameTextView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position);
            }
        });


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
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}

