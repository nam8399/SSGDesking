package com.example.ssgdesking.Fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ssgdesking.Activity.ReserveResultActivity;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.FragmentReserveConfirmBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReserveConfirmFragment extends Fragment implements onBackPressedListener {
    private FragmentReserveConfirmBinding binding;
    ReserveSuccessFragment reserveSuccessFragment;
    ReserveResultActivity reserveResultActivity;
    private ProgressDialog progressDialog;
    private Calendar cal;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat mFormat_bundle = new SimpleDateFormat("hh:mm"); // Fragment로 보낼 날짜 데이터 format

    public ReserveConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveConfirmBinding.inflate(inflater);

        initSetting();
        controller();

        return binding.getRoot();
    }

    private void initSetting() {
        reserveSuccessFragment = new ReserveSuccessFragment();

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        cal = Calendar.getInstance();
        cal.setTime(mDate);

        cal.add(Calendar.HOUR, 9);

        reserveResultActivity = ReserveResultActivity.getInstance();
        binding.reserveConfirmInfo.setText(reserveResultActivity.reserveInfo + "번");
        binding.reserveConfirmStarttime.setText(getTime());
        binding.reserveConfirmEndtime.setText(mFormat.format(cal.getTime()));


        //로딩창 객체 생성
        progressDialog = new ProgressDialog(getContext());
        //로딩창을 투명하게
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void controller() {
        binding.reserveConfirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.reserveConfirmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);

                Bundle bundle = new Bundle(); // 번들을 통해 값 전달

                Handler handler = new Handler(Looper.getMainLooper());
                progressDialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        bundle.putString("SeatNumber", reserveResultActivity.reserveInfo);
                        bundle.putString("SeatTime", mFormat_bundle.format(cal.getTime()));
                        reserveSuccessFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragmentFrame, reserveSuccessFragment).commit();
                    }
                },5000);
            }
        });
    }

    private String getTime(){
        return mFormat.format(mDate);
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }
}