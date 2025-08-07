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
        setContentView(R.layout.activity_main); // layout XML bạn đã gửi trước

        // Ánh xạ các thành phần từ layout
        tvWelcome = findViewById(R.id.tvWelcome);
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);

        // Lấy dữ liệu username từ Intent
        String username = getIntent().getStringExtra("username");

        if (username != null) {
            tvWelcome.setText("Chào mừng " + username + " đã đăng nhập");
        }

        // Xử lý nút Đăng xuất
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý nút Tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                // Xử lý tìm kiếm ở đây, ví dụ:
                Toast.makeText(MainActivity.this, "Tìm sản phẩm: " + keyword, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
