package com.safia.go4lunch.ui.listview;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListViewViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListViewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }
}