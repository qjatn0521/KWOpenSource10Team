package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.FragNotiBinding;
import com.example.myapplication.sports.SportsActivity;

public class FragNoti extends Fragment {

    private FragNotiBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragNotiBinding.inflate((getLayoutInflater()));

        binding.btnSettingSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SportsActivity.class);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }
}
