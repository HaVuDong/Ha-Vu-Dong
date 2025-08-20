package com.havudong.havudong;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.havudong.havudong.Model.Product;
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

        // Lấy danh sách giỏ hàng
        List<Product> cartList = CartManager.getInstance().getCartList();

        cartAdapter = new CartAdapter(this, cartList, () -> updateTotalPrice());
        listViewCart.setAdapter(cartAdapter);

        updateTotalPrice();

        btnCheckout.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Product p : CartManager.getInstance().getCartList()) {
            total += p.getPrice();
        }
        tvTotalPrice.setText("Tổng: " + total + " đ");
    }
}
