package com.havudong.havudong;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;

public class ForgotpassActivity extends AppCompatActivity {

    EditText etUsername, etPhone, etEmail;
    Button btnVerifyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        etUsername = findViewById(R.id.etUsername);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnVerifyInfo = findViewById(R.id.btnVerifyInfo);

        btnVerifyInfo.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (checkUserInfo(username, phone, email)) {
                showNewPasswordDialog(username);
            } else {
                Toast.makeText(this, "Thông tin không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkUserInfo(String username, String phone, String email) {
        try {
            InputStream is = getAssets().open("users.json"); // file mô phỏng data
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray users = new JSONArray(json);

            for (int i = 0; i < users.length(); i++) {
                JSONObject u = users.getJSONObject(i);
                if (username.equals(u.getString("username")) &&
                        phone.equals(u.getString("phone")) &&
                        email.equals(u.getString("email"))) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showNewPasswordDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nhập mật khẩu mới");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        EditText etNewPass = new EditText(this);
        etNewPass.setHint("Mật khẩu mới");
        etNewPass.setInputType(0x00000081); // textPassword
        layout.addView(etNewPass);

        EditText etConfirmPass = new EditText(this);
        etConfirmPass.setHint("Xác nhận mật khẩu");
        etConfirmPass.setInputType(0x00000081); // textPassword
        layout.addView(etConfirmPass);

        builder.setView(layout);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String newPass = etNewPass.getText().toString();
            String confirmPass = etConfirmPass.getText().toString();

            if (!newPass.isEmpty() && newPass.equals(confirmPass)) {
                // Thực hiện lưu mật khẩu mới (ví dụ lưu file, gọi API, ...).
                Toast.makeText(this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
