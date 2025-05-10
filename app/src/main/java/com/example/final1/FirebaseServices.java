package com.example.final1;

import static java.security.AccessController.getContext;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.final1.MainPages.RecipePage.Recipe;
import com.example.final1.Users.User;
import com.example.final1.Users.UserCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

    public void isRecipeInFavorites(String recipeId,
                                    OnSuccessListener<Boolean> onSuccess,
                                    OnFailureListener onFailure) {
        FirebaseUser user = getAuth().getCurrentUser();
        if (user == null) {
            onFailure.onFailure(new Exception("No user logged in"));
            return;
        }

        String email = user.getEmail();

        fire.collection("Users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);
                        List<String> recipes = (List<String>) userDoc.get("recipes");

                        boolean isInFavorites = recipes != null && recipes.contains(recipeId);
                        onSuccess.onSuccess(isInFavorites);
                    } else {
                        onSuccess.onSuccess(false); // لم يتم العثور على المستخدم
                    }
                })
                .addOnFailureListener(onFailure);
    }
    public void toggleRecipeInFavorites(String recipeId,
                                        OnSuccessListener<Boolean> onSuccess,
                                        OnFailureListener onFailure) {
        FirebaseUser user = getAuth().getCurrentUser();
        if (user == null) {
            onFailure.onFailure(new Exception("No user logged in"));
            return;
        }

        String email = user.getEmail();

        fire.collection("Users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot userDocSnapshot = querySnapshot.getDocuments().get(0);
                        DocumentReference userDoc = userDocSnapshot.getReference();
                        List<String> recipes = (List<String>) userDocSnapshot.get("recipes");

                        boolean isFavorite = recipes != null && recipes.contains(recipeId);

                        if (isFavorite) {
                            // إزالة من المفضلة
                            userDoc.update("recipes", FieldValue.arrayRemove(recipeId))
                                    .addOnSuccessListener(unused -> onSuccess.onSuccess(false)) // false = تم الإزالة
                                    .addOnFailureListener(onFailure);
                        } else {
                            // إضافة إلى المفضلة
                            userDoc.update("recipes", FieldValue.arrayUnion(recipeId))
                                    .addOnSuccessListener(unused -> onSuccess.onSuccess(true)) // true = تم الإضافة
                                    .addOnFailureListener(onFailure);
                        }
                    } else {
                        onFailure.onFailure(new Exception("User document not found"));
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
    public void getFavoriteRecipes(OnSuccessListener<ArrayList<Recipe>> onSuccess,
                                   OnFailureListener onFailure) {
        FirebaseUser user = getAuth().getCurrentUser();
        if (user == null) {
            onFailure.onFailure(new Exception("No user logged in"));
            return;
        }

        String email = user.getEmail();
        fire.collection("Users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot userDoc = querySnapshot.getDocuments().get(0);
                        ArrayList<String> favoriteRecipeIds = (ArrayList<String>) userDoc.get("recipes");

                        if (favoriteRecipeIds == null || favoriteRecipeIds.isEmpty()) {
                            onSuccess.onSuccess(new ArrayList<>()); // لا توجد وصفات مفضلة
                            return;
                        }
                        fire.collection("Recipes")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot recipesSnapshot) {
                                        ArrayList<Recipe> favoriteRecipes = new ArrayList<>();
                                        for (QueryDocumentSnapshot doc : recipesSnapshot) {
                                            Recipe recipe = doc.toObject(Recipe.class);
                                            if (recipe != null && recipe.getIdRecipe() != null &&
                                                    favoriteRecipeIds.contains(recipe.getIdRecipe())) {
                                                favoriteRecipes.add(recipe);
                                            }
                                        }
                                        onSuccess.onSuccess(favoriteRecipes);
                                    }
                                })
                                .addOnFailureListener(onFailure);
                    } else {
                        onFailure.onFailure(new Exception("User document not found"));
                    }
                })
                .addOnFailureListener(onFailure);
    }

}





