package com.safia.go4lunch.Injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.viewmodel.MapsViewModel;

public class ViewModelFactory  implements ViewModelProvider.Factory {

    private final RestaurantRepository mapRepository;

    public ViewModelFactory(RestaurantRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
            return (T) new MapsViewModel(mapRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
