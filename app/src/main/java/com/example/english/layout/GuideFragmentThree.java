package com.example.english.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.english.R;
import com.example.english.activity.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GuideFragmentThree extends Fragment {
    private FloatingActionButton bt_enter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_guide_page_three, null);
        bt_enter = inflate.findViewById(R.id.ent_button);
        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return inflate;
    }
}