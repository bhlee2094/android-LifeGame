package com.bhlee.myfirstgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bhlee.myfirstgame.databinding.ActivityAbilityBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class AbilityActivity extends AppCompatActivity implements Button.OnClickListener {

    private String uid;
    private FirebaseFirestore db;
    private ActivityAbilityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbilityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AbilityInit();
    }

    private void AbilityInit(){
        binding.btnCloseAbility.setOnClickListener(this);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close_ability:
                Intent intent = new Intent(AbilityActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
