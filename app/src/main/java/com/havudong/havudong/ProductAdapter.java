package com.havudong.havudong;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.havudong.havudong.Model.Product;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private final List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList != null ? productList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return productList != null ? productList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_custom, parent, false);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.itemImg);
            holder.name = convertView.findViewById(R.id.itemName);
            holder.price = convertView.findViewById(R.id.itemPrice);
            holder.btnAdd = convertView.findViewById(R.id.btnAddCart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);

        // Load ảnh
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.img);

        // Gán dữ liệu
        holder.name.setText(product.getName());
        holder.price.setText(String.format("%,d ₫", product.getPrice())); // format int thành tiền

        // Sự kiện thêm vào giỏ
        holder.btnAdd.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product, 1);
            Toast.makeText(context,
                    product.getName() + " đã thêm vào giỏ (" +
                            CartManager.getInstance().getCartCount() + ")",
                    Toast.LENGTH_SHORT).show();
        });

        // Sự kiện click item -> sang trang chi tiết
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product); // Product cần implements Serializable hoặc Parcelable
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView name, price;
        Button btnAdd;
    }
}
