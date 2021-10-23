package com.safia.go4lunch.ui.maps;

import com.safia.go4lunch.model.nearbySearchResult.NearbyPlace;
import com.safia.go4lunch.model.Restaurant;

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


    @GET("details/json?"+
            "?fields=name%2Crating%2Cformatted_phone_number " +
            "&place_id=ChIJN1t_tDeuEmsRUsoyG83frY4 " +
            "&key=AIzaSyDho4ut-Xsxg7efCchEwhcJe7uKqJANJnM")
    Call<Restaurant> getRestaurantDetails(@Query("place_id") String placeId,
                                                @Query("key") String key);
}