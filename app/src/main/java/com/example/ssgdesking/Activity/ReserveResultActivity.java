package com.example.ssgdesking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.ssgdesking.Fragment.MainFragment;
import com.example.ssgdesking.Fragment.ReserveConfirmFragment;
import com.example.ssgdesking.R;

public class ReserveResultActivity extends AppCompatActivity {
    ReserveConfirmFragment reserveConfirmFragment;
    public static String reserveInfo, reserveSeatID;

    private static class SingletonHolder {
        public static final ReserveResultActivity INSTANCE = new ReserveResultActivity();
    }
    public static ReserveResultActivity getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_result);
        initFragment();
        Intent intent = getIntent();
        reserveInfo = intent.getStringExtra("info");
        reserveSeatID = intent.getStringExtra("seatID");
    }

    private void initFragment() {
        reserveConfirmFragment = new ReserveConfirmFragment();

        //프래그먼트 매니저 획득
        FragmentManager fragmentManager = getSupportFragmentManager();

        //프래그먼트 Transaction 획득
        //프래그먼트를 올리거나 교체하는 작업을 Transaction이라고 합니다.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //프래그먼트를 FrameLayout의 자식으로 등록해줍니다.
        fragmentTransaction.add(R.id.fragmentFrame, reserveConfirmFragment);
        //commit을 하면 자식으로 등록된 프래그먼트가 화면에 보여집니다.
        fragmentTransaction.commit();
    }
}