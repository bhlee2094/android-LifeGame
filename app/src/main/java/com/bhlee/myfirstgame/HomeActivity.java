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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bhlee.myfirstgame.databinding.ActivityHomeBinding;
import com.bhlee.myfirstgame.databinding.ActivityItemBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements Button.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ActivityHomeBinding binding;

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
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        HomeInit();
    }

    private void logout(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(HomeActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    private void HomeInit(){

        if(MainActivity.musicKey) {
            binding.switchMusic.setChecked(true);
        }
        else {
            binding.switchMusic.setChecked(false);
        }

        binding.btnAbility.setOnClickListener(this);
        binding.btnItem.setOnClickListener(this);
        binding.switchMusic.setOnCheckedChangeListener(new musicSwitchListener());

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_home, new StartHomeFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ability:
                Intent intent = new Intent(HomeActivity.this, AbilityActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_item:
                intent = new Intent(HomeActivity.this, ItemActivity.class);
                startActivity(intent);
                break;
        }
    }

    class musicSwitchListener implements CompoundButton.OnCheckedChangeListener{ //음악 스위치

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                startService(new Intent(getApplicationContext(), MusicService.class));
                MainActivity.musicKey = true;
            }
            else {
                stopService(new Intent(getApplicationContext(), MusicService.class));
                MainActivity.musicKey = false;
            }

        }
    }
}