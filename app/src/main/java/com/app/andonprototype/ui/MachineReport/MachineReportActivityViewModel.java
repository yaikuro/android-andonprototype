package com.app.andonprototype.ui.MachineReport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MachineReportActivityViewModel extends ViewModel {

    private MutableLiveData<String> welcomeText;

    public MachineReportActivityViewModel() {
//        welcomeText = new MutableLiveData<>();
//        String pic = getID(MainDashboardViewModel.this);
//        welcomeText.setValue("Welcome " + pic);
    }

    public LiveData<String> getText() {
        return welcomeText;
    }
}