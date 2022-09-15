package com.safia.go4lunch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.model.nearbySearchResult.Geometry;
import com.safia.go4lunch.model.nearbySearchResult.Location;
import com.safia.go4lunch.model.nearbySearchResult.OpeningHours;
import com.safia.go4lunch.model.nearbySearchResult.Photo;
import com.safia.go4lunch.model.nearbySearchResult.Result;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;
import com.safia.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class ExampleUnitTest {
// get Restaurant get all UserForThisRestaurant

    private User user;
    private final Restaurant restaurant = new Restaurant();
    private final List<User> users = new ArrayList<>();
    @Mock
    private RestaurantViewModel restaurantViewModel;

    @Mock
    private UserViewModel userViewModel;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restaurantViewModel = new RestaurantViewModel(restaurantRepository);
        userViewModel = new UserViewModel(userRepository);

    }

    @Test
    public void getCurrentUser() {
        user = new User("1", "Jean Jean", "https://photoDeJean.jpg");
        List<User> users = new ArrayList<>();
        users.add(user);
        assertEquals("1", users.get(0).getUid());
        assertEquals("Jean Jean", users.get(0).getUsername());
        assertEquals("https://photoDeJean.jpg", users.get(0).getUrlPicture());
    }

    @Test
    public void getRestaurantPicked() {
        user = new User("1", "Jean Jean", "https://photoDeJean.jpg");
        restaurant.setName("Chez Janou");
        user.setRestaurantPicked(restaurant);
        users.add(user);
        assertEquals("Chez Janou", users.get(0).getRestaurantPicked().getName());
    }

    @Test
    public void getRestaurant() {
        MutableLiveData<List<Restaurant>> result = new MutableLiveData<>();
        final ArrayList<Restaurant> DummyRestaurant = new ArrayList<>();
        LatLng latLng = new LatLng(1234.3, 345.6);

        Restaurant restaurant1 = new Restaurant();
        Restaurant restaurant2 = new Restaurant();
        restaurant1.setName("Chez Janou");
        restaurant2.setName("Paradis du fruit");

        DummyRestaurant.add(restaurant1);
        DummyRestaurant.add(restaurant2);
        result.setValue(DummyRestaurant);
        Mockito.when(restaurantRepository.getRestaurant(latLng)).thenReturn(result);
        restaurantViewModel.getRestaurants(latLng).observeForever(restaurantList -> {
            assertEquals(DummyRestaurant, restaurantList);
        });

    }

    @Test
    public void getAllUsersForThisRestaurant(){
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        final ArrayList<User> dummyUsers = new ArrayList<>();
        User user1 = new User("1", "Jean Jean", "https://photoDeJean.jpg");
        User user2 = new User("2", "François France", "https://photoDeFrançois.jpg");
        restaurant.setName("Chez Janou");

        user1.setRestaurantPicked(restaurant);
        user2.setRestaurantPicked(restaurant);
        dummyUsers.add(user1);
        dummyUsers.add(user2);
        result.setValue(dummyUsers);

        Mockito.when(restaurantRepository.getAllUsersForThisRestaurant(restaurant)).thenReturn(result);
        restaurantViewModel.getAllUserForThisRestaurant(restaurant).observeForever(restaurantList -> {
            assertEquals(dummyUsers, restaurantList);
            assertEquals(restaurantList.size(),2);

        });
    }

    @Test
    public void getAllUsers(){
        MutableLiveData<List<User>> result = new MutableLiveData<>();
        final ArrayList<User> dummyUsers = new ArrayList<>();
        User user1 = new User("1", "Jean Jean", "https://photoDeJean.jpg");
        User user2 = new User("2", "François France", "https://photoDeFrançois.jpg");

        user1.setRestaurantPicked(restaurant);
        user2.setRestaurantPicked(restaurant);
        dummyUsers.add(user1);
        dummyUsers.add(user2);
        result.setValue(dummyUsers);

        Mockito.when(userRepository.getAllUsers()).thenReturn(result);
        userViewModel.getAllUsers().observeForever(allWorkmates -> {
            assertEquals(dummyUsers, allWorkmates);
            assertEquals(allWorkmates.size(),2);
        });
    }
}