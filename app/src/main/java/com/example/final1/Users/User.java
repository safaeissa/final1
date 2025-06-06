package com.example.final1.Users;

import android.net.Uri;
import android.widget.ImageView;

import com.example.final1.MainPages.RecipePage.Recipe;

import java.util.ArrayList;

public class User {
   private String photo;
   private String Name ;
    private String Weight ;
  private  String  Length ;
  private String email;
  private String Age ;
  private ArrayList<String> recipes = new ArrayList<String>();


    public User()
{

}
    public User(String photo, String name, String weight, String length, String age, String email, ArrayList<String> recipes) {
        this.photo = photo;
       this.Name = name;
        this.Weight = weight;
        this.Length = length;
        this.Age = age;
        this.email = email;
        this.recipes = recipes;

    }

    @Override
    public String toString() {
        return "User{" +
                "photo='" + photo + '\'' +
                ", Name='" + Name + '\'' +
                ", Weight='" + Weight + '\'' +
                ", Length='" + Length + '\'' +
                ", email='" + email + '\'' +
                ", Age='" + Age + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }
    public ArrayList<String> getRecipes() {
        return recipes;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

}
