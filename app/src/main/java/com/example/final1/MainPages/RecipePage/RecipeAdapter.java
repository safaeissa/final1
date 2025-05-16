package com.example.final1.MainPages.RecipePage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends  RecyclerView.Adapter <RecipeAdapter.MyViewHolder> {
    Context context;
    ArrayList<Recipe> RecipeList;
    ArrayList<Recipe> fullList;
    private RecipeAdapter.OnItemClickListener itemClickListener;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipesList) {
        this.context = context;
        this.RecipeList =recipesList;
        this.fullList = recipesList;

    }


    @NonNull
    @Override
    public  MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View v= LayoutInflater.from(context).inflate(R.layout.item_recipe,parent,false);
        return  new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        Recipe recipe = RecipeList.get(position);

        holder.recipeTitle.setText(recipe.getTitle());
        holder.TextR.setText(recipe.getMethod());
        holder.img.setImageURI(Uri.parse(recipe.getImageUrl()));
        holder.recipeTitle.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position);
            }
        });
        if (recipe.getImageUrl()==null || recipe.getImageUrl().isEmpty())
            holder.img.setImageResource(R.drawable.food);
        else Picasso.get().load(recipe.getImageUrl()).into(holder.img);
        holder.itemView.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putSerializable("recipe",recipe);

            RecipeDetiailsFragment  detailsFragment = new RecipeDetiailsFragment();
            detailsFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.main,detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });


    }
    @Override
    public int getItemCount(){
        return RecipeList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle, TextR;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            TextR = itemView.findViewById(R.id.TextR);
            img = itemView.findViewById(R.id.recipeImage);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }





}



