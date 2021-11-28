package com.safia.go4lunch.ui.listview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safia.go4lunch.R;
import com.safia.go4lunch.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    Restaurant restaurant = new Restaurant();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       setUpRecyclerView();
        return inflater.inflate(R.layout.list_view, container, false);
    }

    private void setUpRecyclerView() {
       /* RecyclerView rv = findViewById(R.id.list_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rv.setLayoutManager(layoutManager);
        mAdapter = new RestaurantRvAdapter();
        rv.setAdapter(mAdapter);*/
    }
}