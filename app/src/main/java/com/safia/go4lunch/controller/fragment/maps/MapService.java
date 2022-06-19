package com.safia.go4lunch.controller.fragment.maps;

import com.safia.go4lunch.R;
import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;

import com.safia.go4lunch.model.placeDetailResult.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {
    @GET("nearbysearch/json" +
            "?sensor=true" +
            "&key="+ R.string.google_maps_key +
            "&type=restaurant"+
            "&radius=10000")
    Call<NearbyPlace> getNearbyPlaces(@Query("location") String location);


    @GET("details/json"+
            "?key="+ R.string.google_maps_key)
    Call<PlaceDetail> getRestaurantDetails(@Query("place_id") String result);
}