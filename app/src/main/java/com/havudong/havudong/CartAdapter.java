package com.havudong.havudong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.havudong.havudong.Model.Product;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Product> cartList;
    private OnCartChangeListener listener; // callback cho Activity

    public CartAdapter(Context context, List<Product> cartList, OnCartChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;
        ImageButton btnRemove;
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
            holder.btnRemove = convertView.findViewById(R.id.btnRemove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = cartList.get(position);

        // Gán dữ liệu
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice() + " đ");

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(holder.imgProduct);

        // Xử lý nút xóa
        holder.btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(product);
            cartList = CartManager.getInstance().getCartList(); // 👈 dùng getCartList()
            notifyDataSetChanged();

            if (listener != null) {
                listener.onCartChanged();
            }
        });

        return convertView;
    }

    // Interface callback
    public interface OnCartChangeListener {
        void onCartChanged();
    }
}
