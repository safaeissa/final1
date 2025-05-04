package com.example.final1.MainPages.RecipePage;

import com.example.final1.Users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private String title;
    private String method;
    private String imageUrl;
    private String userName;
    private boolean isFavorite;
    private String idRecipe;

    public Recipe() {
    }

    public Recipe(String title, String method, String imageUrl, String userName, boolean isFavorite, String idRecipe) {
        this.title = title;
        this.method = method;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.isFavorite = isFavorite;
        this.idRecipe = idRecipe;

    }

    public String getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(String idRecipe) {
        this.idRecipe = idRecipe;
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
                ", isFavorite=" + isFavorite +
                ", idRecipe='" + idRecipe + '\'' +
                '}';
    }
}

