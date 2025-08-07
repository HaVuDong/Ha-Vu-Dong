package com.havudong.havudong;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ForgotpassActivity extends AppCompatActivity {

    EditText etUsername, etOldPassword, etNewPassword, etConfirmPassword;
    Button btnChangePassword;
    RequestQueue queue;

    String baseUrl = "https://689310b3c49d24bce8694528.mockapi.io/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        queue = Volley.newRequestQueue(this);

        etUsername = findViewById(R.id.etUsername);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChangePassword = findViewById(R.id.btnConfirmChange);

        btnChangePassword.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String oldPass = etOldPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu mới và xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            checkOldPasswordAndChange(username, oldPass, newPass);
        });
    }

    private void checkOldPasswordAndChange(String username, String oldPass, String newPass) {
        String url = baseUrl + "?username=" + username;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.length() == 0) {
                            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject user = response.getJSONObject(0);
                        String userId = user.getString("id");
                        String currentPass = user.getString("password");

                        if (!oldPass.equals(currentPass)) {
                            Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        updatePassword(userId, newPass);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("API_ERROR", error.toString());
                    Toast.makeText(this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private void updatePassword(String userId, String newPassword) {
        String url = baseUrl + "/" + userId;

        StringRequest request = new StringRequest(
                Request.Method.PUT,
                url,
                response -> {
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    Log.e("PUT_ERROR", error.toString());
                    Toast.makeText(this, "Lỗi cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public byte[] getBody() {
                try {
                    JSONObject body = new JSONObject();
                    body.put("password", newPassword);
                    return body.toString().getBytes("utf-8");
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        queue.add(request);
    }
}
