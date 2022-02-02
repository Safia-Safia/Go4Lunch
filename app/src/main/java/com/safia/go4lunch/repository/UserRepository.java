package com.safia.go4lunch.repository;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;

import java.util.Objects;

public class UserRepository {

    public static final String COLLECTION_USERS = "users";
    private static volatile UserRepository instance;
    public static final String COLLECTION_LIKED = "restaurant liked";
    public static final String RESTAURANT_PICKED = "restaurant picked";

    //--- GET THE COLLECTION REFERENCE ---
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }
    public static CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(Objects.requireNonNull(getInstance().getCurrentUserUID())).collection(COLLECTION_LIKED);
    }
    public static CollectionReference getPickedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(Objects.requireNonNull(getInstance().getCurrentUserUID())).collection(RESTAURANT_PICKED);
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

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    //--- CREATE ---
    public void createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
            String username = user.getDisplayName();
            String uid = user.getUid();
            User userToCreate = new User(uid, username, urlPicture);
            getUsersCollection().document(uid).set(userToCreate);
        }
    }

    //--- GET ---

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<QuerySnapshot> getAllUsersFromFirebase() {
        return getUsersCollection().orderBy("username").get();
    }

    public void getAllUsersEating() {
         getPickedCollection().document().get().addOnCompleteListener(task -> {
            getUsersCollection().orderBy("username").get().getResult().getDocuments();
        });
    }

    public static Task<Void> addRestaurantLike(Restaurant restaurant) {
        return UserRepository.getLikedCollection().document(restaurant.getRestaurantId()).set(restaurant);
    }
    public static void removeRestaurantLiked (Restaurant restaurant) {
       // UserRepository.getLikedCollection().document(restaurant.getRestaurantId()).update(restaurant, FieldValue.delete(restaurant));
    }

    public static Task<DocumentReference> getPickedRestaurant(Restaurant restaurant) {
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            user.setRestaurantPicked(restaurant);
            getUsersCollection().document(user.getUid()).set(user);
        });
        return UserRepository.getPickedCollection().add(restaurant);
    }



}
