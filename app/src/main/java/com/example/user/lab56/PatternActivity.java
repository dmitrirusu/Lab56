package com.example.user.lab56;

import android.app.KeyguardManager;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.eftimoff.patternview.PatternView;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatternActivity extends AppCompatActivity {

    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.patternView)
    PatternView patternView;

    private boolean setPattern;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);
        ButterKnife.bind(this);
        if(getIntent().getExtras().containsKey("day")) {
            int day = getIntent().getIntExtra("day", 0);
            this.day.setText(String.valueOf(day));
            int month = getIntent().getIntExtra("month", 0);
            this.month.setText(String.valueOf(month));
            int hours = getIntent().getIntExtra("hours", 0);
            int minutes = getIntent().getIntExtra("minutes", 0);
            time.setText(String.format("%1$d:%2$d", hours, minutes));
        } else {

            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "INFO");
            wl.acquire();

            KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("name");
            kl.disableKeyguard();

            mp = MediaPlayer.create(PatternActivity.this, R.raw.wake_up);
            mp.start();


            long time = getIntent().getExtras().getLong("time");
            Date date = new Date(time);
            int day = date.getDay();
            this.day.setText(String.valueOf(day));
            int month = date.getMonth();
            this.month.setText(String.valueOf(month));
            int hours = date.getHours();
            int minutes = date.getMinutes();
            this.time.setText(String.format("%1$d:%2$d", hours, minutes));
        }
        setPattern = getIntent().getBooleanExtra("setPattern", false);
        patternView.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
            @Override
            public void onPatternDetected() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PatternActivity.this);
                if(setPattern) {
                    preferences.edit().putString("pattern", patternView.getPatternString()).apply();
                    finish();
                } else if(preferences.getString("pattern", "").equals(patternView.getPatternString())) {
                    mp.stop();
                    mp.release();
                    finish();
                } else {
                    Toast.makeText(PatternActivity.this, "Wrong Pattern", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
