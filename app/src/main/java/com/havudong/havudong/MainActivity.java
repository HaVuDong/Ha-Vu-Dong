package com.havudong.havudong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.tvWelcome);
        tv.setText("Chào mừng bạn đã đăng nhập thành công!");


        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Quay về LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Đóng MainActivity
        });
    }
}
