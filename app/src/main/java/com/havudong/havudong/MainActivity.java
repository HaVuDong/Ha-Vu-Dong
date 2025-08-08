package com.havudong.havudong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnBack, btnSearch;
    TextView tvWelcome;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        tvWelcome = findViewById(R.id.tvWelcome);
        btnBack = findViewById(R.id.btnBack);


        // Nhận username từ Intent
        String username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            tvWelcome.setText("Chào mừng " + username + " đã đăng nhập");
        } else {
            tvWelcome.setText("Chào mừng!");
        }

        // Xử lý nút Đăng xuất
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // kết thúc MainActivity
        });

        // Xử lý tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                // Có thể chuyển sang trang kết quả tìm kiếm, hoặc gọi API
                Toast.makeText(MainActivity.this, "Tìm kiếm: " + keyword, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
