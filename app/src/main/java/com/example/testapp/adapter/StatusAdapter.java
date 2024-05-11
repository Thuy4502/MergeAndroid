package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testapp.R;

import java.util.List;

public class StatusAdapter extends BaseAdapter {
    Context context;
    int resource;
    List<String> status;
    TextView tvCategoryItemVer2;


    public StatusAdapter(@NonNull Context context, int resource, List <String> status) {
        super();
        this.context = context;
        this.resource = resource;
        this.status = status;
    }

    @Override
    public int getCount() {
        return status != null ? status.size() : 0;
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


        String statusName = status.get(position);

        tvCategoryItemVer2.setText(statusName);


        return convertView;
    }
}
