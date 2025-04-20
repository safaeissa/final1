package com.example.final1;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.final1.MainPages.RecipePage.Recipe;
import com.example.final1.Users.User;
import com.example.final1.Users.UserCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getCurrentObjectUser(new UserCallback() {
            @Override
            public void onUserLoaded(User user) {
                // Access the currentUser here
                if (user != null) {
                    setCurrentUser(user);
                }
            }
        });

        userChangeFlag = false;

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
            public void onCancelled(@NonNull DatabaseError error) {
            }
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


    public static FirebaseServices reloadInstance() {
        instance = new FirebaseServices();
        return instance;
    }

    public boolean isUserChangeFlag() {
        return userChangeFlag;
    }

    public void setUserChangeFlag(boolean userChangeFlag) {
        this.userChangeFlag = userChangeFlag;
    }

    public void getCurrentObjectUser(UserCallback callback) {
        ArrayList<User> usersInternal = new ArrayList<>();
        fire.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {
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


    public FirebaseUser getCurrentUser()
    {
       return auth.getCurrentUser();
    }

    public User getCurrentUserObject() {
        return currentUser;
    }

    public void getUserDataByEmail(String email,
                                   final OnSuccessListener<QueryDocumentSnapshot> onSuccessListener,
                                   final OnFailureListener onFailureListener) {
        fire.collection("Users")
                .whereEqualTo("email", email)  // البحث باستخدام البريد الإلكتروني
                .limit(1)  // تحديد نتيجة واحدة فقط
                .get();
    }



    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public void updateUserByEmail(String email, String imageUrl, String age, String weight, String length) {
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("imageUrl", imageUrl);
        updatedData.put("age", age);
        updatedData.put("weight", weight);
        updatedData.put("length", length);

        fire.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String docId = document.getId();
                            fire.collection("Users")
                                    .document(docId)
                                    .update(updatedData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("UpdateUser", "successfully updated");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("UpdateUser", "failed to update", e);
                                    });
                            break;
                        }
                    }
                });
    }

    public interface DeletAccountCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
    public void addRecipeToFavorites(String recipeId,
                                     OnSuccessListener<Void> onSuccess,
                                     OnFailureListener onFailure) {
        FirebaseUser user = getAuth().getCurrentUser();
        if (user == null) {
            onFailure.onFailure(new Exception("No user logged in"));
            return;
        }

        String email = user.getEmail();


        fire.collection("users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentReference userDoc = querySnapshot.getDocuments().get(0).getReference();
                        userDoc.update("recipes", FieldValue.arrayUnion(recipeId))
                                .addOnSuccessListener(onSuccess)
                                .addOnFailureListener(onFailure);
                    } else {
                        Log.d("TAG", "No user found with email: " + email);
                    }
                })
                .addOnFailureListener(onFailure);
    }

    public void deleteCurrentUserAccount(DeletAccountCallback callback) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            callback.onFailure(new Exception("No user logged in"));
            return;
        }
        String userEmail = currentUser.getEmail();
        fire.collection("Users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        // حذف من Firestore
                        fire.collection("Users").document(document.getId()).delete();
                    }
                    // 2. حذف من Storage (مثلاً صورة البروفايل)
                    StorageReference profileImageRef = storage.getReference().child("profile_images/" + currentUser.getUid());
                    profileImageRef.delete().addOnCompleteListener(task -> {
                        // 3. حذف من Authentication
                        currentUser.delete()
                                .addOnSuccessListener(unused -> callback.onSuccess())
                                .addOnFailureListener(callback::onFailure);
                    });
                })
                .addOnFailureListener(callback::onFailure);
    }

    }





