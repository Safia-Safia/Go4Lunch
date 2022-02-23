package com.safia.go4lunch.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.repository.UserRepository;

public class UserViewModel {
    private static volatile UserViewModel instance;
    private final UserRepository userRepository;

    private UserViewModel() {
        userRepository = UserRepository.getInstance();
    }

    public static UserViewModel getInstance() {
        UserViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserViewModel();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public void signOut(Context context) {
        AuthUI.getInstance().signOut(context);
    }

    public void addLikeForThisRestaurant(Restaurant restaurant) {
        userRepository.addRestaurantLike(restaurant);
    }

    public void addPickedRestaurant(Restaurant restaurant) {
        userRepository.addPickedRestaurant(restaurant);
    }

    public void  removeRestaurantLiked(Restaurant restaurant) {
        userRepository.removeRestaurantLiked(restaurant);
    }

    public void removeRestaurantPicked() {
        userRepository.removePickedRestaurant();
    }

    public void createUser() {
        userRepository.createUser();
    }

}