package com.safia.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private final RestaurantRepository repository;

    // CONSTRUCTOR
    public RestaurantViewModel(RestaurantRepository restaurantRepository) {
        this.repository = restaurantRepository;
    }

    // -- RESTAURANT REPOSITORY'S METHODS --

    public LiveData<List<Restaurant>> getRestaurants(LatLng location){
        return repository.getRestaurant(location);
    }

    public LiveData <List<User>> getAllUserForThisRestaurant(Restaurant restaurant){
        return repository.getAllUsersForThisRestaurant(restaurant);
    }

    public LiveData<Boolean> getCurrentUserPickedStatus(Restaurant restaurant){
        return repository.getCurrentUserPickedStatus(restaurant);
    }

}
