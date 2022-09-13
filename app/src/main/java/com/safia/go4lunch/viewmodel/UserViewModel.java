package com.safia.go4lunch.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
   //-- REPOSITORIES
    private final UserRepository userRepository;

    // CONSTRUCTOR
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // -- RESTAURANT USER'S METHODS --


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

    public LiveData<Boolean> addPickedRestaurant(Restaurant restaurant) {
        return userRepository.addPickedRestaurant(restaurant);
    }

    public void  removeRestaurantLiked(Restaurant restaurant) {
        userRepository.removeRestaurantLiked(restaurant);
    }

    public LiveData<Boolean> removeRestaurantPicked() {
        return userRepository.removePickedRestaurant();
    }

    public void createUser() {
        userRepository.createUser();
    }

    public LiveData<List<User>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public LiveData<Boolean> getCurrentUserLikeStatus(Restaurant restaurant){
        return userRepository.getLikeStatus(restaurant);
    }
}