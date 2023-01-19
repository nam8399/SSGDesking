package com.example.ssgdesking.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Adapter.CustomEreportSpinnerAdapter;
import com.example.ssgdesking.Data.ReservationEreportData;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.databinding.FragmentReserveEreportBinding;
import com.example.ssgdesking.databinding.FragmentReserveSuccessEreportBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveEReportSuccessFragment extends Fragment {
    private FragmentReserveSuccessEreportBinding binding;
    ReserveFragment reserveFragment;

    public ReserveEReportSuccessFragment() {
        // Required empty public constructor
    }
    private static class SingletonHolder {
        public static final ReserveEReportSuccessFragment INSTANCE = new ReserveEReportSuccessFragment();
    }
    public static ReserveEReportSuccessFragment getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveSuccessEreportBinding.inflate(inflater);
        initView();
        controller();

        return binding.getRoot();
    }

    private void initView() {
        reserveFragment = ReserveFragment.getInstance();

        binding.reserveEreportNumber.setText("신고번호 [" + ReserveEReportFragment.ereport_number + "]로");
    }

    private void controller() {
        binding.reserveEreportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReserveFragment reserveFragment = ReserveFragment.getInstance();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveFragment).commit();
                Toast.makeText(getContext(), "신고 정보가 작성되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }
}