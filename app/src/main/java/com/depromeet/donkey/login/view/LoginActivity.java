package com.depromeet.donkey.login.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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
    @BindView(R.id.login_id_under)
    View idUnder;
    @BindView(R.id.login_pw_under)
    View pwUnder;

    private final int LOGIN_ID = R.id.login_id_edit;
    private final int LOGIN_PW = R.id.login_pw_edit;

    private LoginPresenter presenter;
    private int COLOR_NONE;
    private int COLOR_COMPLETE;
    private int COLOR_ACTIVE;
    private Drawable LOGIN_DISABLE;
    private Drawable LOGIN_ABLE;
    private boolean loginClickable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        presenter.attachView(this);
        setFocusStatus();
    }

    public void setFocusStatus() {
        COLOR_NONE = ContextCompat.getColor(LoginActivity.this, R.color.colorUnderbarNone);
        COLOR_COMPLETE = ContextCompat.getColor(LoginActivity.this, R.color.colorUnderComplete);
        COLOR_ACTIVE = ContextCompat.getColor(LoginActivity.this, R.color.colorUnderbarActiv);
        LOGIN_DISABLE = ContextCompat.getDrawable(LoginActivity.this, R.drawable.rectangle_gray);
        LOGIN_ABLE = ContextCompat.getDrawable(LoginActivity.this, R.drawable.rectangle_active);

        idEdit.addTextChangedListener(etMonitor);
        pwEdit.addTextChangedListener(etMonitor);
        idEdit.setOnFocusChangeListener(onFocus);
        pwEdit.setOnFocusChangeListener(onFocus);
    }

    @OnClick(R.id.login_button)
    public void loginClick() {

        if (!loginClickable)
            return;

        idEdit.clearFocus();
        pwEdit.clearFocus();

        String id = idEdit.getText().toString();
        String pw = pwEdit.getText().toString();
        /** 둘 다 입력했을때만 활성화 되므로 이건 추석처리하겠음
         if (id.equals("")) {
         idCheck.setVisibility(VISIBLE);
         return;
         }

         if (pw.equals("")) {
         pwCheck.setVisibility(VISIBLE);
         return;
         }
         *********************************************************/
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
                idCheck.setVisibility(VISIBLE);
                pwCheck.setVisibility(VISIBLE);
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

    // 에딧텍스트에 입력 감시
    TextWatcher etMonitor = new TextWatcher() {

        int idCnt, pwCnt;
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ((idCnt == 0 || pwCnt == 0) && (pwEdit.length() * idEdit.length() > 0)) {
                setLoginBtn(true);
                loginClickable = true;
                Log.i("****LOGIN BTN", "active");
            }
            if ((idCnt != 0 && pwCnt != 0) && (pwEdit.length() * idEdit.length() == 0)) {
                setLoginBtn(false);
                loginClickable = false;
                Log.i("****LOGIN BTN", "!active");
            }
            idCnt = idEdit.length();
            pwCnt = pwEdit.length();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void afterTextChanged(Editable s) {

        }
    };

    //에딧텍스트에 포커스 on 확인
    View.OnFocusChangeListener onFocus = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            View under = (v.getId() == LOGIN_ID) ? idUnder : pwUnder;
            TextView check = (v.getId() == LOGIN_PW) ? idCheck : pwCheck;

            if (hasFocus) {
                if (check.getVisibility() == VISIBLE) {
                    ((EditText)v).setText("");
                    check.setVisibility(INVISIBLE);
                }
                under.setBackgroundColor(COLOR_ACTIVE);
                Log.e("******gain focus", v.getTag().toString());
            }
            else {
                under.setBackgroundColor(((EditText)v).getText().length() == 0 ? COLOR_NONE : COLOR_COMPLETE);
                Log.e("******loose focus", v.getTag().toString());
            }
        }
    };

    public void setLoginBtn(boolean active) {
        loginBtn.setBackground(active ? LOGIN_ABLE : LOGIN_DISABLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
