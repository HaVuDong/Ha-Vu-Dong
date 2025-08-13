package com.havudong.havudong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.havudong.havudong.Api.ApiClient;
import com.havudong.havudong.Api.ApiService;
import com.havudong.havudong.Model.User;
import com.havudong.havudong.User.ChangePassActivity;
import com.havudong.havudong.User.UserInfoActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageButton btnMenu, btnSearchIcon;
    LinearLayout searchBar;
    EditText etSearch;
    Button btnSearch;
    TextView tvNavUsername;

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
        searchBar = findViewById(R.id.searchBar);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

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

        // Hiện/ẩn search bar
        btnSearchIcon.setOnClickListener(v ->
                searchBar.setVisibility(searchBar.getVisibility() == View.GONE ? View.VISIBLE : View.GONE)
        );

        btnSearch.setOnClickListener(v -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                Toast.makeText(MainActivity.this, "Tìm kiếm: " + keyword, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            }
        });

        // Dữ liệu mẫu
        String[] items = {"Sản phẩm 1", "Sản phẩm 2", "Sản phẩm 3", "Sản phẩm 4"};
        int[] icons = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};

        // Adapter RecyclerView
        ProductAdapter adapter = new ProductAdapter(this, items, icons, position -> {
            Toast.makeText(MainActivity.this, "Bạn chọn: " + items[position], Toast.LENGTH_SHORT).show();
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cột
        recyclerView.setAdapter(adapter);
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
