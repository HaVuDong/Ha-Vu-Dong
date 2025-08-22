package com.havudong.havudong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.havudong.havudong.Api.ApiClient;
import com.havudong.havudong.Api.ApiService;
import com.havudong.havudong.Model.Product;
import com.havudong.havudong.Adapter.ProductAdapter;
import com.havudong.havudong.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryProductsActivity extends AppCompatActivity {

    private GridView gridView;
    private ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();

    private String categoryName;
    private ApiService apiService;

    private TextView txtCategoryTitle;
    private FrameLayout frameCategory1, frameCategory2, frameCategory3, frameCategory4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        // Khởi tạo view
        gridView = findViewById(R.id.gridViewProducts);
        txtCategoryTitle = findViewById(R.id.txtCategoryTitle);

        frameCategory1 = findViewById(R.id.frameCategory1);
        frameCategory2 = findViewById(R.id.frameCategory2);
        frameCategory3 = findViewById(R.id.frameCategory3);
        frameCategory4 = findViewById(R.id.frameCategory4);

        // Nút back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Lấy category từ Intent
        Intent intent = getIntent();
        categoryName = intent != null ? intent.getStringExtra("category") : null;
        if (categoryName == null || categoryName.trim().isEmpty()) {
            Toast.makeText(this, "Không nhận được danh mục!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtCategoryTitle.setText(categoryName);

        // Adapter cho GridView
        adapter = new ProductAdapter(this, productList);
        gridView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Load sản phẩm ban đầu
        loadProductsByCategory(categoryName);

        // Set listener đổi category
        frameCategory1.setOnClickListener(v -> changeCategory("Dâu Đà Lạt"));
        frameCategory2.setOnClickListener(v -> changeCategory("Rau củ Đà Lạt"));
        frameCategory3.setOnClickListener(v -> changeCategory("Cúc Đà Lạt"));
        frameCategory4.setOnClickListener(v -> changeCategory("Mứt các loại"));
    }

    // Hàm đổi category
    private void changeCategory(String newCategory) {
        if (!newCategory.equals(categoryName)) {
            categoryName = newCategory;
            txtCategoryTitle.setText(categoryName);
            loadProductsByCategory(categoryName);
        }
    }

    // Hàm load sản phẩm theo category
    private void loadProductsByCategory(String category) {
        Call<List<Product>> call = apiService.getProductsByCategory(category);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (productList.isEmpty()) {
                        Toast.makeText(CategoryProductsActivity.this, "Không có sản phẩm cho: " + category, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryProductsActivity.this, "Không có sản phẩm!", Toast.LENGTH_SHORT).show();
                    Log.e("CategoryProducts", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(CategoryProductsActivity.this, "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CategoryProducts", "onFailure", t);
            }
        });
    }
}
