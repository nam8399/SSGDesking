package com.example.ssgdesking.Fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.ssgdesking.Data.ReservationReportData;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.FragmentReserveReportBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveReportFragment extends Fragment {
    private FragmentReserveReportBinding binding;
    ReserveReportSuccessFragment reserveReportSuccessFragment;
    private Spinner spinner_floor, spinner_reason;
    private CustomEreportSpinnerAdapter adapter_floor, adapter_reason;
    private List<String> list_floor = new ArrayList<>();
    private List<String> list_reason = new ArrayList<>();
    private int reason, floor;
    public static String report_number, report_seatid;
    ProgressDialog progressDialog;

    public ReserveReportFragment() {
        // Required empty public constructor
    }
    private static class SingletonHolder {
        public static final ReserveReportFragment INSTANCE = new ReserveReportFragment();
    }
    public static ReserveReportFragment getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveReportBinding.inflate(inflater);
        initView();
        controller();

        return binding.getRoot();
    }

    private void initView() {
        reserveReportSuccessFragment = ReserveReportSuccessFragment.getInstance();
        //로딩창 객체 생성
        progressDialog = new ProgressDialog(getContext());
        //로딩창을 투명하게
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding.reserveEreportEdittext.setText("");

    }

    private void controller() {
        spinner_floor = binding.reserveReportFloor;
        spinner_reason = binding.reserveReportReason;
        list_floor.clear();
        list_reason.clear();

        list_floor.add("19F");
        list_floor.add("21F");


        list_reason.add("모니터 고장");
        list_reason.add("HDMI 고장");
        list_reason.add("의자 고장");
        list_reason.add("책상 고장");
        list_reason.add("기타");

        // 스피너에 붙일 어댑터 초기화
        adapter_floor = new CustomEreportSpinnerAdapter(getContext(), list_floor);
        adapter_reason = new CustomEreportSpinnerAdapter(getContext(), list_reason);
        spinner_floor.setAdapter(adapter_floor);
        spinner_reason.setAdapter(adapter_reason);

        binding.reserveEreportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReserveFragment reserveFragment = ReserveFragment.getInstance();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveFragment).commit();
                Toast.makeText(getContext(), "신고 정보가 작성되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.reserveEreportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                postReportData(String.valueOf(reason), binding.reserveEreportEdittext.getText().toString());
            }
        });

        spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reason = i + 1;
                Log.d("Report Reason : ", String.valueOf(reason));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                floor = i + 1;
                Log.d("Report floor : ", String.valueOf(floor));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void postReportData(String rtype, String rcomment) {
        ReservationReportData reservationReportData = new ReservationReportData(LoginActivity.LOGIN_DATA, ReserveFragment.MY_SEAT_ID, rtype, rcomment);

        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().reserveReportPost(reservationReportData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if(!response.isSuccessful()){
                        Log.e("좌석신고정보 확인 - 연결이 비정상적 : ", "error code : " + response.code());
                        Log.d("response : ", response.toString());
                        return;
                    }
                    String result = response.body();
                    Log.d("좌석신고정보 확인 - 연결이 성공적 : ", response.body());
                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(result);

                    report_number = jsonObj.get("response").toString();
                    report_seatid = binding.reserveReportSeatid.getText().toString();

                    Log.d("ereport_number : ", report_number);


                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragmentTransaction.replace(R.id.fragmentFrame, reserveReportSuccessFragment).commit();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("좌석신고정보 - 연결실패", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }
}