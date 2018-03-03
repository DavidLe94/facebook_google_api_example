package com.example.asus.androidadvance_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by asus on 16/02/2017.
 */

public class CustomNewsLayout extends ArrayAdapter<RSSItem> {
    public CustomNewsLayout(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomNewsLayout(Context context, int resource, List<RSSItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_news_layout, null);
        }

        RSSItem p = getItem(position);

        if (p != null) {
            ImageView img = (ImageView) v.findViewById(R.id.imgNews);
            Picasso.with(getContext()).load(p.getImageLink()).into(img);

            TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvTitle.setText(p.getTitle());

            TextView tvDate = (TextView)v.findViewById(R.id.tvDate);
            tvDate.setText(p.getPostDate());

            TextView tvDes = (TextView)v.findViewById(R.id.tvDescription);
            tvDes.setText(p.getDescription());
        }
        return v;
    }
}
