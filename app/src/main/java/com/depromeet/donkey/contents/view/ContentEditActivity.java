package com.depromeet.donkey.contents.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.depromeet.donkey.R;

import java.util.Date;

public class ContentEditActivity extends AppCompatActivity {

    private TextView tvConfirm;
    private EditText etTitle;
    private EditText etDetail;
    private Date today;
    private static int RED = Color.rgb(255, 79, 78);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_activity);

        tvConfirm = findViewById(R.id.write_confirm);
        etTitle = findViewById(R.id.write_title);
        etDetail = findViewById(R.id.write_detail);

        today = new Date();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.write_confirm :
                writeConfirm();
                break;
        }
    }

    public void writeConfirm() {

        if (etTitle.getText().toString().length() == 0) {
            etTitle.setHintTextColor(RED);
            Log.i("card title", "타이틀누락");
        }
        else if (etDetail.getText().toString().length() == 0) {
            etDetail.setHintTextColor(RED);
            Log.i("card detail", "내용누락");
        }
        else {
            ///////////////데이터저장저장
            etTitle.setText("완료누름ㅎ_ㅎ");
            String title = etTitle.getText().toString();
            String detail = etDetail.getText().toString();
            Log.i("card confirm", "완료 구웃");
        }
    }
}
