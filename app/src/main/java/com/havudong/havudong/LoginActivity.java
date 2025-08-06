package com.havudong.havudong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnCreateAccount;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = Volley.newRequestQueue(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(username, password);
        });

        btnCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser(String username, String password) {
        String url = "https://689310b3c49d24bce8694528.mockapi.io/users?username=" + username;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    if (response.length() == 0) {
                        Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        JSONObject user = response.getJSONObject(0);
                        String apiPass = user.getString("password");
                        if (password.equals(apiPass)) {
                            // Lưu username và user id nếu cần
                            SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
                            prefs.edit().putString("username", username).apply();

                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                    Log.e("LoginError", error.toString());
                });

        queue.add(request);
    }
}
