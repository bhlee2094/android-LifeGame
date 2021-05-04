package com.bhlee.myfirstgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.bhlee.myfirstgame.databinding.ActivityItemBinding;
import com.bhlee.myfirstgame.databinding.ActivityMainBinding;
import com.bhlee.myfirstgame.databinding.DialogFindpasswordBinding;
import com.bhlee.myfirstgame.databinding.DialogJoinBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference usersReference;
    private ActivityMainBinding binding;
    static public boolean musicKey = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init(){
        if(musicKey){
            binding.switchMusic1.setChecked(true);
            startService(new Intent(getApplicationContext(), MusicService.class));
        } else{
            binding.switchMusic1.setChecked(false);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnJoin.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.btnFindPassword.setOnClickListener(this);
        binding.switchMusic1.setOnCheckedChangeListener(new musicSwitchListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_join :
                JoinDialog joinDialog = new JoinDialog(MainActivity.this);
                joinDialog.show();
                break;
            case R.id.btn_login :
                if(binding.editTextTextEmailAddress.getText().toString().length() > 0 && binding.editTextTextPassword.getText().toString().length() > 0){
                    firebaseAuth.signInWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString(), binding.editTextTextPassword.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.btn_findPassword :
                FindPasswordDialog findPasswordDialog = new FindPasswordDialog(MainActivity.this);
                findPasswordDialog.show();
                break;
        }
    }

    public class JoinDialog extends Dialog { //회원가입 다이어로그

        private Context context;
        private DialogJoinBinding joinBinding;

        public JoinDialog(@NonNull Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            joinBinding = DialogJoinBinding.inflate(getLayoutInflater());
            setContentView(joinBinding.getRoot());

            // 다이얼로그의 배경을 투명으로 만든다.
            Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            firebaseAuth = FirebaseAuth.getInstance();

            joinBinding.btnOk.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp();
                }
            });

            joinBinding.btnCancle.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        private void signUp(){
            String email = joinBinding.joinEmail.getText().toString();
            String password = joinBinding.joinPassword.getText().toString();

            if(email.length() > 0 && password.length() >= 6){
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    db = FirebaseFirestore.getInstance();
                                    usersReference = db.collection("users").document(firebaseAuth.getCurrentUser().getUid());
                                    UsersDTO usersDTO = new UsersDTO(0,10,1,1); // 회원가입시 초기 설정
                                    usersReference.set(usersDTO)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MainActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    dismiss();
                                }else{
                                    Toast.makeText(MainActivity.this, "email 형식으로 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{
                Toast.makeText(MainActivity.this, "password 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        }
    } //회원가입 다이어로그 끝

    public class FindPasswordDialog extends Dialog { //비밀번호찾기 다이어로그

        private Context context;
        private DialogFindpasswordBinding findpasswordBinding;

        public FindPasswordDialog(@NonNull Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            findpasswordBinding = DialogFindpasswordBinding.inflate(getLayoutInflater());
            setContentView(findpasswordBinding.getRoot());

            // 다이얼로그의 배경을 투명으로 만든다.
            Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            firebaseAuth = FirebaseAuth.getInstance();

            findpasswordBinding.btnOk2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPasswordResetEmail();
                }
            });

            findpasswordBinding.btnCancle2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        private void sendPasswordResetEmail(){
            String emailAddress = findpasswordBinding.findPasswordEmail.getText().toString();
            firebaseAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "email이 전송되었습니다", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }else{
                                Toast.makeText(MainActivity.this, "존재하지 않는 email 입니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    } //비밀번호찾기 다이어로그 끝

    class musicSwitchListener implements CompoundButton.OnCheckedChangeListener{ //음악 스위치

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                startService(new Intent(getApplicationContext(), MusicService.class));
                musicKey = true;
            }
            else {
                stopService(new Intent(getApplicationContext(), MusicService.class));
                musicKey = false;
            }

        }
    }
}