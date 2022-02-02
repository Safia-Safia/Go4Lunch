package com.safia.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.repository.MapsRepository;

import java.util.List;

public class MapsViewModel extends ViewModel {

    private final MapsRepository repository;

    // CONSTRUCTOR
    public MapsViewModel(MapsRepository mapsRepository) {
        this.repository = mapsRepository;
    }

    public LiveData<List<Restaurant>> getRestaurants(LatLng location){
        return repository.getRestaurant(location);
    }

}
