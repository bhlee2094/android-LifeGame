package com.bhlee.myfirstgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements Button.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button btn_ability, btn_item;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){ //뒤로가기 클릭시 로그아웃
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder dlg = new AlertDialog.Builder(HomeActivity.this);
            dlg.setTitle("로그아웃"); //제목
            dlg.setMessage("로그아웃 하시겠습니까?"); // 메시지
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    logout(firebaseAuth);
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HomeActivity.this, "취소", Toast.LENGTH_SHORT).show();
                }
            });
            dlg.show();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeInit();
    }

    private void logout(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(HomeActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    private void HomeInit(){
        btn_ability = findViewById(R.id.btn_ability);
        btn_item = findViewById(R.id.btn_item);

        btn_ability.setOnClickListener(this);
        btn_item.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_home, new StartHomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ability:
                fragmentTransaction.replace(R.id.fragment_home, new AbilityFragment());
                fragmentTransaction.commit();
                break;
            case R.id.btn_item:
                fragmentTransaction.replace(R.id.fragment_home, new ItemFragment());
                fragmentTransaction.commit();
                break;
        }
    }
}