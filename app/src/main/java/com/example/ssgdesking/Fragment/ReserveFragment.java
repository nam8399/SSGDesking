package com.example.ssgdesking.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ssgdesking.Data.ReservationDTO;
import com.example.ssgdesking.Data.ReserveCommentData;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.View.ReserveAfterDialog;
import com.example.ssgdesking.View.ReserveBeforeDialog;
import com.example.ssgdesking.databinding.FragmentReserveBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReserveFragment extends Fragment implements onBackPressedListener, View.OnClickListener, Runnable {
    private FragmentReserveBinding binding;
    MainFragment mainFragment;
    private ProgressDialog progressDialog;
    private boolean HTTP_OK = false;
    private Handler handler;
    ArrayList<ReservationDTO> items;


    public ReserveFragment() {
        // Required empty public constructor
    }

    private static class SingletonHolder {
        public static final ReserveFragment INSTANCE = new ReserveFragment();
    }

    public static ReserveFragment getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReserveBinding.inflate(inflater);

        controller();

        return binding.getRoot();
    }

    private void controller() {
        mainFragment = new MainFragment();

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"검색 기능 클릭",Toast.LENGTH_SHORT).show();
            }
        });

        binding.floor1911.setOnClickListener(this);
        binding.floor1912.setOnClickListener(this);
        binding.floor1913.setOnClickListener(this);
        binding.floor1914.setOnClickListener(this);
        binding.floor1915.setOnClickListener(this);
        binding.floor1916.setOnClickListener(this);
        binding.floor1917.setOnClickListener(this);
        binding.floor1918.setOnClickListener(this);
        binding.floor1919.setOnClickListener(this);
        binding.floor1921.setOnClickListener(this);
        binding.floor1922.setOnClickListener(this);
        binding.floor1923.setOnClickListener(this);
        binding.floor1924.setOnClickListener(this);
        binding.floor1925.setOnClickListener(this);
        binding.floor1926.setOnClickListener(this);
        binding.floor1927.setOnClickListener(this);
        binding.floor1928.setOnClickListener(this);
        binding.floor1929.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.floor_19_1_1:
            {
                Thread th = new Thread(this);
                th.start();
                //로딩창 객체 생성
                progressDialog = new ProgressDialog(getContext());
                //로딩창을 투명하게
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.show();
                binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_green);
                break;
            }
            case R.id.floor_19_1_2:
            {
                binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-2");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
                break;
            }
            case R.id.floor_19_1_3:
            {
                binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-3");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
                break;
            }
            case R.id.floor_19_1_4:
            {
                binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "15층 1-4");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_gray);
                    }
                });
                break;
            }
            case R.id.floor_19_2_3:
            {
                // 임의의 데이터입니다.
                List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                List<String> listContent = Arrays.asList(
                        "이 자리 좋아요.",
                        "잠잘수 있는 최고의 자리.",
                        "시몬스 침대.",
                        "이 자리에 앉고 인생이 폈습니다."
                );

                binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_green);
                ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), "15층 2-3", addCommentData(listTitle, listContent));
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_red);
                    }
                });
            }

        }
    }

    private ArrayList<ReserveCommentData> addCommentData(List<String> listTitle, List<String> listContent) {
        ArrayList<ReserveCommentData> reserveCommentData = new ArrayList<>();

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            ReserveCommentData data = new ReserveCommentData();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            reserveCommentData.add(data);
        }

        return reserveCommentData;
    }

    @Override
    public void onBackPressed() {
        goToMain();
    }

    //프래그먼트 뒤로가기
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, mainFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기.
    }

    @Override
    public void run() {

        try {
            StringBuffer sb = new StringBuffer();
            URL url = new URL("http://192.168.189.32:80/api/v1/reservation/15");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 저 경로의 source를 받아온다.
            if (conn != null) {
                conn.setConnectTimeout(5000);
                conn.setUseCaches(false);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    while (true) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        sb.append(line + "\n");
                    }
                    Log.d("myLog", sb.toString());
                    HTTP_OK = true;
                    br.close();
                }
                conn.disconnect();
            }

            // 받아온 source를 JSONObject로 변환한다.
            JSONObject jsonObj = new JSONObject(sb.toString());
            JSONArray jArray = (JSONArray) jsonObj.get("response");
            items = new ArrayList<>();

            try {
                Log.d("Json Length", "" + jArray.length());

                for (int i=0; i < jArray.length(); i++) {
                    // 0번째 JSONObject를 받아옴
                    JSONObject row = jArray.getJSONObject(i);
                    ReservationDTO dto = new ReservationDTO();
                    dto.setId(row.getString("id"));
                    dto.setFloor(row.getString("floor"));
                    dto.setSection(row.getString("section"));
                    dto.setLocation(row.getString("location"));
                    dto.setFixed(row.getString("fixed"));
                    dto.setOccupied(row.getString("occupied"));
                    dto.setMonitor(row.getString("monitor"));
                    items.add(dto);

                    Log.d("id : ", i + "번째 : " + row.getString("id"));
                    Log.d("floor : ", i + "번째 : " + row.getString("floor"));
                    Log.d("section : ", i + "번째 : " + row.getString("section"));
                    Log.d("location : ", i + "번째 : " + row.getString("location"));
                    Log.d("fixed : ", i + "번째 : " + row.getString("fixed"));
                    Log.d("occupied : ", i + "번째 : " + row.getString("occupied"));
                    Log.d("monitor : ", i + "번째 : " + row.getString("monitor"));
                }

                handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       connectSetting();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
    }

    private void connectSetting() {
        if (HTTP_OK) {
            progressDialog.dismiss();
            ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), "19층 1-1");
            dialog.show();

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_gray);
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}