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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private Button btn_join, btn_login, btn_findPassword;
    private EditText Address, Password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        btn_join = findViewById(R.id.btn_join);
        btn_login = findViewById(R.id.btn_login);
        btn_findPassword = findViewById(R.id.btn_findPassword);
        Address = findViewById(R.id.editTextTextEmailAddress);
        Password = findViewById(R.id.editTextTextPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_join.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_findPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_join :
                JoinDialog joinDialog = new JoinDialog(MainActivity.this);
                joinDialog.show();
                break;
            case R.id.btn_login :
                if(Address.getText().toString().length() > 0 && Password.getText().toString().length() > 0){
                    firebaseAuth.signInWithEmailAndPassword(Address.getText().toString(), Password.getText().toString())
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

        private EditText join_email, join_password;
        private Button btn_ok, btn_cancle;
        private Context context;

        public JoinDialog(@NonNull Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_join);

            // 다이얼로그의 배경을 투명으로 만든다.
            Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            firebaseAuth = FirebaseAuth.getInstance();
            join_email = findViewById(R.id.join_email);
            join_password = findViewById(R.id.join_password);
            btn_ok = findViewById(R.id.btn_ok);
            btn_cancle = findViewById(R.id.btn_cancle);

            btn_ok.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp();
                }
            });

            btn_cancle.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        private void signUp(){
            String email = join_email.getText().toString();
            String password = join_password.getText().toString();

            if(email.length() > 0 && password.length() >= 6){
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
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

        private EditText findPassword_email;
        private Button btn_ok2, btn_cancle2;
        private Context context;

        public FindPasswordDialog(@NonNull Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_findpassword);

            // 다이얼로그의 배경을 투명으로 만든다.
            Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            firebaseAuth = FirebaseAuth.getInstance();
            findPassword_email = findViewById(R.id.findPassword_email);
            btn_ok2 = findViewById(R.id.btn_ok2);
            btn_cancle2 = findViewById(R.id.btn_cancle2);

            btn_ok2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendPasswordResetEmail();
                }
            });

            btn_cancle2.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        private void sendPasswordResetEmail(){
            String emailAddress = findPassword_email.getText().toString();
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

}