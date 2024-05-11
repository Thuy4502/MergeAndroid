// adapter của spinner lọc đơn hàng theo trạng thái

package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testapp.R;
import com.example.testapp.model.Category;

import java.util.List;


public class ProductCategoryAdapterVer2 extends BaseAdapter {
    Context context;
    int resource;
    List<Category> data;
    TextView tvCategoryItemVer2, tvCategoryItem;


    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);


        tvCategoryItemVer2 = convertView.findViewById(R.id.tvCategoryItemVer2);      // txtOrderStatus của item nằm trong Spinner


        Category category = data.get(position);

        tvCategoryItemVer2.setText(category.getCategory_name());


        return convertView;
    }


    public ProductCategoryAdapterVer2(@NonNull Context context, int resource, List <Category> data) {
        super();
        this.context = context;
        this.resource = resource;
        this.data = data;
    }


}
