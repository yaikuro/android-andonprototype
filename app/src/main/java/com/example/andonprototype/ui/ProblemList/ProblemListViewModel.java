package com.example.andonprototype.ui.ProblemList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProblemListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProblemListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}