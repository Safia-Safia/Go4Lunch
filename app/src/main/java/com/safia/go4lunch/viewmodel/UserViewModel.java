package com.safia.go4lunch.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.safia.go4lunch.controller.fragment.workmates.WorkmatesAdapter2;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;

import java.util.List;

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

    public void getLikeStatus(Restaurant restaurant){
        userRepository.getLikeStatus(restaurant);
    }

    public LiveData<List<User>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public LiveData<Boolean> getCurrentUserLikeStatus(Restaurant restaurant){
        return userRepository.getLikeStatus(restaurant);
    }

}