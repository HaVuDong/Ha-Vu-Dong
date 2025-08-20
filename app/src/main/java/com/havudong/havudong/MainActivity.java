package com.havudong.havudong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.havudong.havudong.Api.ApiClient;
import com.havudong.havudong.Api.ApiService;
import com.havudong.havudong.Model.Product;
import com.havudong.havudong.Model.User;
import com.havudong.havudong.User.ChangePassActivity;
import com.havudong.havudong.User.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton btnMenu, btnSearch, btnCart;
    EditText etSearch;
    TextView tvNavUsername;
    GridView gridView;

    ApiService apiService;
    String username;
    ProductAdapter adapter;
    List<Product> products = new ArrayList<>();

    // Category
    FrameLayout frameCategory1, frameCategory2, frameCategory3, frameCategory4;
    TextView txtCategory1, txtCategory2, txtCategory3, txtCategory4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        drawerLayout   = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        btnMenu        = findViewById(R.id.btnMenu);
        btnSearch      = findViewById(R.id.btnSearch);
        etSearch       = findViewById(R.id.etSearch);
        gridView       = findViewById(R.id.gridView);
        btnCart        = findViewById(R.id.btnCart);

        // Category mapping đúng ID
        frameCategory1 = findViewById(R.id.frameCategory1);
        txtCategory1   = findViewById(R.id.txtCategory1);

        frameCategory2 = findViewById(R.id.frameCategory2);
        txtCategory2   = findViewById(R.id.txtCategory2);

        frameCategory3 = findViewById(R.id.frameCategory3);
        txtCategory3   = findViewById(R.id.txtCategory3);

        frameCategory4 = findViewById(R.id.frameCategory4);
        txtCategory4   = findViewById(R.id.txtCategory4);

        // Header NavigationView
        View headerView = navigationView.getHeaderView(0);
        tvNavUsername = headerView.findViewById(R.id.tvNavUsername);

        // Lấy username từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        username = prefs.getString("username", null);

        apiService = ApiClient.getClient().create(ApiService.class);

        if (username != null) {
            loadUserFromApi(username);
        }

        // Adapter ban đầu
        adapter = new ProductAdapter(this, products);
        gridView.setAdapter(adapter);

        // Click menu
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            drawerLayout.postDelayed(() -> {
                if (id == R.id.menu_account) {
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                } else if (id == R.id.menu_change_password) {
                    startActivity(new Intent(MainActivity.this, ChangePassActivity.class));
                } else if (id == R.id.menu_logout) {
                    logout();
                }
            }, 200);
            return true;
        });

        // Sự kiện tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchProducts(keyword);
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            }
        });

        // Click item GridView
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Product product = products.get(position);
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        // Click category
        frameCategory1.setOnClickListener(v -> openCategory(txtCategory1.getText().toString()));
        frameCategory2.setOnClickListener(v -> openCategory(txtCategory2.getText().toString()));
        frameCategory3.setOnClickListener(v -> openCategory(txtCategory3.getText().toString()));
        frameCategory4.setOnClickListener(v -> openCategory(txtCategory4.getText().toString()));


        // Click giỏ hàng
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Load sản phẩm
        loadProductsFromApi();
    }

    private void openCategory(String categoryName) {
        Intent intent = new Intent(MainActivity.this, CategoryProductsActivity.class);
        intent.putExtra("category", categoryName); // key phải trùng "category"
        startActivity(intent);
    }

    private void loadProductsFromApi() {
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products.clear();
                    products.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Không lấy được sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchProducts(String keyword) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(p);
            }
        }
        if (!filtered.isEmpty()) {
            adapter = new ProductAdapter(this, filtered);
            gridView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserFromApi(String username) {
        apiService.getUserByUsername(username).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    tvNavUsername.setText(response.body().get(0).getUsername());
                } else {
                    Toast.makeText(MainActivity.this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
