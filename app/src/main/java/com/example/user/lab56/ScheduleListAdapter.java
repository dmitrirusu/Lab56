package com.example.user.lab56;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 018 18.12.16.
 */

public class ScheduleListAdapter extends ArrayAdapter<MyXml.Event> {

    private Context context;
    private ArrayList<MyXml.Event> events;

    public ScheduleListAdapter(Context context, int layoutId, ArrayList<MyXml.Event> events) {
        super(context, layoutId, events);
        this.context = context;
        this.events = events;
    }

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.date)
    TextView date;


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_list, null);
        }
        ButterKnife.bind(this, v);
        Date date = new Date(getItem(position).time);
        time.setText(date.getHours() + ":" + date.getMinutes());
        message.setText(getItem(position).message);
        this.date.setText(date.getDate() + "/" + date.getMonth() + "/" + date.getYear());
        return v;
    }
}
