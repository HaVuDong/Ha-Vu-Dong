package com.havudong.havudong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.havudong.havudong.CartManager;
import com.havudong.havudong.Model.CartItem;
import com.havudong.havudong.R;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private final Context context;
    private final List<CartItem> cartItems;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.imgProduct);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.btnIncrease = convertView.findViewById(R.id.btnIncrease);
            holder.btnDecrease = convertView.findViewById(R.id.btnDecrease);
            holder.btnRemove = convertView.findViewById(R.id.btnRemove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("%,d ₫", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgProduct);

        // Tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            CartManager.getInstance().increaseQuantity(item.getId());
            notifyDataSetChanged();
            Toast.makeText(context, "Đã tăng " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        // Giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            CartManager.getInstance().decreaseQuantity(item.getId());
            notifyDataSetChanged();
            Toast.makeText(context, "Đã giảm " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        // Xóa sản phẩm
        holder.btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(item.getId());
            notifyDataSetChanged();
            Toast.makeText(context, "Đã xóa " + item.getName(), Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnIncrease, btnDecrease, btnRemove;
    }

}
