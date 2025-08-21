package com.havudong.havudong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.havudong.havudong.Model.CartItem;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listViewCart;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems);
        listViewCart.setAdapter(cartAdapter);

        updateTotalPrice();

        btnCheckout.setOnClickListener(v -> {
            int total = CartManager.getInstance().getTotalPrice();
            if (total == 0) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang màn hình thanh toán
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            startActivity(intent);
        });
    }

    private void updateTotalPrice() {
        int total = CartManager.getInstance().getTotalPrice();
        tvTotalPrice.setText("Tổng: " + String.format("%,d ₫", total));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }
}
