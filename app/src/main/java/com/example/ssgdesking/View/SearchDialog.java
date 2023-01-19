package com.example.ssgdesking.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Adapter.SearchListAdapter;
import com.example.ssgdesking.Data.ReservationDTO;
import com.example.ssgdesking.Data.ReservationReportData;
import com.example.ssgdesking.Data.SearchData;
import com.example.ssgdesking.Fragment.ReserveFragment;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDialog extends Dialog {
    protected Context mContext;
    private String title, dept, name;
    private EditText editText;
    ProgressDialog progressDialog;
    private ArrayList<SearchData> searchItems;
    SearchListAdapter searchListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_dialog_search);

        controller();

        progressDialog = new ProgressDialog(getContext());

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        editText = findViewById(R.id.search_name);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE); //키보드 다음 버튼을 완료 버튼으로 바꿔줌
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    progressDialog.show();
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    getNameData(editText.getText().toString());
                    //키보드에 완료버튼을 누른 후 수행할 것
                    return true;
                }
                return false;
            }
        });

        setCancelable( true );
        setCanceledOnTouchOutside( true );

        Window window = getWindow();
        if( window != null ) {
            // 백그라운드 투명
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = window.getAttributes();
            // 화면에 가득 차도록
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;

            // 열기&닫기 시 애니메이션 설정
            params.windowAnimations = R.style.AnimationPopupStyle;
            window.setAttributes( params );
            // UI 하단 정렬
            window.setGravity( Gravity.BOTTOM );
        }
    }

    private void controller() {
        ImageView search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                getNameData(editText.getText().toString());
            }
        });

        ImageView canclebtn = findViewById(R.id.btn_dialog_cancel);
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void getNameData(String name) {
        //Retrofit 호출
        Call<String> call = Retrofit_client.getApiService().getNameInfo(name);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (!response.isSuccessful()) {
                        Log.e("검색정보 확인 - 연결이 비정상적 : ", "error code : " + response.code());
                        Log.d("response : ", response.toString());
                        return;
                    }
                    String result = response.body();
                    Log.d("검색정보 확인 - 연결이 성공적 : ", response.body());
                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(result);

                    JSONArray jArray = (JSONArray) jsonObj.get("response");
                    searchItems = new ArrayList<>();
                    searchItems.clear();

                    {
                        Log.d("Json Length", "" + jArray.length());

                        if (jArray.length() == 0) {
                            Toast.makeText(getContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < jArray.length(); i++) {
                            // 0번째 JSONObject를 받아옴
                            JSONObject row = jArray.getJSONObject(i);
                            SearchData dto = new SearchData();
                            dto.setEname(row.getString("ename"));
                            dto.setLocation(row.getString("location"));
                            dto.setSection(row.getString("section"));
                            dto.setSeatid(row.getString("seatid"));
                            dto.setId(row.getString("id"));
                            dto.setDname(row.getString("dname"));
                            dto.setFloor(row.getString("floor"));
                            searchItems.add(dto);

                            Log.d("id : ", i + "번째 : " + row.getString("ename"));
                            Log.d("floor : ", i + "번째 : " + row.getString("location"));
                            Log.d("section : ", i + "번째 : " + row.getString("section"));
                            Log.d("location : ", i + "번째 : " + row.getString("seatid"));
                            Log.d("fixed : ", i + "번째 : " + row.getString("id"));
                            Log.d("occupied : ", i + "번째 : " + row.getString("dname"));
                            Log.d("monitor : ", i + "번째 : " + row.getString("floor"));
                        }
                        searchListAdapter = new SearchListAdapter(getContext(), searchItems);
                        ListView listView = findViewById(R.id.search_listview);
                        listView.setAdapter(searchListAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.d("SearchDialog", "section : " +
                                        searchItems.get(i).getSection() +", location : " + searchItems.get(i).getLocation());
                                ReserveFragment.isSearchClick = true;
                                ReserveFragment.SearchSection = searchItems.get(i).getSection();
                                ReserveFragment.SearchLocation = searchItems.get(i).getLocation();
                                dismiss();
                            }
                        });


                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
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
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("검색정보 - 연결실패", t.getMessage());
            }
        });
    }

    public SearchDialog(Context context) {
        super( context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}