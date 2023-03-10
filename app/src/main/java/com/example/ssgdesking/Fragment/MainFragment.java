package com.example.ssgdesking.Fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssgdesking.Activity.MainActivity;
import com.example.ssgdesking.Adapter.SearchListAdapter;
import com.example.ssgdesking.Data.ReservationDTO;
import com.example.ssgdesking.Data.SearchData;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.FragmentMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    ArrayList<ReservationDTO> flooritems;
    MainActivity mainActivity;
    ReserveFragment reserveFragment;
    ProgressDialog progressDialog;
    private int reserve_count = 0;
    private FragmentManager fragmentManager;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentMainBinding.inflate(inflater);

        initView();
        controller();


        return binding.getRoot();
    }

    private void initView() {
        mainActivity = MainActivity.getInstance();
        reserveFragment = new ReserveFragment();
        progressDialog = new ProgressDialog(getContext());

        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getFloorData("19");
    }

    private void controller() {
        binding.reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveFragment).commit();
            }
        });

        binding.outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.reserveBefore.setVisibility(View.VISIBLE);
                binding.reserveAfter.setVisibility(View.GONE);
            }
        });

        binding.reserveClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.microsoft.teams");
                startActivity(myIntent);
            }
        });

        binding.reserveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReserveInfoFragment reserveInfoFragment = ReserveInfoFragment.getInstance();
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentFrame, reserveInfoFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void getFloorData(String floor) {
        //Retrofit ??????
        Call<String> call = Retrofit_client.getApiService().getFloorInfo(floor);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (!response.isSuccessful()) {
                        Log.e("??? ?????? ?????? - ????????? ???????????? : ", "error code : " + response.code());
                        Log.d("response : ", response.toString());
                        return;
                    }
                    String result = response.body();
                    Log.d("??? ?????? ?????? - ????????? ????????? : ", response.body());
                    // ????????? source??? JSONObject??? ????????????.
                    JSONObject jsonObj = new JSONObject(result);

                    JSONArray jArray = (JSONArray) jsonObj.get("response");
                    flooritems = new ArrayList<>();
                    flooritems.clear();

                    {
                        Log.d("Json Length", "" + jArray.length());

                        if (jArray.length() == 0) {
                            Toast.makeText(getContext(), "?????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < jArray.length(); i++) {
                            // 0?????? JSONObject??? ?????????
                            JSONObject row = jArray.getJSONObject(i);
                            ReservationDTO dto = new ReservationDTO();
                            dto.setId(row.getString("id"));
                            dto.setFloor(row.getString("floor"));
                            dto.setSection(row.getString("section"));
                            dto.setLocation(row.getString("location"));
                            dto.setFixed(row.getString("fixed"));
                            dto.setOccupied(row.getString("occupied"));
                            dto.setMonitor(row.getString("monitor"));
                            flooritems.add(dto);

                            Log.d("id : ", i + "?????? : " + row.getString("id"));
                            Log.d("floor : ", i + "?????? : " + row.getString("floor"));
                            Log.d("section : ", i + "?????? : " + row.getString("section"));
                            Log.d("location : ", i + "?????? : " + row.getString("location"));
                            Log.d("fixed : ", i + "?????? : " + row.getString("fixed"));
                            Log.d("occupied : ", i + "?????? : " + row.getString("occupied"));
                            Log.d("monitor : ", i + "?????? : " + row.getString("monitor"));

                            if (dto.getFixed().equals("true") || dto.getOccupied().equals("true")) {
                                reserve_count++;
                            }
                        }


                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                binding.reservePossible.setText((54-reserve_count) + "??? ????????????");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("??? ?????? - ????????????", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //????????? ????????? GC(Garbage Collector) ??? ???????????? ?????? ?????? ????????? ??????.
    }
}