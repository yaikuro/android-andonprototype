package com.app.andonprototype.ui.AssetManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AssetActivityViewModel extends ViewModel {

    private MutableLiveData<String> welcomeText;

    public AssetActivityViewModel() {
    }

    public LiveData<String> getText() {
        return welcomeText;
    }
}