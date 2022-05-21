package com.safia.go4lunch.repository;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

    public UserRepository() {
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

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //--- GET ---
    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }


    // -- LIKE --
    public void addRestaurantLike(Restaurant restaurant) {
        getLikedCollection().document(restaurant.getRestaurantId()).set(restaurant);
    }

    public void removeRestaurantLiked(Restaurant restaurant) {
        getLikedCollection().document(restaurant.getRestaurantId()).delete();
    }

    public LiveData<Boolean> getLikeStatus(Restaurant restaurant) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        getLikedCollection().get().addOnCompleteListener(task -> {
            List<Restaurant> restaurantLiked = task.getResult().toObjects(Restaurant.class);
            boolean isLiked = false;
            for (Restaurant restaurant1 : restaurantLiked) {
                if (restaurant1.getRestaurantId().contains(restaurant.getRestaurantId())) {
                    isLiked = true;
                }
            }
            result.postValue(isLiked);
        });
        return result;
    }

    // -- PICKED --
    public LiveData<Boolean> addPickedRestaurant(Restaurant restaurant) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
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
            result.postValue(true);
        });
        return result;
    }

    public LiveData<Boolean> removePickedRestaurant() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        getUsersCollection().document(Objects.requireNonNull(getInstance().getCurrentUserUID())).get().addOnCompleteListener(task -> {
            User user = task.getResult().toObject(User.class);
            RestaurantRepository.getInstance().getRestaurantCollection().document(user.getRestaurantPicked()
                    .getRestaurantId()).collection(RestaurantRepository.USER_PICKED).document(user.uid).delete();
            user.setRestaurantPicked(null);
            getUsersCollection().document(user.getUid()).set(user);
            result.postValue(true);
        });
        return  result;
    }

    //- ALL USERS IN THE APP --
    public LiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
                users.postValue(userList);
            }
        });
        return users;
    }

    //HomeActivity
    public boolean getUserRestaurant() {
        return true;
    }

}
