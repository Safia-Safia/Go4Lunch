package com.safia.go4lunch.ui.maps;

import com.google.android.gms.maps.model.LatLng;
import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;

import com.safia.go4lunch.model.placeDetailResult.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {
    @GET("nearbysearch/json" +
            "?sensor=true" +
            "&key=AIzaSyDho4ut-Xsxg7efCchEwhcJe7uKqJANJnM" +
            "&type=restaurant"+
            "&radius=10000")
    Call<NearbyPlace> getNearbyPlaces(@Query("location") String location);


    @GET("details/json"+
            "?key=AIzaSyDho4ut-Xsxg7efCchEwhcJe7uKqJANJnM")
    Call<PlaceDetail> getRestaurantDetails(@Query("place_id") String result);
}