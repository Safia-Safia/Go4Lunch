package com.safia.go4lunch.viewmodel;

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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

    public Task<Void> signOut(Context context){
        return AuthUI.getInstance().signOut(context);
    }

    public Task<DocumentReference> getLikeForThisRestaurant (Restaurant restaurant){
        return UserRepository.getLikeForThisRestaurant(restaurant);
    }

    public Task<DocumentReference> getPickedRestaurant (Restaurant restaurant){
        return UserRepository.getPickedRestaurant(restaurant);
    }

    public Task<Void> updateRestaurantPicked (String restaurantName, String uid){
        return UserRepository.updateRestaurantPicked(restaurantName,uid);
    }


    public void createUser(){
        userRepository.createUser();
    }

}