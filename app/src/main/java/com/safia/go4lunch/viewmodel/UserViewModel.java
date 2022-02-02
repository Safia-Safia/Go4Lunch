package com.safia.go4lunch.viewmodel;

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
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
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserViewModel();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() != null);
    }

    public void signOut(Context context){
        AuthUI.getInstance().signOut(context);
    }

    public Task<Void> getLikeForThisRestaurant (Restaurant restaurant){
        return UserRepository.addRestaurantLike(restaurant);
    }

    public void getPickedRestaurant (Restaurant restaurant){
        UserRepository.getPickedRestaurant(restaurant);
    }

    public void createUser(){
        userRepository.createUser();
    }

}