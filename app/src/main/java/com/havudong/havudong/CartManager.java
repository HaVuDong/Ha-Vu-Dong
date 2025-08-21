package com.havudong.havudong;

import com.havudong.havudong.Model.Product;
import com.havudong.havudong.Model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Thêm sản phẩm vào giỏ (có quantity)
    public void addToCart(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage(),
                quantity
        ));
    }

    // Xóa 1 sản phẩm
    public void removeFromCart(String productId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getId().equals(productId)) {
                cartItems.remove(i);
                break;
            }
        }
    }

    // Lấy toàn bộ giỏ hàng
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // Đếm số lượng sản phẩm
    public int getCartCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    // Tính tổng tiền
    public int getTotalPrice() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void increaseQuantity(String productId) {
        for (CartItem item : cartItems) {
            if (item.getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }
    }

    public void decreaseQuantity(String productId) {
        for (CartItem item : cartItems) {
            if (item.getId().equals(productId)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                }
                break;
            }
        }
    }

    // Xóa hết giỏ
    public void clearCart() {
        cartItems.clear();
    }
}
