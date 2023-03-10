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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Adapter.CustomEreportSpinnerAdapter;
import com.example.ssgdesking.Data.ReservationEreportData;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.FragmentReserveEreportBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveEReportFragment extends Fragment {
    private FragmentReserveEreportBinding binding;
    ReserveEReportSuccessFragment reserveEReportSuccessFragment;
    private Spinner spinner;
    private CustomEreportSpinnerAdapter adapter;
    private List<String> list = new ArrayList<>();
    private int rtype;
    public static String ereport_number;
    ProgressDialog progressDialog;

    public ReserveEReportFragment() {
        // Required empty public constructor
    }
    private static class SingletonHolder {
        public static final ReserveEReportFragment INSTANCE = new ReserveEReportFragment();
    }
    public static ReserveEReportFragment getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveEreportBinding.inflate(inflater);
        initView();
        controller();

        return binding.getRoot();
    }

    private void initView() {
        reserveEReportSuccessFragment = ReserveEReportSuccessFragment.getInstance();
        //????????? ?????? ??????
        progressDialog = new ProgressDialog(getContext());
        //???????????? ????????????
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    private void controller() {
        spinner = binding.reserveEreportSpinner;
        list.clear();

        // ????????? ?????? ?????? ????????? ?????? ??????
        list.add("?????????");
        list.add("????????????");
        list.add("?????????");
        list.add("??????");

        // ???????????? ?????? ????????? ?????????
        adapter = new CustomEreportSpinnerAdapter(getContext(), list);
        spinner.setAdapter(adapter);

        binding.reserveEreportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReserveFragment reserveFragment = ReserveFragment.getInstance();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveFragment).commit();
            }
        });

        binding.reserveEreportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                postEreportData(String.valueOf(rtype), binding.reserveEreportEdittext.getText().toString());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rtype = i + 1;
                Log.d("RType : ", String.valueOf(rtype));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void postEreportData(String rtype, String context) {
        ReservationEreportData reservationEreportData = new ReservationEreportData(LoginActivity.LOGIN_DATA, rtype, context);
        //Retrofit ??????
        Call<String> call = Retrofit_client.getApiService().reserveEreportPost(reservationEreportData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if(!response.isSuccessful()){
                        Log.e("?????????????????? ?????? - ????????? ???????????? : ", "error code : " + response.code());
                        Log.d("response : ", response.toString());
                        return;
                    }
                    String result = response.body();
                    Log.d("?????????????????? ?????? - ????????? ????????? : ", response.body());
                    // ????????? source??? JSONObject??? ????????????.
                    JSONObject jsonObj = new JSONObject(result);

                    ereport_number = jsonObj.get("response").toString();

                    Log.d("ereport_number : ", ereport_number);


                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                    fragmentTransaction.replace(R.id.fragmentFrame, reserveEReportSuccessFragment).commit();
                    binding.reserveEreportEdittext.setText(null);
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
                Log.e("?????????????????? - ????????????", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //????????? ????????? GC(Garbage Collector) ??? ???????????? ?????? ?????? ????????? ??????.
    }
}