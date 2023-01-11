package com.example.ssgdesking.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ssgdesking.R;
import com.example.ssgdesking.databinding.FragmentReserveSuccessBinding;

public class ReserveSuccessFragment extends Fragment {
    private FragmentReserveSuccessBinding binding;

    public ReserveSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveSuccessBinding.inflate(inflater);
        initView();
        controller();

        return binding.getRoot();
    }

    private void initView() {
        if (getArguments() != null)
        {
            binding.reserveSuccessSeatnum.setText(getArguments().getString("SeatNumber"));
            binding.reserveSuccessEndtime.setText(getArguments().getString("SeatTime"));
        }
    }

    private void controller() {
        binding.reserveConfirmGotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }
}