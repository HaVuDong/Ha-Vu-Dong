package com.havudong.havudong.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.havudong.havudong.CartManager;
import com.havudong.havudong.R;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private ImageView qrImageView;
    private TextView tvTotalPrice, tvOrder;
    private ProgressBar progressBar;
    private Button btnDone;

    // Thông tin ngân hàng
    private final String BANK_CODE = "970422"; // MBBank
    private final String ACCOUNT_NO = "0367189928";
    private final String ADD_INFO = "ThanhToanDonHang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detailed);

        qrImageView = findViewById(R.id.qrImageView);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvOrder = findViewById(R.id.tvOrder);
        progressBar = findViewById(R.id.progressBar);
        btnDone = findViewById(R.id.btnDone);

        // Lấy tổng tiền
        double totalPrice = CartManager.getInstance().getTotalPrice();

        // Hiển thị tổng tiền
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText("Tổng tiền: " + formatter.format(totalPrice));

        // Set nội dung chuyển khoản
        tvOrder.setText("Nội dung: " + ADD_INFO);

        // Load QR động
        String qrUrl = "https://img.vietqr.io/image/"
                + BANK_CODE + "-" + ACCOUNT_NO
                + "-compact.png?amount=" + (int) totalPrice
                + "&addInfo=" + ADD_INFO;

        progressBar.setVisibility(ProgressBar.VISIBLE);
        Glide.with(this)
                .load(qrUrl)
                .placeholder(R.drawable.placeholder) // bạn cần tạo image placeholder
                .error(R.drawable.error_image) // bạn cần tạo image lỗi
                .into(qrImageView);
        progressBar.setVisibility(ProgressBar.GONE);

        // Nút hoàn tất
        btnDone.setOnClickListener(v -> {
            CartManager.getInstance().clearCart(); // xóa giỏ
            finish(); // quay lại
        });
    }
}
