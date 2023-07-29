package com.example.english.ui.word;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WordViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is word fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}