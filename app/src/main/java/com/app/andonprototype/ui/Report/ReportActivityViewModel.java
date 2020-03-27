package com.app.andonprototype.ui.Report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportActivityViewModel extends ViewModel {

    private MutableLiveData<String> welcomeText;

    public ReportActivityViewModel() {
    }

    public LiveData<String> getText() {
        return welcomeText;
    }
}