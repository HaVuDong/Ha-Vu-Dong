package com.havudong.havudong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.havudong.havudong.CartManager;
import com.havudong.havudong.Model.Product;
import com.havudong.havudong.R;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPrice, tvDescription, tvQuantity;
    private ImageView ivImage;
    private Button btnAddToCart, btnIncrease, btnDecrease, btnBuyNow; // thêm btnBuyNow
    private Product product;
    private int quantity = 1; // số lượng mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        ivImage = findViewById(R.id.ivImage);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // thêm 3 view cho tăng giảm
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        // ánh xạ btnBuyNow
        btnBuyNow = findViewById(R.id.btnBuyNow);

        if (getIntent() != null && getIntent().hasExtra("product")) {
            product = (Product) getIntent().getSerializableExtra("product");

            tvName.setText(product.getName());
            tvPrice.setText(String.format("%,d ₫", product.getPrice()));
            tvDescription.setText(product.getDescription());

            Glide.with(this)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivImage);
        }

        // hiển thị số lượng mặc định
        tvQuantity.setText(String.valueOf(quantity));

        // tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // thêm vào giỏ
        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addToCart(product, quantity);
                btnAddToCart.setText("Đã thêm vào giỏ (" + CartManager.getInstance().getCartCount() + ")");
            }
        });

        // mua ngay → thêm vào giỏ + chuyển sang CartActivity
        btnBuyNow.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addToCart(product, quantity);
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
                finish(); // đóng ProductDetail để user ở lại màn giỏ hàng
            }
        });
    }
}
