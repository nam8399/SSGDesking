package com.example.ssgdesking.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssgdesking.R;

import org.w3c.dom.Text;

import java.util.Objects;

public class ReserveAfterDialog extends Dialog {
    protected Context mContext;
    private String title, dept, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_dialog_after);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        TextView seatnum = findViewById(R.id.seat_number);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView deptName_txt = findViewById(R.id.seat_deptname);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView name_txt = findViewById(R.id.seat_name);

        seatnum.setText(title);
        deptName_txt.setText(dept);
        name_txt.setText(name);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView cancelBtn = findViewById(R.id.btn_dialog_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

    public ReserveAfterDialog(Context context, String title, String dept, String name) {
        super( context );
        mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.title = title;
        this.dept = dept;
        this.name = name;
    }
}