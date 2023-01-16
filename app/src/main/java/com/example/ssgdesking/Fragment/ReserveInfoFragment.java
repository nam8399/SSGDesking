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
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.FragmentReserveInfoBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveInfoFragment extends Fragment {
    FragmentReserveInfoBinding binding;
    private ProgressDialog progressDialog;
    public static boolean isReserveLeave = false;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public ReserveInfoFragment() {
        // Required empty public constructor
    }

    private static class SingletonHolder {
        public static final ReserveInfoFragment INSTANCE = new ReserveInfoFragment();
    }

    public static ReserveInfoFragment getInstance() {
        return SingletonHolder.INSTANCE;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveInfoBinding.inflate(inflater);

        initView();


        return binding.getRoot();
    }

    private void initView() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String seatinfo = pref.getString("RESERVE_SEAT_INFO", "");
        String entertime = pref.getString("RESERVE_ENTER_TIME", "");
        String leavetime = pref.getString("RESERVE_LEAVE_TIME", "");

        try {
            if (!seatinfo.equals("") &&
                    !entertime.equals("") &&
                    !leavetime.equals("")) {
                binding.reserveInfoNotice.setText("퇴실 시 좌석 반납 부탁드립니다.");
                binding.reserveInfoInfo.setText(seatinfo);
                binding.reserveInfoStatus.setText("배정");
                binding.reserveInfoStarttime.setText(entertime);
                binding.reserveInfoEndtime.setText(leavetime);
                binding.reserveInfoReserveAfterLayout.setVisibility(View.VISIBLE);
                binding.reserveInfoReserveLayout.setVisibility(View.GONE);
            } else {
                binding.reserveInfoReserveAfterLayout.setVisibility(View.GONE);
                binding.reserveInfoReserveLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.reserveInfoReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReserveFragment reserveFragment = ReserveFragment.getInstance();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentFrame, reserveFragment).commit();
            }
        });

        binding.reserveInfoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReserveFragment reserveFragment = ReserveFragment.getInstance();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentFrame, reserveFragment).commit();
            }
        });

        binding.reserveInfoReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveLeavePost();
            }
        });

        binding.reserveInfoReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReserveLeaveSuccessFragment reserveLeaveSuccessFragment = new ReserveLeaveSuccessFragment();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveLeaveSuccessFragment).commit();
                isReserveLeave = false;
            }
        });

    }

    public void reserveLeavePost(){
        isReserveLeave = true;
        LoginData loginData = new LoginData(LoginActivity.LOGIN_DATA, ReserveFragment.MY_SEAT_ID);
        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().reserveLeavePost(ReserveFragment.MY_SEAT_ID, loginData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("퇴실하기 - 연결이 비정상적 : ", "error code : " + response.code());
                    Log.d("response : ", response.toString());
                    return;
                }
                String checkAlready = response.body();
                Log.d("퇴실하기 - 연결이 성공적 : ", response.body().toString());

                if (response.body().contains("성공")) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);

                    Handler handler = new Handler(Looper.getMainLooper());
                    progressDialog.show();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            ReserveLeaveSuccessFragment reserveLeaveSuccessFragment = new ReserveLeaveSuccessFragment();
                            fragmentTransaction.replace(R.id.fragmentFrame, reserveLeaveSuccessFragment).commit();
                        }
                    });
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
    }
}