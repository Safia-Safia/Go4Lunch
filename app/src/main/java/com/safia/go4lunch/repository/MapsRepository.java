package com.safia.go4lunch.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.ui.maps.MapService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsRepository {
    public static final String GET_USER_LOCATION = "GET_USER_LOCATION";
    public static final int GET_RADIUS = 0;

    public LiveData<List<Restaurant>> getRestaurant(Restaurant restaurant,String location,int radius){
        final MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Get a Retrofit instance and the related endpoints
        MapService mapService = retrofit.create(MapService.class);

        // Create the call on NearbyPlace API
        Call<List<Restaurant>> call = mapService.getNearbyPlaces(restaurant, location,radius);
        // Start the call
        call.enqueue(new Callback<List<Restaurant>>() {

            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                result.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
            }
        });
        return result;
    }
}
