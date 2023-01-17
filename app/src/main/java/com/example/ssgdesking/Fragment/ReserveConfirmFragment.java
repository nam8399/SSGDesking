package com.example.ssgdesking.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Activity.ReserveResultActivity;
import com.example.ssgdesking.Data.LoginData;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.FragmentReserveConfirmBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveConfirmFragment extends Fragment implements onBackPressedListener {
    private FragmentReserveConfirmBinding binding;
    ReserveSuccessFragment reserveSuccessFragment;
    ReserveResultActivity reserveResultActivity;
    public static String RESERVE_SEAT_INFO, RESERVE_ENTER_TIME, RESERVE_LEAVE_TIME;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
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
        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        reserveSuccessFragment = new ReserveSuccessFragment();

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        cal = Calendar.getInstance();
        cal.setTime(mDate);

        cal.add(Calendar.HOUR, 10);

        reserveResultActivity = ReserveResultActivity.getInstance();
        binding.reserveConfirmInfo.setText(reserveResultActivity.reserveInfo + "번");
        binding.reserveConfirmStarttime.setText(getTime());
        binding.reserveConfirmEndtime.setText(mFormat.format(cal.getTime()));
        RESERVE_SEAT_INFO = reserveResultActivity.reserveInfo + "번";
        RESERVE_ENTER_TIME = getTime();
        RESERVE_LEAVE_TIME = mFormat.format(cal.getTime());
        editor.putString("RESERVE_SEAT_INFO", RESERVE_SEAT_INFO);
        editor.putString("RESERVE_ENTER_TIME", RESERVE_ENTER_TIME);
        editor.putString("RESERVE_LEAVE_TIME", RESERVE_LEAVE_TIME);
        editor.apply();

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
                reservePost(reserveResultActivity.reserveSeatID);
            }
        });
    }

    public void reservePost(String empno){
        Log.d("empno : ", empno);
        LoginData loginData = new LoginData(LoginActivity.LOGIN_DATA);
        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().reservePost(empno, loginData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("예약확인 - 연결이 비정상적 : ", "error code : " + response.code());
                    Log.d("response : ", response.toString());
                    return;
                }
                String checkAlready = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);

                Bundle bundle = new Bundle(); // 번들을 통해 값 전달

                Handler handler = new Handler(Looper.getMainLooper());
                progressDialog.show();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        bundle.putString("SeatNumber", reserveResultActivity.reserveInfo);
                        bundle.putString("SeatTime", mFormat_bundle.format(cal.getTime()));
                        reserveSuccessFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragmentFrame, reserveSuccessFragment).commit();
                    }
                });
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
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