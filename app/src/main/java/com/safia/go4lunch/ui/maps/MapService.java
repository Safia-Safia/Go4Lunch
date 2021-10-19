package com.safia.go4lunch.ui.maps;

import com.safia.go4lunch.model.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {
    @GET("nearbysearch/json" +
            "?sensor=true" +
            "&key=AIzaSyDho4ut-Xsxg7efCchEwhcJe7uKqJANJnM" +
            "&type=restaurant")
    Call<NearbyPlace> getNearbyPlaces(@Query("location") String location, @Query("radius") int radius);
}