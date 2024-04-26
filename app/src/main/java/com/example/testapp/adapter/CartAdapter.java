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
import com.example.testapp.CartListActivity;
import com.example.testapp.CustomerHomeActivity;
import com.example.testapp.R;
import com.example.testapp.UserOrderActivity;
import com.example.testapp.model.Cart;
import com.example.testapp.model.Product;
import com.example.testapp.model.request.CartRequest;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>
{
    private static Context context;
    private List<Cart> cartList;
    public float priceAddition;
    float price = CartListActivity.getTotalPrice();
    public CartRequest cartData;


    public void updateTotalPrice(float newPrice) {
        price = newPrice;
        notifyDataSetChanged(); // Thông báo cho RecyclerView cập nhật giao diện
    }

    public CartAdapter(List<Cart> list) {
        this.cartList = list;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_item, parent, false);
        context = parent.getContext();
        CartViewHolder cartViewHolder = new CartViewHolder(view);

        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        Product sp = cart.getProduct();
        if (cart == null) {
            return;
        }
        holder.tvProductName.setText(cart.getProduct().getProductName());
        holder.cart = cart;
        holder.tvQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.tvSize.setText(cart.getSize());

        StringBuilder price = new StringBuilder();
        price.append(String.valueOf(cart.getPrice()));
        priceAddition = Float.parseFloat(String.valueOf(price));
        holder.tvProductPrice.setText(UserOrderActivity.formatNumber(Float.parseFloat(String.valueOf(cart.getPrice()))));


        if(sp.getImage() != null && !sp.getImage().isEmpty()) {
            Glide.with(context)
                    .load(sp.getImage())
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.icon_caphe)
                    .into(holder.ivProductImg);
        }
        else {
            holder.ivProductImg.setImageResource(R.drawable.img_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        if(cartList != null) {
            return cartList.size();
        }
        return 0;
    }


    public class CartViewHolder extends  RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice, tvQuantity, tvSize;
        ImageView ivAdd, ivMinus, ivProductImg, ivBack, ivTrash;

        Cart cart;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvName);
            tvProductPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivProductImg = itemView.findViewById(R.id.ivProductImg);
            tvSize = itemView.findViewById(R.id.tvSize);
            ivAdd = itemView.findViewById(R.id.ivAddCart);
            ivMinus = itemView.findViewById(R.id.ivMinusCart);
            ivTrash = itemView.findViewById(R.id.ivTrash);

            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence textQuantity = tvQuantity.getText();
                    String sQuantity = textQuantity.toString();
                    int sl = Integer.parseInt(sQuantity);
                    tvQuantity.setText(String.valueOf(sl + 1));
                    priceAddition = cart.getPrice();
                    price += priceAddition;
                    CartListActivity.setTotalPrice(price);


                    if (cartData == null) {
                        cartData = new CartRequest();
                        System.out.println("Gio hang nullllllllllllll" + cart.getProduct().getProductId());
                    }
                    cartData.setProduct_id(String.valueOf(cart.getProduct().getProductId()));
                    cartData.setSize(cart.getSize());
                    CustomerHomeActivity.callApiIncrementCart(cartData);
                }
            });

            ivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence textQuantity = tvQuantity.getText();
                    String sQuantity = textQuantity.toString();
                    int sl = Integer.parseInt(sQuantity);
                    if (sl > 1) {
                        tvQuantity.setText(String.valueOf(sl - 1));
                        priceAddition = cart.getPrice();
                        price -= priceAddition;
                        CartListActivity.setTotalPrice(price);

                        System.out.println("Cart  ...................." + cart.getProduct().getProductId());

                        // Kiểm tra và khởi tạo cartRequest nếu nó là null
                        if (cartData == null) {
                            cartData = new CartRequest();
                            System.out.println("Gio hang nullllllllllllll" + cart.getProduct().getProductId());
                        }
                        cartData.setProduct_id(String.valueOf(cart.getProduct().getProductId()));
                        System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvv"+ cartData.getProduct_id());
                        cartData.setSize(cart.getSize());
                        CustomerHomeActivity.callApiReduceCart(cartData);
//                        CartListActivity.callApiGetAllCart();
                    }
                }
            });

            ivTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cartData == null) {
                        cartData = new CartRequest();
                        System.out.println("Gio hang nullllllllllllll" + cart.getProduct().getProductId());
                    }

                    cartData.setProduct_id(String.valueOf(cart.getProduct().getProductId()));
                    cartData.setSize(cart.getSize());
                    cartData.getProduct_id();
                    CustomerHomeActivity.updateAfterRemove();
                    CustomerHomeActivity.callApiDeleteCart(cartData);
                    CartListActivity.callApiGetAllCart();
                }
            });



        }


    }
}
