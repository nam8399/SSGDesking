package com.example.ssgdesking.Fragment;

import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Adapter.CustomSpinnerAdapter;
import com.example.ssgdesking.Data.LoginData;
import com.example.ssgdesking.Data.ReservationDTO;
import com.example.ssgdesking.Data.ReserveCommentData;
import com.example.ssgdesking.Data.RetrofitSeatData;
import com.example.ssgdesking.Data.ReviewData;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.View.ReserveAfterDialog;
import com.example.ssgdesking.View.ReserveBeforeDialog;
import com.example.ssgdesking.databinding.FragmentReserveBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveFragment extends Fragment implements onBackPressedListener, View.OnClickListener, Runnable {
    private FragmentReserveBinding binding;
    MainFragment mainFragment;
    Reserve21Fragment reserve21Fragment;
    private ProgressDialog progressDialog;
    private boolean HTTP_OK = false;
    private boolean VIEW_HTTP_OK = false;
    private Handler handler;
    private String section = "";
    private String location = "";
    private RetrofitSeatData retrofitSeatData;
    private LoginData mLoginData;
    ArrayList<ReservationDTO> items;
    ArrayList<ReservationDTO> viewItems;
    ArrayList<ReviewData> reviewItems;
    Call<RetrofitSeatData> call;
    private final String TAG = this.getClass().getSimpleName();

    private List<String> list = new ArrayList<>();
    private Spinner spinner;
    private CustomSpinnerAdapter adapter;


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
        getUserData();

        initView();

        controller();

        return binding.getRoot();
    }

    private void initView() {
        reserve21Fragment = new Reserve21Fragment();

        initSpinner();

        Handler progressHandler = new Handler(Looper.getMainLooper());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Thread viewThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    StringBuffer sb = new StringBuffer();
                    URL url = new URL("http://192.168.189.32:80/api/v1/reservation/19");

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
                            VIEW_HTTP_OK = true;
                            br.close();
                        } else {
                            Toast.makeText(getContext(), "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        conn.disconnect();
                    }

                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(sb.toString());
                    JSONArray jArray = (JSONArray) jsonObj.get("response");
                    viewItems = new ArrayList<>();

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
                            viewItems.add(dto);

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
                                try {
                                    if (VIEW_HTTP_OK) {

                                        for (ReservationDTO dto : viewItems) {
                                            if (dto.getOccupied().equals("true")) {
                                                setReserveColorGray(dto.getSection(), dto.getLocation());
                                            } else {
                                                setReserveColorRed(dto.getSection(), dto.getLocation());
                                            }
                                        }
                                        progressHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }catch (Exception e){
                    progressHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "서버 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Log.e("error", e.getMessage());
                            onBackPressed();
                        }
                    });
                }
            }
        });

        viewThread.start();
    }

    private void initSpinner() {
        spinner = binding.spinner;

        // 스피너 안에 넣을 데이터 임의 생성
        list.add("19F");
        list.add("21F");

        // 스피너에 붙일 어댑터 초기화
        adapter = new CustomSpinnerAdapter(getContext(), list);
        spinner.setAdapter(adapter);

        // 스피너 클릭 리스너
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String otherItem = (String) spinner.getItemAtPosition(position);
//                Toast.makeText(getContext(), "선택한 아이템 : " + otherItem, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "getItemAtPosition() - 선택한 아이템 : " + otherItem);
                switch (otherItem) {
                    case "21F":{
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragmentFrame, reserve21Fragment).commit();
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

    private void controller() {
        mainFragment = new MainFragment();

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"검색 기능 클릭",Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initView();
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
        binding.floor19110.setOnClickListener(this);
        binding.floor19111.setOnClickListener(this);
        binding.floor19112.setOnClickListener(this);
        binding.floor1921.setOnClickListener(this);
        binding.floor1922.setOnClickListener(this);
        binding.floor1923.setOnClickListener(this);
        binding.floor1924.setOnClickListener(this);
        binding.floor1925.setOnClickListener(this);
        binding.floor1926.setOnClickListener(this);
        binding.floor1927.setOnClickListener(this);
        binding.floor1928.setOnClickListener(this);
        binding.floor1929.setOnClickListener(this);
        binding.floor19210.setOnClickListener(this);
        binding.floor19211.setOnClickListener(this);
        binding.floor19212.setOnClickListener(this);
        binding.floor1931.setOnClickListener(this);
        binding.floor1932.setOnClickListener(this);
        binding.floor1933.setOnClickListener(this);
        binding.floor1934.setOnClickListener(this);
        binding.floor1935.setOnClickListener(this);
        binding.floor1936.setOnClickListener(this);
        binding.floor1937.setOnClickListener(this);
        binding.floor1938.setOnClickListener(this);
        binding.floor1939.setOnClickListener(this);
        binding.floor19310.setOnClickListener(this);
        binding.floor19311.setOnClickListener(this);
        binding.floor19312.setOnClickListener(this);
        binding.floor19313.setOnClickListener(this);
        binding.floor19314.setOnClickListener(this);
        binding.floor19315.setOnClickListener(this);
        binding.floor19316.setOnClickListener(this);
        binding.floor1941.setOnClickListener(this);
        binding.floor1942.setOnClickListener(this);
        binding.floor1943.setOnClickListener(this);
        binding.floor1944.setOnClickListener(this);
        binding.floor1945.setOnClickListener(this);
        binding.floor1946.setOnClickListener(this);
        binding.floor1947.setOnClickListener(this);
        binding.floor1948.setOnClickListener(this);
        binding.floor1949.setOnClickListener(this);
        binding.floor19410.setOnClickListener(this);
        binding.floor19411.setOnClickListener(this);
        binding.floor19412.setOnClickListener(this);
        binding.floor19413.setOnClickListener(this);
        binding.floor19414.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.floor_19_1_1:
            {
                startThread();
                binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "1";
                break;
            }
            case R.id.floor_19_1_2:
            {
                startThread();
                binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "2";
                break;
            }
            case R.id.floor_19_1_3:
            {
                startThread();
                binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "3";
                break;
            }
            case R.id.floor_19_1_4:
            {
                startThread();
                binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "4";
                break;
            }
            case R.id.floor_19_1_5:
            {
                startThread();
                binding.floor1915.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "5";
                break;
            }
            case R.id.floor_19_1_6:
            {
                startThread();
                binding.floor1916.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "6";
                break;
            }
            case R.id.floor_19_1_7:
            {
                startThread();
                binding.floor1917.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "7";
                break;
            }
            case R.id.floor_19_1_8:
            {
                startThread();
                binding.floor1918.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "8";
                break;
            }
            case R.id.floor_19_1_9:
            {
                startThread();
                binding.floor1919.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "9";
                break;
            }
            case R.id.floor_19_1_10:
            {
                startThread();
                binding.floor19110.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "10";
                break;
            }
            case R.id.floor_19_1_11:
            {
                startThread();
                binding.floor19111.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "11";
                break;
            }
            case R.id.floor_19_1_12:
            {
                startThread();
                binding.floor19112.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "challenge";
                location = "12";
                break;
            }

            case R.id.floor_19_2_1:
            {
                startThread();
                binding.floor1921.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "1";
                break;
            }

            case R.id.floor_19_2_2:
            {
                startThread();
                binding.floor1922.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "2";
                break;
            }

            case R.id.floor_19_2_3:
            {
                startThread();
                binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "3";
                break;
            }

            case R.id.floor_19_2_4:
            {
                startThread();
                binding.floor1924.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "4";
                break;
            }

            case R.id.floor_19_2_5:
            {
                startThread();
                binding.floor1925.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "5";
                break;
            }

            case R.id.floor_19_2_6:
            {
                startThread();
                binding.floor1926.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "6";
                break;
            }

            case R.id.floor_19_2_7:
            {
                startThread();
                binding.floor1927.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "7";
                break;
            }

            case R.id.floor_19_2_8:
            {
                startThread();
                binding.floor1928.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "8";
                break;
            }

            case R.id.floor_19_2_9:
            {
                startThread();
                binding.floor1929.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "9";
                break;
            }
            case R.id.floor_19_2_10:
            {
                startThread();
                binding.floor19210.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "10";
                break;
            }
            case R.id.floor_19_2_11:
            {
                startThread();
                binding.floor19211.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "11";
                break;
            }
            case R.id.floor_19_2_12:
            {
                startThread();
                binding.floor19212.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "collaboration";
                location = "12";
                break;
            }
            case R.id.floor_19_3_1:
            {
                startThread();
                binding.floor1931.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "1";
                break;
            }
            case R.id.floor_19_3_2:
            {
                startThread();
                binding.floor1932.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "2";
                break;
            }
            case R.id.floor_19_3_3:
            {
                startThread();
                binding.floor1933.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "3";
                break;
            }
            case R.id.floor_19_3_4:
            {
                startThread();
                binding.floor1934.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "4";
                break;
            }
            case R.id.floor_19_3_5:
            {
                startThread();
                binding.floor1935.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "5";
                break;
            }
            case R.id.floor_19_3_6:
            {
                startThread();
                binding.floor1936.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "6";
                break;
            }
            case R.id.floor_19_3_7:
            {
                startThread();
                binding.floor1937.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "7";
                break;
            }
            case R.id.floor_19_3_8:
            {
                startThread();
                binding.floor1938.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "8";
                break;
            }
            case R.id.floor_19_3_9:
            {
                startThread();
                binding.floor1939.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "9";
                break;
            }
            case R.id.floor_19_3_10:
            {
                startThread();
                binding.floor19310.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "10";
                break;
            }
            case R.id.floor_19_3_11:
            {
                startThread();
                binding.floor19311.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "11";
                break;
            }
            case R.id.floor_19_3_12:
            {
                startThread();
                binding.floor19312.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "12";
                break;
            }
            case R.id.floor_19_3_13:
            {
                startThread();
                binding.floor19313.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "13";
                break;
            }
            case R.id.floor_19_3_14:
            {
                startThread();
                binding.floor19314.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "14";
                break;
            }
            case R.id.floor_19_3_15:
            {
                startThread();
                binding.floor19315.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "15";
                break;
            }
            case R.id.floor_19_3_16:
            {
                startThread();
                binding.floor19316.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "fun";
                location = "16";
                break;
            }
            case R.id.floor_19_4_1:
            {
                startThread();
                binding.floor1941.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "1";
                break;
            }
            case R.id.floor_19_4_2:
            {
                startThread();
                binding.floor1942.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "2";
                break;
            }
            case R.id.floor_19_4_3:
            {
                startThread();
                binding.floor1943.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "3";
                break;
            }
            case R.id.floor_19_4_4:
            {
                startThread();
                binding.floor1944.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "4";
                break;
            }
            case R.id.floor_19_4_5:
            {
                startThread();
                binding.floor1945.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "5";
                break;
            }
            case R.id.floor_19_4_6:
            {
                startThread();
                binding.floor1946.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "6";
                break;
            }
            case R.id.floor_19_4_7:
            {
                startThread();
                binding.floor1947.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "7";
                break;
            }
            case R.id.floor_19_4_8:
            {
                startThread();
                binding.floor1948.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "8";
                break;
            }
            case R.id.floor_19_4_9:
            {
                startThread();
                binding.floor1949.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "9";
                break;
            }
            case R.id.floor_19_4_10:
            {
                startThread();
                binding.floor19410.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "10";
                break;
            }
            case R.id.floor_19_4_11:
            {
                startThread();
                binding.floor19411.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "11";
                break;
            }
            case R.id.floor_19_4_12:
            {
                startThread();
                binding.floor19412.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "12";
                break;
            }
            case R.id.floor_19_4_13:
            {
                startThread();
                binding.floor19413.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "13";
                break;
            }
            case R.id.floor_19_4_14:
            {
                startThread();
                binding.floor19414.setBackgroundResource(R.drawable.reserve_seat_green);

                section = "innovation";
                location = "14";
                break;
            }

        }
    }

    private void startThread() {
        Thread th = new Thread(this);
        th.start();
        //로딩창 객체 생성
        progressDialog = new ProgressDialog(getContext());
        //로딩창을 투명하게
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
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
            URL url = new URL("http://192.168.189.32:80/api/v1/reservation/19");

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
                    Log.d("Response 값 : ", sb.toString());
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
                        try {
                            connectSetting();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private void connectSetting() { // 좌석 클릭 시 처리
        if (HTTP_OK) {
            progressDialog.dismiss();

            for (ReservationDTO dto : items) {

                boolean isReserve = false;
                String reserve_title = dto.getFloor() + "층 " + section + "-" + location;
                Log.d("클릭 예약 좌석 정보 : ", reserve_title);
                if (dto.getSection().equals(section) && dto.getLocation().equals(location)) {
                    isReserve = true;
                    Log.d("좌석 예약여부 확인 : ", dto.getOccupied());
                }

                if (isReserve && dto.getOccupied().equals("true")) { // 좌석에 앉는 사람이 있울 시
                    getReserveData(dto.getId(), reserve_title, dto);
                } else if(isReserve && dto.getOccupied().equals("false")) { // 좌석이 비어있을 시
//                    getReviewData(dto.getId());
                    // 임의의 데이터입니다.
                    List<String> listTitle = Arrays.asList("No. 0001", "No. 0002", "No. 0003", "No. 0004");
                    List<String> listContent = Arrays.asList(
                            "이 자리 좋아요.",
                            "잠잘수 있는 최고의 자리.",
                            "시몬스 침대.",
                            "이 자리에 앉고 인생이 폈습니다."
                    );

                    ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), reserve_title, addCommentData(listTitle, listContent), dto.getId());
                    dialog.show();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            setReserveColorRed(dto.getSection(), dto.getLocation());
                        }
                    });
                }
            }


        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getReserveData(String id, String reserve_title, ReservationDTO dto) {
        Call<String> call_str = Retrofit_client.getApiService().getPostsString(id);
        call_str.enqueue(new Callback<String>() {
            //콜백 받는 부분
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String result = response.body().toString(); // 로그인 응답값
                    Log.d("Response Result : ", result);

                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(result);

                    JSONObject row = (JSONObject) jsonObj.get("response");
                    retrofitSeatData = new RetrofitSeatData();
                    retrofitSeatData.setDeptid(row.getString("deptid"));
                    retrofitSeatData.setDeptName(row.getString("deptName"));
                    retrofitSeatData.setEmpno(row.getString("empno"));
                    retrofitSeatData.setName(row.getString("name"));

                    Log.d("deptid : ", row.getString("deptid"));
                    Log.d("deptName : ", row.getString("deptName"));
                    Log.d("empno : ", row.getString("empno"));
                    Log.d("name : ", row.getString("name"));

                    ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), reserve_title, retrofitSeatData.getDeptName(), retrofitSeatData.getName());
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            setReserveColorGray(dto.getSection(), dto.getLocation());
                        }
                    });

                } catch (NullPointerException e) {
                    Toast.makeText(getContext(), "서버 응답값이 없습니다.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "서버 통신 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData() {
        LoginData loginData = new LoginData(LoginActivity.LOGIN_DATA);
        Log.d("유저정보 Id : ", LoginActivity.LOGIN_DATA);
        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().getEmpInfo(loginData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("유저정보 ID : ", LoginActivity.LOGIN_DATA);
                if(!response.isSuccessful()){
                    Log.e("유저정보 확인 - 연결이 비정상적 : ", "error code : " + response.code());
                    Log.d("response : ", response.toString());
                    return;
                }
                String checkAlready = response.body();
                Log.d("유저정보 확인 - 연결이 성공적 : ", response.body().toString());

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
    }

    private void getReviewData(String seatId) {
        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().getReview(seatId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){
                    Log.e("유저정보 확인 - 연결이 비정상적 : ", "error code : " + response.code());
                    Log.d("response : ", response.toString());
                    return;
                }
                String result = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());

                try {
                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jArray = (JSONArray) jsonObj.get("response");
                    items = new ArrayList<>();

                    Log.d("Json Length", "" + jArray.length());

                    for (int i=0; i < jArray.length(); i++) {
                        // 0번째 JSONObject를 받아옴
                        JSONObject row = jArray.getJSONObject(i);
                        ReviewData reviewData = new ReviewData();
//                        reviewData.setMessageId(row.getInt("id"));
//                        reviewData.setFlowerId(row.getInt("flowerId"));
//                        reviewData.setImgUrl(row.getString("imgUrl"));
//                        reviewData.setContent(row.getString("content"));
//                        reviewData.setWriter(row.getString("writer"));
//                        reviewData.setFont(row.getInt("font"));
//                        reviewItems.add(reviewData);

//                        Log.d("id : ", i + "번째 : " + row.getString("id"));
//                        Log.d("flowerId : ", i + "번째 : " + row.getString("flowerId"));
//                        Log.d("imgUrl : ", i + "번째 : " + row.getString("imgUrl"));
//                        Log.d("content : ", i + "번째 : " + row.getString("content"));
//                        Log.d("writer : ", i + "번째 : " + row.getString("writer"));
//                        Log.d("font : ", i + "번째 : " + row.getString("font"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
    }

    private void setReserveColorRed(String section, String location) {
        switch (section) {
            case "challenge":
            {
                if (location.equals("1")) {
                    binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1915.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1916.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1917.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1918.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1919.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19110.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19111.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19112.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                }
            }
            case "collaboration":
            {
                if (location.equals("1")) {
                    binding.floor1921.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1922.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1924.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1925.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1926.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1927.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1928.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1929.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19210.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19211.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19212.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                }
            }
            case "fun":
            {
                if (location.equals("1")) {
                    binding.floor1931.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1932.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1933.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1934.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1935.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1936.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1937.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1938.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1939.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19310.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19311.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19312.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                }
            }
            case "innovation":
            {
                if (location.equals("1")) {
                    binding.floor1941.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1942.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1943.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1944.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1945.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1946.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1947.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1948.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1949.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19410.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19411.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19412.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                }
            }

        }
    }

    private void setReserveColorGreen(String section, String location) {
        switch (section) {
            case "challenge":
            {
                if (location.equals("1")) {
                    binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1915.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1916.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1917.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1918.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1919.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19110.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19111.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19112.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                }
            }
            case "collaboration":
            {
                if (location.equals("1")) {
                    binding.floor1921.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1922.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1924.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1925.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1926.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1927.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1928.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1929.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19210.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19211.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19212.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                }
            }
            case "fun":
            {
                if (location.equals("1")) {
                    binding.floor1931.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1932.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1933.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1934.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1935.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1936.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1937.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1938.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1939.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19310.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19311.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19312.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                }
            }
            case "innovation":
            {
                if (location.equals("1")) {
                    binding.floor1941.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1942.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1943.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1944.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1945.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1946.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1947.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1948.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1949.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19410.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19411.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19412.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                }
            }

        }
    }

    private void setReserveColorGray(String section, String location) {
        switch (section) {
            case "challenge":
            {
                if (location.equals("1")) {
                    binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1915.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1916.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1917.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1918.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1919.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19110.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19111.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19112.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                }
            }
            case "collaboration":
            {
                if (location.equals("1")) {
                    binding.floor1921.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1922.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1924.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1925.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1926.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1927.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1928.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1929.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19210.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19211.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19212.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                }
            }
            case "fun":
            {
                if (location.equals("1")) {
                    binding.floor1931.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1932.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1933.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1934.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1935.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1936.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1937.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1938.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1939.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19310.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19311.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19312.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                }
            }
            case "innovation":
            {
                if (location.equals("1")) {
                    binding.floor1941.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1942.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1943.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1944.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1945.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1946.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1947.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1948.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1949.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19410.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19411.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19412.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                }
            }

        }
    }
}