package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.model.Product;
import com.example.testapp.model.ReviewStar;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Product> listProducts;
    private List<ReviewStar> listReviewStar;

        public ProductAdapter(Context context, List<Product> listProducts, List<ReviewStar> listReviewStar)  {
            this.mInflater = LayoutInflater.from(context);
            this.listProducts = listProducts;
            this.listReviewStar = listReviewStar;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView tvName, tvPrice, tvStar;
            private ImageView imgProduct;
            private LinearLayout llStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ProductName);
            tvPrice = itemView.findViewById(R.id.tv_ProductPrice);
            imgProduct = itemView.findViewById(R.id.img_Product);
            tvStar = itemView.findViewById(R.id.txtEvalu);

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