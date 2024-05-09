package com.example.testapp.adapter;





import static com.example.testapp.ProductListActivity.deletePd;
import static com.example.testapp.api.ApiService.apiService;
import static com.example.testapp.function.Function.formatToVND;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;


import com.example.testapp.ProductEditingActivity;
import com.example.testapp.R;
import com.example.testapp.model.Product;
import com.example.testapp.response.ApiResponse;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagerAdapter extends RecyclerView.Adapter<ProductManagerAdapter.MyViewHolder> {
    private Context context;
    private List<Product> productList;
    static int mItemSelected = -1;         //vị trí item được chọn
    public ProductManagerAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setFilterList(List<Product> filterList){
        this.productList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        final Product product1 = productList.get(position);

        holder.setIsRecyclable(false);
        if (product1 == null){
            return;
        }


        // Set sự kiện cho nút Xóa trong item
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cửa sổ xác nhận Xóa sản phẩm
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận ngưng bán sản phẩm")
                        .setMessage("Bạn có chắc chắn muốn ngưng bán sản phẩm này không?")
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Gọi API để xóa sản phẩm
                                //String token="Bearer "+"eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MTI0MTM2MzcsImV4cCI6MTcxMzAxODQzNywidXNlcm5hbWUiOiIrODQ5NzkzNDUxOTEifQ.N8cAv_ZdfIT9dBMdgE6oPfIL8bC6G1tbAWf0MdDOWg2kzZh2sk985x556X9VfpSQ";;
                                deletePd(String.valueOf(holder.tvProductID.getText()), mItemSelected, context);
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }



        });


        // Nếu sản phẩm có trạng thái Unactive(Đã bị xóa) thì đổi màu nền item và ẩn nút Xóa
        if (productList.get(position).getStatus().equals("Unactive")){
            String a = productList.get(position).getStatus();
            holder.itemProduct.setBackgroundResource(R.drawable.style_deleted_product);
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.itemProduct.setBackgroundResource(R.drawable.rounded_corners);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }



        // Set dữ liệu cho item của RecycleView Product
        holder.tvProductID.setText(product1.getProductId());
        holder.tvProductName.setText(product1.getProductName());
        holder.tvProductPrice.setText(formatToVND((int) product1.getPrice_update_detail().get(0).getPriceNew()));
        Picasso.get()
                .load(product1.getImage())    //load(url)
                .into(holder.imgProduct);                           //into(variant)



    }




    public int getItemCount() {
        return productList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
         TextView tvProductID;
        TextView tvProductName;
        TextView tvProductPrice;
        ShapeableImageView imgProduct;
        CheckBox cbProduct;
        LinearLayout itemProduct;
        ImageButton btnDelete;
        TextInputEditText etProductName, etProductPrice, etDescription;
        ImageView ivProductImg;
        TextView tvCategoryItem;
        AppCompatSpinner spnCategoryOption;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            itemProduct = itemView.findViewById(R.id.itemProduct);
            tvProductID = itemView.findViewById(R.id.tvProductId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.ivProduct);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            etProductName = itemView.findViewById(R.id.etProductName);
            etProductPrice = itemView.findViewById(R.id.etProductPrice);
            etDescription = itemView.findViewById(R.id.etDescription);
            ivProductImg = itemView.findViewById(R.id.ivProductImg);
            tvCategoryItem = itemView.findViewById(R.id.tvCategoryItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemSelected = getAdapterPosition();
                    notifyDataSetChanged();

                    Intent intent = new Intent(v.getContext(), ProductEditingActivity.class);
                    intent.putExtra("PRODUCT_ID", tvProductID.getText());
                    v.getContext().startActivity(intent);
                }
            });

        }

    }
}


