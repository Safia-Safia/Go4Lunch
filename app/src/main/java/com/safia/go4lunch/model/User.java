package com.safia.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class User implements Parcelable {
    public String uid;
    public String username;
    @Nullable
    private String urlPicture;
    private Restaurant restaurant;
    public User() { }

    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public Restaurant getRestaurantPicked() { return restaurant; }


    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(@Nullable String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantPicked(Restaurant restaurantPicked) {
        restaurant = restaurantPicked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.username);
        dest.writeString(this.urlPicture);
        dest.writeParcelable(this.restaurant, flags);
    }

    protected User(Parcel in) {
        this.uid = in.readString();
        this.username = in.readString();
        this.urlPicture = in.readString();
        this.restaurant = in.readParcelable(Restaurant.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
