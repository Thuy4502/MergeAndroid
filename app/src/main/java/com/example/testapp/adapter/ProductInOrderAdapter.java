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
import com.example.testapp.UserOrderActivity;
import com.example.testapp.model.Cart;


import java.util.List;

public class ProductInOrderAdapter extends RecyclerView.Adapter<ProductInOrderAdapter.ProductInOrderViewHolder> {

    List<Cart> productList;
    private static Context context;

    public ProductInOrderAdapter(List<Cart> productList) {
        this.productList = productList;
//        this.context = context;
    }

    @NonNull
    @Override
    public ProductInOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_detail, parent, false);
        context = parent.getContext();
        ProductInOrderViewHolder productInOrderViewHolder = new ProductInOrderViewHolder(view);
        return productInOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductInOrderViewHolder holder, int position) {
        Cart sp = productList.get(position);
        if(sp == null) {
            return;
        }
        holder.tvProductName.setText(sp.getProduct().getProductName());
        holder.tvSize.setText(sp.getSize());
        holder.tvQuantity.setText(String.valueOf(sp.getQuantity()));
        holder.tvPrice.setText(UserOrderActivity.formatNumber(sp.getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(sp.getProduct().getImage())
                .into(holder.ivProductImg);
    }

    @Override
    public int getItemCount() {
        if(productList != null) {
            return productList.size();
        }
        return 0;
    }


    public class ProductInOrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvSize, tvPrice;
        ImageView ivProductImg;

        public ProductInOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSize = itemView.findViewById(R.id.tvSize);
            ivProductImg = itemView.findViewById(R.id.ivProductImg);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
