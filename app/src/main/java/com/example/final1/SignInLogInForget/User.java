package com.example.final1.SignInLogInForget;

import android.media.Image;
import android.widget.ImageView;

public class User {

   private ImageView photo;
   private String Name ;
    private String Weight ;
  private  String  Length ;
  private String Age ;
public User()
{

}
    public User(ImageView photo, String name, String weight, String length, String age) {
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
