package com.havudong.havudong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.havudong.havudong.Model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPrice, tvDescription;
    private ImageView ivImage;
    private Button btnAddToCart;

    private Product product; // nhận sản phẩm từ ProductAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ view
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        ivImage = findViewById(R.id.ivImage);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Nhận dữ liệu sản phẩm từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            product = (Product) intent.getSerializableExtra("product");

            // Gán dữ liệu vào view
            tvName.setText(product.getName());
            tvPrice.setText(product.getPrice() + " ₫");
            tvDescription.setText(product.getDescription());

            // Load ảnh từ URL bằng Glide
            Glide.with(this)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivImage);
        }

        // Xử lý thêm vào giỏ
        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addToCart(product);
                btnAddToCart.setText("Đã thêm vào giỏ (" + CartManager.getInstance().getCartCount() + ")");
            }
        });
    }
}
