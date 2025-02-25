package com.example.final1.MainPages.RecipePage;

import com.example.final1.Users.User;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String title;
    private String method;
    private String imageUrl;
    private String userName;
    private String ueserImg;
    private boolean isFavorite;
    private ArrayList<String> ingredients;

    public Recipe() {
    }

    public Recipe(String title, String method, String imageUrl, String userName,String  ueserImg, boolean isFavorite, ArrayList<String> ingredients) {
        this.title = title;
        this.method = method;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.isFavorite = isFavorite;
        this.ingredients = ingredients;
        this. ueserImg=  ueserImg;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setUeserImg(String ueserImg) {
        this.ueserImg = ueserImg;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getUeserImg() {
        return ueserImg;
    }

    public String getTitle() {
        return title;
    }

    public String getMethod() {
        return method;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", method='" + method + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", ueserImg='" + ueserImg + '\'' +
                ", isFavorite=" + isFavorite +
                ", ingredients=" + ingredients +
                '}';
    }


}

