package com.depromeet.donkey.join.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.depromeet.donkey.R;
import com.depromeet.donkey.join.data.Member;
import com.depromeet.donkey.join.presenter.JoinContract;
import com.depromeet.donkey.join.presenter.JoinPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinActivity extends AppCompatActivity implements JoinContract.View {
    private static final String TAG = JoinActivity.class.getSimpleName();

    @BindView(R.id.join_id_edit)
    EditText editId;
    @BindView(R.id.join_pw_edit)
    EditText editPw;

    private JoinPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        ButterKnife.bind(this);
        presenter = new JoinPresenter(this);
        presenter.attachView(this);
    }

    @OnClick(R.id.join_button)
    public void onJoinClick() {
        String id = editId.getText().toString();
        String pw = editPw.getText().toString();
        if (id.equals("")) {
            toast("아이디를 입력해 주세요.");
            return;
        }

        if (pw.equals("")) {
            toast("비밀번호를 입력해 주세요.");
            return;
        }
        presenter.connect(new Member("0", id, pw));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void joinSuccess() {
        toast("회원가입이 완료되었습니다.");
        finish();
    }

    @Override
    public void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(JoinActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.join_actionbar, null);
        actionBar.setCustomView(view);
        actionBar.setBackgroundDrawable(getDrawable(R.color.colorWhite));
        /*ImageView buttonBack = view.findViewById(R.id.join_actionbar_back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        return true;
    }
}
