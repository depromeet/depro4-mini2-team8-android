package com.depromeet.donkey.contents.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.depromeet.donkey.R;

public class ContentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        Intent intent=new Intent(this.getIntent());
        String title = intent.getStringExtra("title");

        TextView tvTitle = findViewById(R.id.content_title);
        TextView tvDday = findViewById(R.id.content_dday);
        TextView tvDetail = findViewById(R.id.content_detail);

        tvTitle.setText(title);
    }
}
