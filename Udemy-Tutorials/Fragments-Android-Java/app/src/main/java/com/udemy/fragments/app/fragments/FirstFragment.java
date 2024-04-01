package com.udemy.fragments.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.udemy.fragments.app.R;

public class FirstFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d("--fragment_first--", "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("--fragment_first--", "onCreate: ");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("--fragment_first--", "onResume: ");
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        TextView txtTitle = view.findViewById(R.id.txt_title);
        Button btnHello = view.findViewById(R.id.btn_hello);

        btnHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Welcome to the First Fragment", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}