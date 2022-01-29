package com.safia.go4lunch.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.safia.go4lunch.R;

public class WorkmatesFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.workmates, container, false);
    }
}