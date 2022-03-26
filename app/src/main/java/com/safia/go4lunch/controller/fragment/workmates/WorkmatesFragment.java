package com.safia.go4lunch.controller.fragment.workmates;

import static com.safia.go4lunch.repository.RestaurantRepository.USER_PICKED;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.safia.go4lunch.R;

import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.RestaurantRepository;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.RestaurantViewModel;
import com.safia.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment {
    private List<User> userList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private final UserViewModel userViewModel = UserViewModel.getInstance();
    private final RestaurantViewModel restaurantViewModel = RestaurantViewModel.getInstance();
    WorkmatesAdapter2 mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.workmates, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_userList);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        mAdapter = new WorkmatesAdapter2(userList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        UserRepository.getInstance().getUsersCollection().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
            }
        });
    }

}