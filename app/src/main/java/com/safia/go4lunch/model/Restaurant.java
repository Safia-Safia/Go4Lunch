package com.safia.go4lunch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


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
    private Double rating;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("openingHour")
    @Expose
    private String openingHour;
    @SerializedName("types")
    @Expose
    private String types;
    @SerializedName("website")
    @Expose
    private String website;



    public Restaurant(String restaurantId, String name, Double latitude, Double longitude, @Nullable String address, @Nullable String urlPhoto, @Nullable Double rating, String phoneNumber, String openingHour) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.rating = rating;
        this.phoneNumber = phoneNumber;
        this.openingHour = openingHour;
    }

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

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
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
        dest.writeString(this.openingHour);
        dest.writeString(this.types);
        dest.writeString(this.website);
    }

    public void readFromParcel(Parcel source) {
        this.restaurantId = source.readString();
        this.name = source.readString();
        this.latitude = (Double) source.readValue(Double.class.getClassLoader());
        this.longitude = (Double) source.readValue(Double.class.getClassLoader());
        this.address = source.readString();
        this.urlPhoto = source.readString();
        this.rating = (Double) source.readValue(Double.class.getClassLoader());
        this.phoneNumber = source.readString();
        this.openingHour = source.readString();
        this.types = source.readString();
        this.website = source.readString();
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
        this.openingHour = in.readString();
        this.types = in.readString();
        this.website = in.readString();
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
