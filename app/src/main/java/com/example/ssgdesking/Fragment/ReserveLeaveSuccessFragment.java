package com.example.ssgdesking.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Data.LoginData;
import com.example.ssgdesking.Data.ReservationDTO;
import com.example.ssgdesking.Data.ReserveInfoData;
import com.example.ssgdesking.Data.ReviewData;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.databinding.FragmentReserveLeaveSuccessBinding;
import com.example.ssgdesking.databinding.FragmentReserveSuccessBinding;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveLeaveSuccessFragment extends Fragment {
    private FragmentReserveLeaveSuccessBinding binding;
    ReserveFragment reserveFragment;
    SharedPreferences pref;

    public ReserveLeaveSuccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveLeaveSuccessBinding.inflate(inflater);
        initView();
        controller();

        return binding.getRoot();
    }

    private void initView() {
        reserveFragment = ReserveFragment.getInstance();
        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        String seatinfo = pref.getString("RESERVE_SEAT_TITLE", "");

        binding.reserveSuccessSeatnum.setText(seatinfo);

        if (ReserveInfoFragment.isReserveLeave) {
            binding.reserveLeaveReturnSuccess.setText("좌석 반납 완료");
            binding.animSucceess.setVisibility(View.VISIBLE);
        } else {
            binding.reserveLeaveReturnSuccess.setText("");
            binding.animSucceess.setVisibility(View.INVISIBLE);
        }
    }

    private void controller() {
        binding.reserveLeaveGotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveFragment).commit();
            }
        });

        binding.reserveLeaveWritereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postReviewData(binding.reserveLeaveEdittext.getText().toString());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveFragment).commit();
            }
        });
    }

    private void postReviewData(String context) {
        ReviewData reviewData = new ReviewData(LoginActivity.LOGIN_DATA, ReserveFragment.MY_SEAT_ID, context);
        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().postReview(reviewData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if(!response.isSuccessful()){
                        Log.e("리뷰정보 확인 - 연결이 비정상적 : ", "error code : " + response.code());
                        Log.d("response : ", response.toString());
                        return;
                    }
                    String result = response.body();
                    Log.d("리뷰정보 확인 - 연결이 성공적 : ", response.body().toString());
                    Toast.makeText(getContext(), "후기 작성이 완료됐습니다.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("리뷰정보 - 연결실패", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }
}