package com.bhlee.myfirstgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AbilityActivity extends AppCompatActivity implements Button.OnClickListener {

    private Button btn_close_ability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ability);

        btn_close_ability = findViewById(R.id.btn_close_ability);
        btn_close_ability.setOnClickListener(this);
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