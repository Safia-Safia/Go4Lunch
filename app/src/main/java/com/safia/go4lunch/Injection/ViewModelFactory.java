package com.safia.go4lunch.Injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.RestaurantAndUserViewModel;

public class ViewModelFactory  implements ViewModelProvider.Factory {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public ViewModelFactory(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantAndUserViewModel.class)) {
            return (T) new RestaurantAndUserViewModel(restaurantRepository, userRepository);
        }
        throw new IllegalArgumentException("Unknown RestaurantAndUserViewModel class");
    }
}
