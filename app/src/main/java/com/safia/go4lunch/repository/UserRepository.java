package com.safia.go4lunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserRepository {

    public static final String COLLECTION_USERS = "users";
    private static volatile UserRepository instance;
    private static final String COLLECTION_LIKED = "restaurant liked";
    public static final String RESTAURANT_PICKED_BY = "restaurant picked by";
    public static boolean isRestaurantPicked = false;

    // Get the Collection Reference
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }

    public static CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(Objects.requireNonNull(getInstance().getCurrentUserUID())).collection(COLLECTION_LIKED);

    }

    public static CollectionReference getPickedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(Objects.requireNonNull(getInstance().getCurrentUserUID())).collection(RESTAURANT_PICKED_BY);
    }

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    //--- CREATE ---

    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) { //Si on à bel et bien un utilisateur il récupère les iniformations suivantes
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();
            User userToCreate = new User(uid, username, urlPicture, false);
            getUsersCollection().document(uid).set(userToCreate);
        }
    }

    public static Task<Void> createLike(String restaurantId, String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put(userId, true);
        return UserRepository.getLikedCollection().document(restaurantId).set(user, SetOptions.merge());
    }

    //--- GET ---

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<QuerySnapshot> getAllUsersFromFirebase(){
        return getUsersCollection().orderBy("username").get();
    }

    public static Task<DocumentReference> getLikeForThisRestaurant(Restaurant restaurant) {
        return UserRepository.getLikedCollection().add(restaurant);
    }

    public static Task<DocumentReference> getPickedRestaurant(Restaurant restaurant) {
        isRestaurantPicked = true;
        return UserRepository.getPickedCollection().add(restaurant);
    }

    //--- UPDATE ---
    public static Task<Void> updateRestaurantPicked(String restaurantName, String uid) {
        return getPickedCollection().document(uid).update(RESTAURANT_PICKED_BY, restaurantName);
    }

    //--- REMOVE ---

}
