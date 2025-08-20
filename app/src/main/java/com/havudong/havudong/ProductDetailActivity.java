package com.havudong.havudong;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.havudong.havudong.Model.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPrice, tvDescription;
    private ImageView ivImage;
    private Button btnAddToCart;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        ivImage = findViewById(R.id.ivImage);
        btnAddToCart = findViewById(R.id.btnAddToCart);

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

        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addToCart(product);
                btnAddToCart.setText("Đã thêm vào giỏ (" + CartManager.getInstance().getCartCount() + ")");
            }
        });
    }
}
