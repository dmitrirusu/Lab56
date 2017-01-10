package com.example.user.lab56;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    @BindView(R.id.profile_image)
    CircleImageView profileAvatar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.status)
    TextView status;

    @OnClick(R.id.status)
    public void changeStatus() {
        new MaterialDialog.Builder(this)
                .title("Enter Text")
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
                        status.setText(input);
                    }
                }).show();
    }

    @OnClick(R.id.btn_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.set_user_name)
    public void changeUserName() {
        new MaterialDialog.Builder(this)
                .title("Enter your username")
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
                        username.setText(input);
                    }
                }).show();
    }

    @OnClick(R.id.set_content)
    public void setAvatar() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    @OnClick(R.id.set_unlocker_pattern)
    public void setPattern() {
        Date date = new Date(System.currentTimeMillis());
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int month = date.getMonth();
        int day = date.getDate();
        Intent intent = new Intent(this, PatternActivity.class);
        intent.putExtra("day", day);
        intent.putExtra("month", month);
        intent.putExtra("hours", hours);
        intent.putExtra("minutes", minutes);
        intent.putExtra("setPattern", true);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        title.setText("Your Profile");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            profileAvatar.setImageBitmap((BitmapFactory.decodeFile(picturePath)));
        }
    }
}
