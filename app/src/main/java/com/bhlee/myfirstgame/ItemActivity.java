package com.bhlee.myfirstgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bhlee.myfirstgame.databinding.ActivityAbilityBinding;
import com.bhlee.myfirstgame.databinding.ActivityItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemActivity extends AppCompatActivity implements Button.OnClickListener {

    private String uid;
    private FirebaseFirestore db;
    private ActivityItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ItemInit();
    }

    private void ItemInit(){
        binding.btnCloseItem.setOnClickListener(this);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_close_item:
                Intent intent = new Intent(ItemActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
