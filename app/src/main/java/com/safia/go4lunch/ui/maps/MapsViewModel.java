package com.safia.go4lunch.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.repository.MapsRepository;

import java.util.List;

public class MapsViewModel extends ViewModel {

    private MapsRepository repository;

    // CONSTRUCTOR
    public MapsViewModel(MapsRepository mapsRepository) {
        this.repository = mapsRepository;
    }

    public LiveData<List<Restaurant>> fetchRestaurantFollowing(String location,int radius){
        return repository.getRestaurant(location,radius);
    }}
