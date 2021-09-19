package com.safia.go4lunch.Injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.safia.go4lunch.repository.MapsRepository;
import com.safia.go4lunch.ui.maps.MapsViewModel;

public class ViewModelFactory  implements ViewModelProvider.Factory {

    private final MapsRepository mapRepository;

    public ViewModelFactory(MapsRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsRepository.class)) {
            return (T) new MapsViewModel(mapRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
