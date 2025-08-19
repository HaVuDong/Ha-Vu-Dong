package com.havudong.havudong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.havudong.havudong.Model.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
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

        // Load ảnh từ URL
        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.img);

        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice() + " ₫");

        holder.btnAdd.setOnClickListener(v ->
                Toast.makeText(context, product.getName() + " đã thêm vào giỏ", Toast.LENGTH_SHORT).show()
        );

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView name, price;
        Button btnAdd;
    }
}
