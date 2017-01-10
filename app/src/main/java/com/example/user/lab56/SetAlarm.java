package com.example.user.lab56;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetAlarm extends AppCompatActivity {

    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;
    private String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor...";
    @BindView(R.id.tw_date)
    TextView date;
    @BindView(R.id.item_time)
    TextView itemTime;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.item_content)
    TextView itemContent;

    @OnClick(R.id.set_unlocker_pattern)
    public void setPattern() {
        Intent intent = new Intent(this, PatternActivity.class);
        intent.putExtra("day", day);
        intent.putExtra("month", month);
        intent.putExtra("hours", hours);
        intent.putExtra("minutes", minutes);
        intent.putExtra("setPattern", true);
        startActivity(intent);
    }
    @OnClick(R.id.add)
    public void add() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hours, minutes);
        Date date = new Date(System.currentTimeMillis());
        date.setDate(day);
        date.setMonth(month);
        date.setYear(year);
        date.setHours(hours);
        date.setMinutes(minutes-1);
        date.setSeconds(0);
        try {
            long time = calendar.getTimeInMillis();
            XmlHelper.add(this, time, message); //1482012600000
            Toast.makeText(this, "Alarm Added", Toast.LENGTH_SHORT).show();
            finish();
        } catch (TransformerException | IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.set_content)
    public void setContent() {
        new MaterialDialog.Builder(this)
                .title("Enter Text")
//                .content(R.string.input_content)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        dialog.dismiss();
                    }
                })
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        message = input.toString();
                        content.setText(message);
                        itemContent.setText(message);
                    }
                }).show();
    }

    @OnClick(R.id.set_time)
    public void onClickSetTime() {
        Date date = new Date();
        final int hours = date.getHours();
        final int minutes = date.getMinutes();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        SetAlarm.this.hours = hourOfDay;
                        SetAlarm.this.minutes = minute;
                        SetAlarm.this.date.setText(String.format("%1$s-%2$s-%3$s %4$s:%5$s", day, month, year, hourOfDay, minute));
                        itemTime.setText(String.format("%1$s-%2$s-%3$s %4$s:%5$s", day, month, year, hourOfDay, minute));
                    }
                }, hours, minutes, false);
        timePickerDialog.show();

    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        ButterKnife.bind(this);

        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);
        hours = getIntent().getIntExtra("hours", 0);
        minutes = getIntent().getIntExtra("minutes", 0);

        this.date.setText(String.format("%1$s-%2$s-%3$s %4$s:%5$s", day, month, year, hours, minutes));
        itemTime.setText(String.format("%1$s-%2$s-%3$s %4$s:%5$s", day, month, year, hours, minutes));
    }
}
