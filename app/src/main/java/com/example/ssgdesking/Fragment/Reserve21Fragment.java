package com.example.ssgdesking.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ssgdesking.Adapter.CustomSpinnerAdapter;
import com.example.ssgdesking.Data.ReservationDTO;
import com.example.ssgdesking.Data.RetrofitSeatData;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.FragmentReserve21Binding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class Reserve21Fragment extends Fragment implements onBackPressedListener {
    private FragmentReserve21Binding binding;
    MainFragment mainFragment;
    private ProgressDialog progressDialog;
    private boolean HTTP_OK = false;
    private boolean VIEW_HTTP_OK = false;
    private Handler handler;
    private String section = "";
    private String location = "";
    private RetrofitSeatData retrofitSeatData;
    ArrayList<ReservationDTO> items;
    ArrayList<ReservationDTO> viewItems;
    Call<RetrofitSeatData> call;
    private final String TAG = this.getClass().getSimpleName();

    private List<String> list = new ArrayList<>();
    private Spinner spinner;
    private CustomSpinnerAdapter adapter;


    public Reserve21Fragment() {
        // Required empty public constructor
    }

    private static class SingletonHolder {
        public static final Reserve21Fragment INSTANCE = new Reserve21Fragment();
    }

    public static Reserve21Fragment getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserve21Binding.inflate(inflater);
//        initView();
//
//        controller();
        initSpinner();

        return binding.getRoot();
    }


    private void initSpinner() {
        spinner = binding.spinner;
        ReserveFragment reserveFragment = new ReserveFragment();

        // ????????? ?????? ?????? ????????? ?????? ??????
        list.add("21F");
        list.add("19F");

        // ???????????? ?????? ????????? ?????????
        adapter = new CustomSpinnerAdapter(getContext(), list);
        spinner.setAdapter(adapter);

        // ????????? ?????? ?????????
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String otherItem = (String) spinner.getItemAtPosition(position);
//                Toast.makeText(getContext(), "????????? ????????? : " + otherItem, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "getItemAtPosition() - ????????? ????????? : " + otherItem);
                switch (otherItem) {
                    case "19F":{
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, reserveFragment).commit();
                        break;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }


    @Override
    public void onBackPressed() {
        goToMain();
    }

    //??????????????? ????????????
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, mainFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //????????? ????????? GC(Garbage Collector) ??? ???????????? ?????? ?????? ????????? ??????.
    }

}