package com.example.ssgdesking.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ssgdesking.Activity.LoginActivity;
import com.example.ssgdesking.Activity.MainActivity;
import com.example.ssgdesking.Activity.ReserveResultActivity;
import com.example.ssgdesking.Adapter.ReserveCommentAdapter;
import com.example.ssgdesking.Data.ReserveCommentData;
import com.example.ssgdesking.Fragment.ReserveFragment;
import com.example.ssgdesking.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ReserveBeforeDialog extends Dialog {
    protected Context mContext;
    private String title, seatID;
    private ArrayList<ReserveCommentData> data;
    ReserveCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_dialog_before);

        initView();

        initRecycler();

        getData();
    }

    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        adapter = new ReserveCommentAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        TextView seatnum = findViewById(R.id.seat_number);
        seatnum.setText(title);

        TextView cancelBtn = findViewById(R.id.reserve_another);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TextView reserveBtn = findViewById(R.id.reserve_btn);
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ReserveFragment.isReserve) {
                    Toast.makeText(getContext(), "이미 지정된 좌석이 있습니다.", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Intent intent = new Intent(getContext(), ReserveResultActivity.class);
                    intent.putExtra("info", title);
                    intent.putExtra("seatID", seatID);
                    getContext().startActivity(intent);
                    dismiss();
                }

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

    private void getData() {

        for (int i = 0; i < data.size(); i++) {
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data.get(i));
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

    public ReserveBeforeDialog(Context context, String title, ArrayList<ReserveCommentData> data, String seatID) {
        super( context );
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.title = title;
        this.data = data;
        this.seatID = seatID;
    }
}