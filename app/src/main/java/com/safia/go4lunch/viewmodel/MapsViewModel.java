package com.safia.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class MapsViewModel extends ViewModel {

    private final RestaurantRepository repository;

    // CONSTRUCTOR
    public MapsViewModel(RestaurantRepository restaurantRepository) {
        this.repository = restaurantRepository;
    }

    public LiveData<List<Restaurant>> getRestaurants(LatLng location){
        return repository.getRestaurant(location);
    }

}
