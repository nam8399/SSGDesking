package com.example.ssgdesking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ssgdesking.Data.ReservationLoginData;
import com.example.ssgdesking.R;
import com.example.ssgdesking.Retrofit.Retrofit_client;
import com.example.ssgdesking.View.ProgressDialog;
import com.example.ssgdesking.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    public static String LOGIN_DATA;
    public static String USER_NAME;
    public static String USER_EMPNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater()); //파라미터로 LayoutInflater객체 전달

        setContentView(binding.getRoot());

        controller();


    }

    private void controller() {
        //로딩창 객체 생성
        progressDialog = new ProgressDialog(this);
        //로딩창을 투명하게
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("로그인 파라미터 : ", "아이디 : " + binding.inputId.getText().toString() + " 비밀번호 : " + binding.inputPw.getText().toString());
                LoginPost(binding.inputId.getText().toString(), binding.inputPw.getText().toString());
            }
        });
    }

    public void LoginPost(String empno, String password){
        progressDialog.show();

        //Retrofit 호출
        ReservationLoginData reservationLoginData = new ReservationLoginData(empno, password);
        Call<String> call = Retrofit_client.getApiService().loginPost(reservationLoginData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    progressDialog.dismiss();
                    if(!response.isSuccessful()){
                        Log.e("로그인 정보 - 연결이 비정상적 : ", "error code : " + response.code());
                        Toast.makeText(getApplicationContext(), "해당 계정 정보가 없습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String result = response.body().toString(); // 로그인 응답값
                    Log.e("로그인 정보 - 연결이 성공적 : ", result);

                    // 받아온 source를 JSONObject로 변환한다.
                    JSONObject jsonObj = new JSONObject(result);

                    JSONObject row = (JSONObject) jsonObj.get("response");
                    LOGIN_DATA = row.getString("id");
                    USER_NAME = row.getString("name");
                    USER_EMPNO = row.getString("empno");

                    Log.d("id : ", row.getString("id"));
                    Log.d("name : ", row.getString("name"));
                    Log.d("empno : ", row.getString("empno"));

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "로그인 성공",Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("연결실패", t.getMessage());
                Toast.makeText(getApplicationContext(), "서버 연결상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}