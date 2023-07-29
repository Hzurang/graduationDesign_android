package com.example.english.ui.essay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EssayViewModel extends ViewModel {

    private boolean isDataLoading = false;

    private boolean isDataUpdate = false;

    public boolean isDataUpdate() {
        return isDataUpdate;
    }

    public void setDataUpdate(boolean isDataUpdate) {
        isDataUpdate = isDataUpdate;
    }

    public boolean isDataLoading() {
        return isDataLoading;
    }

    public void setDataLoading(boolean dataLoading) {
        isDataLoading = dataLoading;
    }
}