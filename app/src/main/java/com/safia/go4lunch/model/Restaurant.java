package com.safia.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.safia.go4lunch.model.placeDetailResult.OpeningHours;

import java.util.ArrayList;
import java.util.List;


public class Restaurant implements Parcelable {
    @SerializedName("restaurantId")
    @Expose
    private String restaurantId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("urlPhoto")
    @Expose
    private String urlPhoto;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("openingHour")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("types")
    @Expose
    private String types;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("distance")
    @Expose
    private long distance;
    List<User> users = new ArrayList<>();

    public  Restaurant (){
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhotoForRetrofit(String urlPhoto) {
        this.urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference="
                + urlPhoto + "&key=AIzaSyDho4ut-Xsxg7efCchEwhcJe7uKqJANJnM";
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.restaurantId);
        dest.writeString(this.name);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeString(this.address);
        dest.writeString(this.urlPhoto);
        dest.writeValue(this.rating);
        dest.writeString(this.phoneNumber);
        dest.writeParcelable(this.openingHours, flags);
        dest.writeString(this.types);
        dest.writeString(this.website);
        dest.writeLong(this.distance);
        dest.writeList(this.users);
    }


    protected Restaurant(Parcel in) {
        this.restaurantId = in.readString();
        this.name = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.address = in.readString();
        this.urlPhoto = in.readString();
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
        this.phoneNumber = in.readString();
        this.openingHours = in.readParcelable(OpeningHours.class.getClassLoader());
        this.types = in.readString();
        this.website = in.readString();
        this.distance = in.readLong();
        in.readList(users, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
