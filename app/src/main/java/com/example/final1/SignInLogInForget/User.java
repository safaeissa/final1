package com.example.final1.SignInLogInForget;

import android.media.Image;
import android.widget.ImageView;

public class User {

   private ImageView photo;
   private String Name ;
    private double Weight ;
  private  double  Length ;
  private int Age ;
public User()
{

}
    public User(ImageView photo, String name, double weight, double length, int age) {
        this.photo = photo;
       this.Name = name;
        this.Weight = weight;
        this.Length = length;
        this.Age = age;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public double getLength() {
        return Length;
    }

    public void setLength(double length) {
        Length = length;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "photo='" + photo + '\'' +
                ", Name='" + Name + '\'' +
                ", Weight=" + Weight +
                ", Length=" + Length +
                ", Age=" + Age +
                '}';
    }
}
