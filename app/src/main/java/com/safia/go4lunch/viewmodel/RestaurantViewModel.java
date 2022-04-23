package com.safia.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.safia.go4lunch.controller.fragment.workmates.WorkmatesPickedList;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.RestaurantRepository;

import java.util.List;

public class RestaurantViewModel extends ViewModel {
    private static volatile  RestaurantViewModel instance;
    private RestaurantRepository repository;
    private RestaurantViewModel() {
        repository = RestaurantRepository.getInstance();
    }

    public static RestaurantViewModel getInstance() {
        RestaurantViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantRepository.class) {
            if (instance == null) {
                instance = new RestaurantViewModel();
            }
            return instance;
        }
    }

    // CONSTRUCTOR
    public RestaurantViewModel(RestaurantRepository restaurantRepository) {
        this.repository = restaurantRepository;
    }

    public LiveData<List<Restaurant>> getRestaurants(LatLng location){
        return repository.getRestaurant(location);
    }

    public void getAllUserForThisRestaurant(Restaurant restaurant, List<User> userList){
        repository.getAllUsersForThisRestaurant(restaurant, userList);
    }

    public void getCurrentUserChoice(Restaurant restaurant){
        repository.getCurrentUserChoice(restaurant);
    }

    public void getUsersPickedList(Restaurant restaurant){
        repository.userPickedList(restaurant);
    }

}
