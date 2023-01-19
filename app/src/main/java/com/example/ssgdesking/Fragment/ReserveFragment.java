package com.example.ssgdesking.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.example.ssgdesking.Data.ReserveInfoData;
import com.example.ssgdesking.Data.RetrofitSeatData;
import com.example.ssgdesking.Data.ReviewData;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.View.ReserveAfterDialog;
import com.example.ssgdesking.View.ReserveBeforeDialog;
import com.example.ssgdesking.View.SearchDialog;
import com.example.ssgdesking.databinding.FragmentReserveBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
    private ReserveInfoData reserveInfoData;
    private LoginData mLoginData;
    public static boolean isReserve = false;
    public static String MY_SEAT_ID;
    public static String reserve_title;
    ArrayList<ReservationDTO> items;
    ArrayList<ReservationDTO> viewItems;
    ArrayList<ReviewData> reviewItems;
    Call<RetrofitSeatData> call;
    private final String TAG = this.getClass().getSimpleName();
    private SearchDialog searchDialog;
    public static boolean isSearchClick = false;
    public static String SearchSection = "";
    public static String SearchLocation = "";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

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

        controller();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        initView();

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
                    URL url = new URL("http://43.201.16.249:8080/api/v1/reservation/19");

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
                                        isReserve = false;
                                        for (ReservationDTO dto : viewItems) {
                                            if (dto.getFixed().equals("true")) {
                                                setReserveColorPurple(dto.getSection(), dto.getLocation());
                                            } else if (dto.getOccupied().equals("true")) {
                                                setReserveColorGray(dto.getSection(), dto.getLocation());
                                            } else {
                                                setReserveColorRed(dto.getSection(), dto.getLocation());
                                            }
                                        }
                                        getUserData();
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

        list.clear();
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
        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        mainFragment = new MainFragment();
        searchDialog = new SearchDialog(getContext());

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog.show();
            }
        });

        searchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isSearchClick) {
                    isSearchClick = false;
                    setReserveColorGreen(SearchSection, SearchLocation);
                }
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
                section = "meritocracy";
                location = "1";
                break;
            }
            case R.id.floor_19_1_2:
            {
                startThread();
                binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "2";
                break;
            }
            case R.id.floor_19_1_3:
            {
                startThread();
                binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "3";
                break;
            }
            case R.id.floor_19_1_4:
            {
                startThread();
                binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "4";
                break;
            }
            case R.id.floor_19_1_5:
            {
                startThread();
                binding.floor1915.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "5";
                break;
            }
            case R.id.floor_19_1_6:
            {
                startThread();
                binding.floor1916.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "6";
                break;
            }
            case R.id.floor_19_1_7:
            {
                startThread();
                binding.floor1917.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "7";
                break;
            }
            case R.id.floor_19_1_8:
            {
                startThread();
                binding.floor1918.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "8";
                break;
            }
            case R.id.floor_19_1_9:
            {
                startThread();
                binding.floor1919.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "9";
                break;
            }
            case R.id.floor_19_1_10:
            {
                startThread();
                binding.floor19110.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "10";
                break;
            }
            case R.id.floor_19_1_11:
            {
                startThread();
                binding.floor19111.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
                location = "11";
                break;
            }
            case R.id.floor_19_1_12:
            {
                startThread();
                binding.floor19112.setBackgroundResource(R.drawable.reserve_seat_green);
                section = "meritocracy";
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
            URL url = new URL("http://43.201.16.249:8080/api/v1/reservation/19");

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

            for (ReservationDTO dto : items) {

                boolean isReserve = false;
                reserve_title = dto.getFloor() + "층 " + section + "-" + location;
                pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
                editor = pref.edit();
                editor.putString("RESERVE_SEAT_TITLE", reserve_title);
                editor.apply();
                Log.d("클릭 예약 좌석 정보 : ", reserve_title);
                if (dto.getSection().equals(section) && dto.getLocation().equals(location)) {
                    isReserve = true;
                    Log.d("좌석 예약여부 확인 : ", dto.getOccupied());
                }

                if (isReserve && dto.getOccupied().equals("true") && dto.getFixed().equals("false")) { // 좌석에 앉는 사람이 있울 시
                    getReserveData(dto.getId(), reserve_title, dto);
                } else if(isReserve && dto.getOccupied().equals("false") && dto.getFixed().equals("false")) { // 좌석이 비어있을 시
                    getReviewData(dto.getId(), reserve_title, dto);
                } else if (isReserve && dto.getFixed().equals("true")) {
                    Toast.makeText(getContext(), "고정 좌석입니다.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    setReserveColorPurple(dto.getSection(), dto.getLocation());
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

                    progressDialog.dismiss();
                    ReserveAfterDialog dialog = new ReserveAfterDialog(getContext(), reserve_title, retrofitSeatData.getDeptName(), retrofitSeatData.getName());
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (dto.getSection().equals(reserveInfoData.getSection()) &&
                                    dto.getLocation().equals(reserveInfoData.getLocation()) &&
                                    reserveInfoData.getOccupied().equals("true")) {
                                setReserveColorBlue(dto.getSection(), dto.getLocation());
                            } else {
                                setReserveColorGray(dto.getSection(), dto.getLocation());
                            }
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
        Call<String> call = Retrofit_client.getApiService().getEmpSeat(loginData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    Log.d("유저정보 ID : ", LoginActivity.LOGIN_DATA);
                    if(!response.isSuccessful()){
                        Log.e("유저정보 확인 - 연결이 비정상적 : ", "error code : " + response.code());
                        Log.d("response : ", response.toString());
                        return;
                    }
                    String result = response.body();
                    Log.d("유저정보 확인 - 연결이 성공적 : ", response.body().toString());

                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(result);

                    JSONObject row = (JSONObject) jsonObj.get("response");
                    reserveInfoData = new ReserveInfoData();
                    reserveInfoData.setId(row.getString("id"));
                    reserveInfoData.setFloor(row.getString("floor"));
                    reserveInfoData.setSection(row.getString("section"));
                    reserveInfoData.setLocation(row.getString("location"));
                    reserveInfoData.setOccupied(row.getString("occupied"));

                    Log.d("id : ", row.getString("id"));
                    Log.d("floor : ", row.getString("floor"));
                    Log.d("section : ", row.getString("section"));
                    Log.d("location : ", row.getString("location"));
                    Log.d("occupied : ", row.getString("occupied"));

                    for (ReservationDTO dto : viewItems) {
                        if (dto.getSection().equals(reserveInfoData.getSection()) &&
                                dto.getLocation().equals(reserveInfoData.getLocation())) {
                            if (reserveInfoData.getOccupied().equals("true")) {
                                setReserveColorBlue(dto.getSection(), dto.getLocation());
                                isReserve = true; // 내 예약 여부 확인
                                MY_SEAT_ID = dto.getId();
                            } else {
                                MY_SEAT_ID = dto.getId();
                            }

                        }
                    }

                    if (!isReserve) {
                        editor.putString("RESERVE_SEAT_INFO", "");
                        editor.putString("RESERVE_ENTER_TIME", "");
                        editor.putString("RESERVE_LEAVE_TIME", "");
                        editor.apply();
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

    private void getReviewData(String seatId, String reserve_title, ReservationDTO dto) {
        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().getReview(seatId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                reviewItems = new ArrayList<>();
                if(!response.isSuccessful()){
                    Log.e("리뷰 확인 - 연결이 비정상적 : ", "error code : " + response.code());
                    Log.d("response : ", response.toString());
                    return;
                }
                String result = response.body();
                Log.d("리뷰 확인 - 연결이 성공적 : ", response.body().toString());

                try {
                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jArray = (JSONArray) jsonObj.get("response");
                    items = new ArrayList<>();

                    Log.d("Json Length", "" + jArray.length());
                    reviewItems.clear();

                    ArrayList<String> listTitle = new ArrayList<>();
                    ArrayList<String> listContent = new ArrayList<>();

                    if (jArray.length() == 0) {
                        listTitle.add("알림");
                        listContent.add("작성된 리뷰가 없습니다.");
                    }

                    int index = 0;
                    for (int i=0; i < jArray.length(); i++) {
                        // 0번째 JSONObject를 받아옴
                        JSONObject row = jArray.getJSONObject(i);
                        ReviewData reviewData = new ReviewData();
                        reviewData.setId(row.getString("id"));
                        reviewData.setEmpid(row.getString("empid"));
                        reviewData.setSeatid(row.getString("seatid"));
                        reviewData.setContext(row.getString("context"));
                        reviewItems.add(reviewData);

                        Log.d("id : ", i + "번째 : " + row.getString("id"));
                        Log.d("empid : ", i + "번째 : " + row.getString("empid"));
                        Log.d("seatid : ", i + "번째 : " + row.getString("seatid"));
                        Log.d("context : ", i + "번째 : " + row.getString("context"));

                    }
                    if (!reviewItems.isEmpty() || reviewItems != null) {
                        for (ReviewData mReviewData : reviewItems) {
                            index ++;
                            listTitle.add("No. " + index);
                            listContent.add(mReviewData.getContext());
                        }
                    }

                    progressDialog.dismiss();
                    ReserveBeforeDialog dialog = new ReserveBeforeDialog(getContext(), reserve_title, addCommentData(listTitle, listContent), dto.getId());
                    dialog.show();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            setReserveColorRed(dto.getSection(), dto.getLocation());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("리뷰확인 - 연결실패", t.getMessage());
            }
        });
    }

    private void setReserveColorRed(String section, String location) {
        switch (section) {
            case "meritocracy":
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
                } else if (location.equals("13")) {
                    binding.floor19313.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19314.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("15")) {
                    binding.floor19315.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("16")) {
                    binding.floor19316.setBackgroundResource(R.drawable.reserve_seat_red);
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
                } else if (location.equals("13")) {
                    binding.floor19413.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19414.setBackgroundResource(R.drawable.reserve_seat_red);
                    break;
                }
            }

        }
    }

    public void setReserveColorGreen(String section, String location) {
        switch (section) {
            case "meritocracy":
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
                } else if (location.equals("13")) {
                    binding.floor19313.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19314.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("15")) {
                    binding.floor19315.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("16")) {
                    binding.floor19316.setBackgroundResource(R.drawable.reserve_seat_green);
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
                } else if (location.equals("13")) {
                    binding.floor19413.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19414.setBackgroundResource(R.drawable.reserve_seat_green);
                    break;
                }
            }

        }
    }


    private void setReserveColorBlue(String section, String location) {
        switch (section) {
            case "meritocracy":
            {
                if (location.equals("1")) {
                    binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1915.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1916.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1917.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1918.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1919.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19110.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19111.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19112.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                }
            }
            case "collaboration":
            {
                if (location.equals("1")) {
                    binding.floor1921.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1922.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1924.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1925.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1926.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1927.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1928.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1929.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19210.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19211.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19212.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                }
            }
            case "fun":
            {
                if (location.equals("1")) {
                    binding.floor1931.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1932.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1933.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1934.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1935.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1936.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1937.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1938.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1939.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19310.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19311.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19312.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("13")) {
                    binding.floor19313.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19314.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("15")) {
                    binding.floor19315.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("16")) {
                    binding.floor19316.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                }
            }
            case "innovation":
            {
                if (location.equals("1")) {
                    binding.floor1941.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1942.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1943.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1944.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1945.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1946.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1947.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1948.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1949.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19410.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19411.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19412.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("13")) {
                    binding.floor19413.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19414.setBackgroundResource(R.drawable.reserve_seat_blue);
                    break;
                }
            }

        }
    }

    private void setReserveColorPurple(String section, String location) {
        switch (section) {
            case "meritocracy":
            {
                if (location.equals("1")) {
                    binding.floor1911.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1912.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1913.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1914.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1915.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1916.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1917.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1918.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1919.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19110.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19111.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19112.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                }
            }
            case "collaboration":
            {
                if (location.equals("1")) {
                    binding.floor1921.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1922.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1923.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1924.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1925.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1926.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1927.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1928.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1929.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19210.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19211.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19212.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                }
            }
            case "fun":
            {
                if (location.equals("1")) {
                    binding.floor1931.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1932.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1933.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1934.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1935.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1936.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1937.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1938.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1939.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19310.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19311.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19312.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("13")) {
                    binding.floor19313.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19314.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("15")) {
                    binding.floor19315.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("16")) {
                    binding.floor19316.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                }
            }
            case "innovation":
            {
                if (location.equals("1")) {
                    binding.floor1941.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("2")) {
                    binding.floor1942.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("3")) {
                    binding.floor1943.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("4")) {
                    binding.floor1944.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("5")) {
                    binding.floor1945.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("6")) {
                    binding.floor1946.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("7")) {
                    binding.floor1947.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("8")) {
                    binding.floor1948.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("9")) {
                    binding.floor1949.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("10")) {
                    binding.floor19410.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("11")) {
                    binding.floor19411.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("12")) {
                    binding.floor19412.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("13")) {
                    binding.floor19413.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19414.setBackgroundResource(R.drawable.reserve_seat_purple);
                    break;
                }
            }

        }
    }

    private void setReserveColorGray(String section, String location) {
        switch (section) {
            case "meritocracy":
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
                } else if (location.equals("13")) {
                    binding.floor19313.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19314.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("15")) {
                    binding.floor19315.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("16")) {
                    binding.floor19316.setBackgroundResource(R.drawable.reserve_seat_gray);
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
                } else if (location.equals("13")) {
                    binding.floor19413.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                } else if (location.equals("14")) {
                    binding.floor19414.setBackgroundResource(R.drawable.reserve_seat_gray);
                    break;
                }
            }

        }
    }
}