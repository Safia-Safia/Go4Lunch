package com.safia.go4lunch.controller.fragment.workmates;

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
import com.safia.go4lunch.R;

import com.safia.go4lunch.controller.activity.DetailActivity;
import com.safia.go4lunch.model.Restaurant;
import com.safia.go4lunch.model.User;
import com.safia.go4lunch.repository.UserRepository;
import com.safia.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment {
    private List<User> userList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private final UserViewModel userViewModel = UserViewModel.getInstance();
    WorkmatesAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.workmates, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_userList);
        setUpRecyclerView(userList);
        return view;
    }

    private void setUpRecyclerView(List<User> users) {
        mAdapter = new WorkmatesAdapter(users);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

}