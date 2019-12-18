package com.app.andonprototype.ui.Report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportActivityViewModel extends ViewModel {

    private MutableLiveData<String> welcomeText;

    public ReportActivityViewModel() {
//        welcomeText = new MutableLiveData<>();
//        String pic = getID(MainDashboardViewModel.this);
//        welcomeText.setValue("Welcome " + pic);
    }

    public LiveData<String> getText() {
        return welcomeText;
    }
}