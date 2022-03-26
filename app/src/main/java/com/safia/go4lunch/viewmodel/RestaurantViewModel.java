package com.safia.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.safia.go4lunch.controller.fragment.workmates.WorkmatesAdapter;
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

    public Task<QuerySnapshot> getAllUserForThisRestaurant(Restaurant restaurant, List<User> userList, WorkmatesAdapter adapter){
        return repository.getAllUsersForThisRestaurant(restaurant, userList, adapter);
    }

    public void setRestaurantStatus(Restaurant restaurant){
        repository.setRestaurantStatus(restaurant);
    }
}
