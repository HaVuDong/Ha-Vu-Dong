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
import com.havudong.havudong.Adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton btnMenu, btnSearchIcon;
    EditText etSearch;
    TextView tvNavUsername;
    GridView gridView;

    ApiService apiService;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        btnMenu = findViewById(R.id.btnMenu);
        btnSearchIcon = findViewById(R.id.btnSearchIcon);
        etSearch = findViewById(R.id.etSearch);
        gridView = findViewById(R.id.gridView);

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

        // Sự kiện mở menu
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Click menu item
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

        // Sự kiện tìm kiếm (nếu muốn)
        btnSearchIcon.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                Toast.makeText(MainActivity.this, "Tìm kiếm: " + keyword, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            }
        });

        // Dữ liệu mẫu
        List<Product> products = new ArrayList<>();
        products.add(new Product("Gà thịt thả vườn", "250.000 ₫", R.drawable.img_4));
        products.add(new Product("Gà thịt công nghiệp", "100.000 ₫", R.drawable.img_2));
        products.add(new Product("Gà tre thái", "400.000 ₫", R.drawable.img_3));
        products.add(new Product("Đông tảo loại 1", "350.000 ₫", R.drawable.img_1));

        // Gắn adapter custom
        ProductAdapter adapter = new ProductAdapter(this, products);
        gridView.setAdapter(adapter);

        // Click item ListView nếu muốn
        gridView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(MainActivity.this, "Bạn chọn: " + products.get(position).getName(), Toast.LENGTH_SHORT).show()
        );
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
