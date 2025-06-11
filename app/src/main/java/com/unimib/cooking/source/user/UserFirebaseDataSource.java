package com.unimib.cooking.source.user;

import static com.unimib.cooking.util.Constants.FIREBASE_FAVORITE_RECIPES_COLLECTION;
import static com.unimib.cooking.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unimib.cooking.model.Recipe;
import com.unimib.cooking.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserFirebaseDataSource extends BaseUserDataRemoteDataSource {
    private static final String TAG = UserFirebaseDataSource.class.getSimpleName();
    private final DatabaseReference databaseReference;

    public UserFirebaseDataSource(String firebaseDatabaseUrl) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseDatabaseUrl);
        databaseReference = firebaseDatabase.getReference().getRef();
    }

    @Override
    public void saveUserData(User user) {
        Log.d("tag", "entrato");
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d(TAG, "User already present in Firebase Realtime Database");
                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                } else {
                    Log.d(TAG, "User not present in Firebase Realtime Database");
                    databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getIdToken()).setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    userResponseCallback.onSuccessFromRemoteDatabase(user);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    userResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
            }
        });
    }

    @Override
    public void getUserFavoriteRecipes(String idToken) {
        databaseReference
            .child(FIREBASE_USERS_COLLECTION)
            .child(idToken)
            .child(FIREBASE_FAVORITE_RECIPES_COLLECTION)
            .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error getting data", task.getException());
                        userResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
                    }
                    else {
                        Log.d(TAG, "Successful read: " + task.getResult().getValue());

                        List<Recipe> recipes = new ArrayList<>();
                        for(DataSnapshot ds : task.getResult().getChildren()) {
                            Recipe recipe = ds.getValue(Recipe.class);
                            recipes.add(recipe);
                        }

                        userResponseCallback.onSuccessFromRemoteDatabase(recipes);
                    }
                });
    }

    public void updateFavoriteStatus(String idToken, Recipe selectedRecipe) {
        databaseReference
            .child(FIREBASE_USERS_COLLECTION)
            .child(idToken)
            .child(FIREBASE_FAVORITE_RECIPES_COLLECTION)
            .get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Error getting data", task.getException());

                } else {
                    Log.d(TAG, "Successful read: " + task.getResult().getValue());

                    List<Recipe> recipes = new ArrayList<>();
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        Recipe recipe = ds.getValue(Recipe.class);

                        if (recipe == null) continue;

                        if (!selectedRecipe.getLiked() && recipe.getIdMeal() == selectedRecipe.getIdMeal()) {
                            continue;
                        }

                        recipes.add(recipe);
                    }

                    if (selectedRecipe.getLiked()) {
                        recipes.add(selectedRecipe);
                    }

                    databaseReference
                            .child(FIREBASE_USERS_COLLECTION)
                            .child(idToken)
                            .child(FIREBASE_FAVORITE_RECIPES_COLLECTION)
                            .setValue(recipes);
                }
            });
    }

    @Override
    public void deleteProfile(String idToken) {
        databaseReference
            .child(FIREBASE_USERS_COLLECTION)
            .child(idToken)
            .removeValue()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    userResponseCallback.onSuccessDeleteProfile();
                } else {
                    userResponseCallback.onErrorDeleteProfile();
                }
            });
    }

    @Override
    public void removeAllFavorites(String idToken) {
        databaseReference
                .child(FIREBASE_USERS_COLLECTION)
                .child(idToken)
                .child(FIREBASE_FAVORITE_RECIPES_COLLECTION)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userResponseCallback.onSuccessDeleteAllFavorites();
                    } else {
                        userResponseCallback.onErrorDeleteAllFavorites();
                    }
                });
    }
}
