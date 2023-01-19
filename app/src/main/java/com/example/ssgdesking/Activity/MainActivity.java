package com.example.ssgdesking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssgdesking.Fragment.MainFragment;
import com.example.ssgdesking.Fragment.ReserveEReportFragment;
import com.example.ssgdesking.Fragment.ReserveFragment;
import com.example.ssgdesking.Fragment.ReserveInfoFragment;
import com.example.ssgdesking.Fragment.ReserveReportFragment;
import com.example.ssgdesking.Interface.onBackPressedListener;
import com.example.ssgdesking.R;
import com.example.ssgdesking.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MainFragment fragmentMain;
    private long lastTimeBackPressed;
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater()); //파라미터로 LayoutInflater객체 전달

        setContentView(binding.getRoot());

        initSideMenu();

        initFragment();
    }
    private void initSideMenu() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_sidemenu); //왼쪽 상단 버튼 아이콘 지정

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_reserve_seat:{ // 좌석 예약
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.item_reserve_classroom:{ // 회의실 예약
                        Intent myIntent = getPackageManager().getLaunchIntentForPackage("com.microsoft.teams");
                        startActivity(myIntent);
                        break;
                    }
                    case R.id.item_seatinfo:{ // 좌석배정현황
                        ReserveInfoFragment reserveInfoFragment = ReserveInfoFragment.getInstance();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentFrame, reserveInfoFragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.item_report_seat:{ // 좌석 신고
                        ReserveReportFragment reserveReportFragment = ReserveReportFragment.getInstance();
                        fragmentManager = getSupportFragmentManager();

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentFrame, reserveReportFragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.item_report_etc:{ // 불편사항 신고
                        ReserveEReportFragment reserveEReportFragment = ReserveEReportFragment.getInstance();
                        fragmentManager = getSupportFragmentManager();

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentFrame, reserveEReportFragment);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.item_message_developer:{ // 개발자에게 연락하기
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.setType("plain/text");
                        String[] address = {"slondy1012@gmail.com"};
                        email.putExtra(Intent.EXTRA_EMAIL, address);
                        email.putExtra(Intent.EXTRA_SUBJECT, "[I&C DESK 문의]");
                        email.putExtra(Intent.EXTRA_TEXT, "문의사항을 입력해주세요.");
                        startActivity(email);
                        break;
                    }
                    case R.id.item_logout:{ // 로그아웃
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private static class SingletonHolder {
        public static final MainActivity INSTANCE = new MainActivity();
    }
    public static MainActivity getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void initFragment() {
        fragmentMain = new MainFragment();

        //프래그먼트 매니저 획득
        fragmentManager = getSupportFragmentManager();

        //프래그먼트 Transaction 획득
        //프래그먼트를 올리거나 교체하는 작업을 Transaction이라고 합니다.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //프래그먼트를 FrameLayout의 자식으로 등록해줍니다.
        fragmentTransaction.add(R.id.fragmentFrame, fragmentMain);
        //commit을 하면 자식으로 등록된 프래그먼트가 화면에 보여집니다.
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //프래그먼트 onBackPressedListener사용
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            for(Fragment fragment : fragmentList){
                if(fragment instanceof onBackPressedListener){
                    ((onBackPressedListener)fragment).onBackPressed();
                    return;
                }
            }

            //두 번 클릭시 어플 종료
            if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
                finish();
                return;
            }
            lastTimeBackPressed = System.currentTimeMillis();
            Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();

        }


    }


}