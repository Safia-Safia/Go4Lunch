package com.safia.go4lunch.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;

import java.util.List;

public class RestaurantAndUserViewModel extends ViewModel {
   //-- REPOSITORIES
    private final UserRepository userRepository;
    private final RestaurantRepository repository;

    // CONSTRUCTOR
    public RestaurantAndUserViewModel(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.repository = restaurantRepository;
        this.userRepository = userRepository;
    }

    // -- RESTAURANT USER'S METHODS --

    public LiveData <List<User>> getAllUserForThisRestaurant(Restaurant restaurant){
        return repository.getAllUsersForThisRestaurant(restaurant);
    }

    public LiveData<Boolean> getCurrentUserPickedStatus(Restaurant restaurant){
        return repository.getCurrentUserPickedStatus(restaurant);
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


    // -- RESTAURANT REPOSITORY'S METHODS --

    public LiveData<List<Restaurant>> getRestaurants(LatLng location){
        return repository.getRestaurant(location);
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