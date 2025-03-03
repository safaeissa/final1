package com.example.final1;

import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.final1.MainPages.RecipePage.Recipe;
import com.example.final1.Users.User;
import com.example.final1.Users.UserCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseServices {
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private static FirebaseServices instance;
    private FirebaseAuth auth;
    private FirebaseFirestore fire;
    private FirebaseStorage storage;
    private Uri selectedImageURL;
    private User currentUser;
    private boolean userChangeFlag;


    public FirebaseServices() {
        storageReference = FirebaseStorage.getInstance().getReference("recipe_images");
        databaseReference = FirebaseDatabase.getInstance().getReference("recipes");
        auth = FirebaseAuth.getInstance();
        fire = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

    }
    public void getUsersList(final DataStatus dataStatus) {
        fire.collection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        users.add(user);
                    }
                    dataStatus.onSuccess(users);
                })
                .addOnFailureListener(e -> dataStatus.onFailure(e.getMessage()));
    }

    public interface DataStatus {
        void onSuccess(List<User> users);
        void onFailure(String error);
    }


    public void getRecipes(final RecipeCallback callback) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Recipe recipe = data.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                callback.onRecipesLoaded(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }






    public interface RecipeCallback {
        void onRecipesLoaded(List<Recipe> recipes);
    }
    public Uri getSelectedImageURL() {
        return selectedImageURL;
    }
    public void setSelectedImageURL(Uri selectedImageURL) {
        this.selectedImageURL = selectedImageURL;
    }

    public FirebaseFirestore getFire() {
        return fire;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public static FirebaseServices getInstance() {

        if (instance == null) {
            instance = new FirebaseServices();
        }
        return instance;
    }


    public static FirebaseServices reloadInstance(){
        instance=new FirebaseServices();
        return instance;
    }

   public boolean isUserChangeFlag() {
        return userChangeFlag;
    }

    public void setUserChangeFlag(boolean userChangeFlag) {
        this.userChangeFlag = userChangeFlag;
    }

    public void getCurrentObjectUser(UserCallback callback) {        ArrayList<User> usersInternal = new ArrayList<>();
        fire.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                    User user = dataSnapshot.toObject(User.class);
                    if (auth.getCurrentUser() != null && auth.getCurrentUser().getEmail().equals(user.getName())) {
                        usersInternal.add(user);

                    }
                }
                if (usersInternal.size() > 0)
                    currentUser = usersInternal.get(0);

                callback.onUserLoaded(currentUser);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public User getCurrentUser()
    {
        return this.currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public boolean updateUser(User user)
    {
        final boolean[] flag = {false};
        // Reference to the collection
        String collectionName = "users";
        String usernameFieldName = "username";
        String usernameValue = user.getName();
        // Create a query for documents based on a specific field
        Query query = fire.collection(collectionName).
                whereEqualTo(usernameFieldName, usernameValue);

        // Execute the query
        query.get()
                .addOnSuccessListener((QuerySnapshot querySnapshot) -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Get a reference to the document
                        DocumentReference documentRef = document.getReference();

                        // Update specific fields of the document
                        documentRef.update(

                                        usernameFieldName, usernameValue

                                )
                                .addOnSuccessListener(aVoid -> {

                                    flag[0] = true;
                                })
                                .addOnFailureListener(e -> {
                                    System.err.println("Error updating document: " + e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error getting documents: " + e);
                });

        return flag[0];
    }


}

