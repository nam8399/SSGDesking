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

import okhttp3.CookieJar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    ProgressDialog progressDialog;

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
        Call<ReservationLoginData> call = Retrofit_client.getApiService().loginPost(reservationLoginData);
        call.enqueue(new Callback<ReservationLoginData>() {
            @Override
            public void onResponse(Call<ReservationLoginData> call, Response<ReservationLoginData> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    Toast.makeText(getApplicationContext(), "해당 계정 정보가 없습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                ReservationLoginData checkAlready = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "로그인 성공",Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(Call<ReservationLoginData> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("연결실패", t.getMessage());
                Toast.makeText(getApplicationContext(), "연결실패",Toast.LENGTH_SHORT).show();
            }
        });
    }
}