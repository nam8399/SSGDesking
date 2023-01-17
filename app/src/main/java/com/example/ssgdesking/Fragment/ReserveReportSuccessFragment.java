package com.example.ssgdesking.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ssgdesking.R;
import com.example.ssgdesking.databinding.FragmentReserveSuccessEreportBinding;
import com.example.ssgdesking.databinding.FragmentReserveSuccessReportBinding;

public class ReserveReportSuccessFragment extends Fragment {
    private FragmentReserveSuccessReportBinding binding;
    ReserveFragment reserveFragment;

    public ReserveReportSuccessFragment() {
        // Required empty public constructor
    }
    private static class SingletonHolder {
        public static final ReserveReportSuccessFragment INSTANCE = new ReserveReportSuccessFragment();
    }
    public static ReserveReportSuccessFragment getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveSuccessReportBinding.inflate(inflater);
        initView();
        controller();

        return binding.getRoot();
    }

    private void initView() {
        reserveFragment = ReserveFragment.getInstance();

        binding.reserveReportSeatid.setText(ReserveReportFragment.report_seatid);
        binding.reserveReportNumber.setText("신고번호 [" + ReserveReportFragment.report_number + "]로 접수되었습니다.");
    }

    private void controller() {
        binding.reserveEreportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReserveFragment reserveFragment = ReserveFragment.getInstance();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveFragment).commit();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }
}