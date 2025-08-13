package com.havudong.havudong.User;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.havudong.havudong.Api.ApiClient;
import com.havudong.havudong.Api.ApiService;
import com.havudong.havudong.Model.User;
import com.havudong.havudong.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassActivity extends AppCompatActivity {

    TextView tvTitle;
    EditText etOldPassword, etNewPassword, etConfirmPassword;
    Button btnConfirm;
    ApiService apiService;
    String loggedInUsername; // username đang login

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass); // layout đổi mật khẩu

        apiService = ApiClient.getClient().create(ApiService.class);

        // Lấy username đang login từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        loggedInUsername = preferences.getString("username", null);

        if (loggedInUsername == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ view
        tvTitle = findViewById(R.id.tvChangePassTitle);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirmChange);

        // Hiển thị username trên tiêu đề
        tvTitle.setText("Đổi mật khẩu của " + loggedInUsername);

        btnConfirm.setOnClickListener(v -> {
            String oldPass = etOldPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy user từ API theo username
            apiService.getUserByUsername(loggedInUsername).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        User user = response.body().get(0);

                        if (!user.getPassword().equals(oldPass)) {
                            Toast.makeText(ChangePassActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Cập nhật mật khẩu
                        user.setPassword(newPass);
                        apiService.updatePassword(user.getId(), user).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ChangePassActivity.this, "Không thể đổi mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(ChangePassActivity.this, "Lỗi cập nhật: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(ChangePassActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Toast.makeText(ChangePassActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
