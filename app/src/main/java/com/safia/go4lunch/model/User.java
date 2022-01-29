package com.safia.go4lunch.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class User {
    public String uid;
    public String username;
    @Nullable
    private String urlPicture;
    private boolean isRestaurantPicked;
    public User() { }

    public User(String uid, String username, @Nullable String urlPicture, boolean isRestaurantPicked) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.isRestaurantPicked = isRestaurantPicked;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public boolean getRestaurantPicked(boolean isRestaurantPicked) { return isRestaurantPicked; }


    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantPicked(boolean restaurantPicked) {
        isRestaurantPicked = restaurantPicked;
    }
}
