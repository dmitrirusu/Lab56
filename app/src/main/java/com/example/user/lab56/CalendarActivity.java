package com.example.user.lab56;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarActivity extends AppCompatActivity {

    @BindView(R.id.custom_calendar)
    CompactCalendarView calendar;

    ArrayList<MyXml.Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        try {
            update();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Intent intent = new Intent(CalendarActivity.this, SetAlarm.class);
                intent.putExtra("date", dateClicked);
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });
    }

    private void update() throws IOException, SAXException, ParserConfigurationException {
        events = MyXml.getFromXml();

        calendar.removeAllEvents();
        ArrayList<Event> calendarEvent = new ArrayList<>();

        for (MyXml.Event event : events) {
            HashMap<String, String> data = new HashMap<>();
            data.put("message", event.message);
            data.put("id", event.id);
            calendarEvent.add(new Event(Color.GREEN, event.time, data));
        }
        calendar.addEvents(calendarEvent);
    }
}
