package com.havudong.havudong;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.havudong.havudong.Model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPayment;
    private RadioGroup radioGroupPayment;
    private RadioButton rbFullPayment, rbInstallment;
    private Spinner spinnerMonths;
    private TextView tvTotal, tvMonthly;
    private Button btnPay;

    private PaymentAdapter paymentAdapter;
    private List<CartItem> cartItems;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detailed);

        recyclerViewPayment = findViewById(R.id.recyclerViewPayment);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        rbFullPayment = findViewById(R.id.rbFullPayment);
        rbInstallment = findViewById(R.id.rbInstallment);
        spinnerMonths = findViewById(R.id.spinnerMonths);
        tvTotal = findViewById(R.id.tvTotal);
        tvMonthly = findViewById(R.id.tvMonthly);
        btnPay = findViewById(R.id.btnPay);

        // Lấy dữ liệu giỏ hàng
        cartItems = new ArrayList<>(CartManager.getInstance().getCartItems());
        totalPrice = CartManager.getInstance().getTotalPrice();

        // Adapter cho RecyclerView
        paymentAdapter = new PaymentAdapter(cartItems);
        recyclerViewPayment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPayment.setAdapter(paymentAdapter);

        // Hiển thị tổng tiền
        tvTotal.setText("Tổng tiền: " + String.format("%,d ₫", totalPrice));

        // Spinner chọn số tháng trả góp
        Integer[] months = {3, 6, 9, 12};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonths.setAdapter(adapter);

        // Ẩn spinner khi chọn trả toàn bộ
        spinnerMonths.setVisibility(View.GONE);
        tvMonthly.setVisibility(View.GONE);

        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbFullPayment) {
                spinnerMonths.setVisibility(View.GONE);
                tvMonthly.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbInstallment) {
                spinnerMonths.setVisibility(View.VISIBLE);
                tvMonthly.setVisibility(View.VISIBLE);
                updateMonthlyPayment();
            }
        });

        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateMonthlyPayment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Nút thanh toán
        btnPay.setOnClickListener(v -> {
            if (rbFullPayment.isChecked()) {
                Toast.makeText(this, "Thanh toán toàn bộ: " +
                        String.format("%,d ₫", totalPrice), Toast.LENGTH_LONG).show();
            } else if (rbInstallment.isChecked()) {
                int monthsSelected = (int) spinnerMonths.getSelectedItem();
                int monthly = totalPrice / monthsSelected;
                Toast.makeText(this, "Thanh toán trả góp " + monthsSelected +
                        " tháng, mỗi tháng: " + String.format("%,d ₫", monthly), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sau khi thanh toán xong thì xóa giỏ
            CartManager.getInstance().clearCart();
            finish();
        });
    }

    private void updateMonthlyPayment() {
        if (rbInstallment.isChecked()) {
            int monthsSelected = (int) spinnerMonths.getSelectedItem();
            int monthly = totalPrice / monthsSelected;
            tvMonthly.setText("Mỗi tháng: " + String.format("%,d ₫", monthly));
        }
    }
}
