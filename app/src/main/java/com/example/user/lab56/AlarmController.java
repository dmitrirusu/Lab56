package com.example.user.lab56;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmController extends AppCompatActivity {

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    @OnClick(R.id.set_alarm)
    public void setAlarm() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        selectedYear = year;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;
                        showTimePickerDialog();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void showTimePickerDialog() {
        Date date = new Date();
        int hours = date.getHours();
        int minutes = date.getMinutes();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        Intent intent = new Intent(AlarmController.this, SetAlarm.class);
                        intent.putExtra("year", selectedYear);
                        intent.putExtra("month", selectedMonth);
                        intent.putExtra("day", selectedDay);
                        intent.putExtra("minutes", minute);
                        intent.putExtra("hours", hourOfDay);
                        startActivity(intent);
                    }
                }, hours, minutes, false);
        timePickerDialog.show();
    }

    @OnClick(R.id.your_profile)
    public void openProfile() {
        startActivity(new Intent(this, Profile.class));
    }

    @OnClick(R.id.schedule_list)
    public void openSheduleList() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        selectedYear = year;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;
                        Intent intent = new Intent(AlarmController.this, ScheduleList.class);
                        intent.putExtra("year", selectedYear);
                        intent.putExtra("month", selectedMonth);
                        intent.putExtra("day", selectedDay);
                        startActivity(intent);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    @OnClick(R.id.alarm_a_friend)
    public void alarmAFriend() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_controller);
        ButterKnife.bind(this);
    }
}
