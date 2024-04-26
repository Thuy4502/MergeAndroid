package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Product> listProducts;

        public ProductAdapter(Context context, List<Product> listProducts) {
            this.mInflater = LayoutInflater.from(context);
            this.listProducts = listProducts;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tvName, tvPrice;
            private ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ProductName);
            tvPrice = itemView.findViewById(R.id.tv_ProductPrice);
            imgProduct = itemView.findViewById(R.id.img_Product);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardProduct = mInflater.inflate(R.layout.layout_item_product, parent, false);
        return new ViewHolder(cardProduct);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product products = listProducts.get(position);
        holder.tvName.setText(products.getProductName());
        holder.tvPrice.setText(String.valueOf(products.getPrice_update_detail())); // Convert giá thành chuỗi
        Glide.with(holder.itemView.getContext())
                .load(products.getImage())
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

}