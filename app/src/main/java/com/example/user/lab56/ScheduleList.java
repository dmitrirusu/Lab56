package com.example.user.lab56;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.domain.Event;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleList extends AppCompatActivity {

    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;

    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.current_date)
    TextView currentDate;
    @BindView(R.id.list)
    ListView list;
    private ArrayList<MyXml.Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        ButterKnife.bind(this);

        Date date = new Date(System.currentTimeMillis());
        int hours = date.getHours();
        int minutes = date.getMinutes();
        time.setText(hours + ":" + minutes);


        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);

        currentDate.setText(day + "/" + month + "/" + year);

        try {
            events = MyXml.getFromXml();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        ArrayList<MyXml.Event> filteredEvents = new ArrayList<>();
        for (MyXml.Event event : events) {
            Date eventDate = new Date(event.time);
            if(eventDate.getDate() == day && eventDate.getMonth() == month) {
                HashMap<String, String> data = new HashMap<>();
                data.put("message", event.message);
                data.put("id", event.id);
                filteredEvents.add(event);
            }
        }
        ScheduleListAdapter adapter = new ScheduleListAdapter(this, R.layout.item_list, filteredEvents);
        list.setAdapter(adapter);
    }
}
