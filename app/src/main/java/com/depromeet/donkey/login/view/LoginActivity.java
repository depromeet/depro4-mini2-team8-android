package com.depromeet.donkey.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.depromeet.donkey.R;
import com.depromeet.donkey.join.view.JoinActivity;
import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.login.presenter.LoginContract;
import com.depromeet.donkey.login.presenter.LoginPresenter;
import com.depromeet.donkey.main.view.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    @BindView(R.id.login_button)
    TextView loginBtn;
    @BindView(R.id.login_id_edit)
    EditText idEdit;
    @BindView(R.id.login_pw_edit)
    EditText pwEdit;
    @BindView(R.id.login_id_check)
    TextView idCheck;
    @BindView(R.id.login_pw_check)
    TextView pwCheck;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        presenter.attachView(this);
    }

    @OnClick(R.id.login_button)
    public void loginClick() {
        String id = idEdit.getText().toString();
        String pw = pwEdit.getText().toString();
        if (id.equals("")) {
            idCheck.setVisibility(View.VISIBLE);
            return;
        }

        if (pw.equals("")) {
            pwCheck.setVisibility(View.VISIBLE);
            return;
        }
        presenter.requestLoginInfo(new Member(id, "123", pw));
    }

    @OnClick(R.id.login_join_button)
    public void joinClick() {
        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginFailure(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                idCheck.setVisibility(View.VISIBLE);
                pwCheck.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void loginSuccess(Member member) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("Member", member);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
