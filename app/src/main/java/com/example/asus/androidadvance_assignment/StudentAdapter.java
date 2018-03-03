package com.example.asus.androidadvance_assignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asus on 10/02/2017.
 */

public class StudentAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<students> list;

    public StudentAdapter(ArrayList<students> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.students_one_row,null);

        TextView tvID = (TextView)row.findViewById(R.id.tvID);
        TextView tvStudentID = (TextView)row.findViewById(R.id.tvStudentID);
        TextView tvStudentName = (TextView)row.findViewById(R.id.tvStudentName);

        students stu = list.get(position);
        tvID.setText(stu.getID()+". ");
        tvStudentID.setText(stu.getStudentsID());
        tvStudentName.setText(stu.getStudentName());
        return row;
    }
}
