package com.safia.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {

    private static volatile UserRepository instance;
    public static final String COLLECTION_LIKED = "restaurant liked";
    public static final String COLLECTION_USERS = "users";

    //--- GET THE COLLECTION REFERENCE ---
    public CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }

    public CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .document(Objects.requireNonNull(getInstance().getCurrentUserUID())).collection(COLLECTION_LIKED);
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
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Task<Void> addRestaurantLike(Restaurant restaurant) {
        return getLikedCollection().document(restaurant.getRestaurantId()).set(restaurant);
    }

    public Task<Void> removeRestaurantLiked(Restaurant restaurant) {
        return getLikedCollection().document(restaurant.getRestaurantId()).delete();
    }

    public void addPickedRestaurant(Restaurant restaurant) {
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            //Récupérer le restaurant qui avait été choisi
            //Si ce restaurant n'est pas null partir dans la collection userpicked de ce restaurant et enlever le user de cette liste
            if (user.getRestaurantPicked() != null) {
                RestaurantRepository.getInstance().getRestaurantCollection()
                        .document(user.getRestaurantPicked().getRestaurantId())
                        .collection(RestaurantRepository.USER_PICKED)
                        .document(user.uid).delete();
            }
            user.setRestaurantPicked(restaurant);
            RestaurantRepository.getInstance().getRestaurantCollection()
                    .document(restaurant.getRestaurantId())
                    .collection(RestaurantRepository.USER_PICKED)
                    .document(user.uid).set(user);
            getUsersCollection().document(user.getUid()).set(user);

        });
    }

    public void removePickedRestaurant() {
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            RestaurantRepository.getInstance().getRestaurantCollection().document(user.getRestaurantPicked().getRestaurantId())
                    .collection(RestaurantRepository.USER_PICKED).document(user.uid).delete();
            user.setRestaurantPicked(null);
            getUsersCollection().document(user.getUid()).set(user);
        });
    }

    public void getUserListFromFirebase() {
        getUsersCollection().document(COLLECTION_USERS).get().addOnCompleteListener(task -> {
            List<User> userList = new ArrayList<>();
            User user = task.getResult().toObject(User.class);
            userList.add(user);
        });
    }
}
