package com.bhlee.myfirstgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemActivity extends AppCompatActivity implements Button.OnClickListener {

    private Button btn_close_item;
    private String uid;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ItemInit();
    }

    private void ItemInit(){
        btn_close_item = findViewById(R.id.btn_close_item);
        btn_close_item.setOnClickListener(this);

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
