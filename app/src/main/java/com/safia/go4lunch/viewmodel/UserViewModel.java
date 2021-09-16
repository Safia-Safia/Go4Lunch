package com.safia.go4lunch.viewmodel;

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;

public class UserViewModel {
    private static volatile UserViewModel instance;
    private UserRepository userRepository;

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


    public void createUser(){
        userRepository.createUser();
    }

    public Task<User> getUserData(){
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class)) ;
    }
}