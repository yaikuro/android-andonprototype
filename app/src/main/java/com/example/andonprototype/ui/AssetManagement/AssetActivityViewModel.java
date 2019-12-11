package com.example.andonprototype.ui.AssetManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssetActivityViewModel extends ViewModel {

    private MutableLiveData<String> welcomeText;

    public AssetActivityViewModel() {
//        welcomeText = new MutableLiveData<>();
//        String pic = getID(MainDashboardViewModel.this);
//        welcomeText.setValue("Welcome " + pic);
    }

    public LiveData<String> getText() {
        return welcomeText;
    }
}