package com.depromeet.donkey.join.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.depromeet.donkey.R;
import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.join.presenter.JoinContract;
import com.depromeet.donkey.join.presenter.JoinPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class JoinActivity extends AppCompatActivity implements JoinContract.View {
    private static final String TAG = JoinActivity.class.getSimpleName();

    @BindView(R.id.join_id_edit)
    EditText editId;
    @BindView(R.id.join_pw_edit)
    EditText editPw;
    @BindView(R.id.join_id_check)
    TextView idCheck;
    @BindView(R.id.join_pw_check)
    TextView pwCheck;
    @BindView(R.id.join_id_under)
    View idUnder;
    @BindView(R.id.join_pw_under)
    View pwUnder;
    @BindView(R.id.join_button)
    TextView joinBtn;

    private JoinPresenter presenter;
    private final int JOIN_ID = R.id.join_id_edit;
    private final int JOIN_PW = R.id.join_pw_edit;
    private int COLOR_NONE;
    private int COLOR_COMPLETE;
    private int COLOR_ACTIVE;
    private Drawable JOIN_DISABLE;
    private Drawable JOIN_ABLE;
    private boolean joinClickable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        ButterKnife.bind(this);
        presenter = new JoinPresenter(this);
        presenter.attachView(this);
        setFocusStatus();
    }

    public void setFocusStatus() {

        editId.addTextChangedListener(etMonitor);
        editPw.addTextChangedListener(etMonitor);
        editId.setOnFocusChangeListener(onFocus);
        editPw.setOnFocusChangeListener(onFocus);

        COLOR_NONE = ContextCompat.getColor(JoinActivity.this, R.color.colorUnderbarNone);
        COLOR_COMPLETE = ContextCompat.getColor(JoinActivity.this, R.color.colorUnderComplete);
        COLOR_ACTIVE = ContextCompat.getColor(JoinActivity.this, R.color.colorUnderbarActiv);
        JOIN_DISABLE = ContextCompat.getDrawable(JoinActivity.this, R.drawable.rectangle_gray);
        JOIN_ABLE = ContextCompat.getDrawable(JoinActivity.this, R.drawable.rectangle_active);
    }

    @OnClick(R.id.join_button)
    public void onJoinClick() {
        if (!joinClickable)
            return;

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

        presenter.connect(new Member(id, id, pw));
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

    // 에딧텍스트에 입력 감시
    TextWatcher etMonitor = new TextWatcher() {

        int idCnt, pwCnt;
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ((idCnt == 0 || pwCnt == 0) && (editPw.length() * editId.length() > 0)) {
                setJoinBtn(true);
                joinClickable = true;
                Log.i("****LOGIN BTN", "active");
            }
            if ((idCnt != 0 && pwCnt != 0) && (editPw.length() * editId.length() == 0)) {
                setJoinBtn(false);
                joinClickable = false;
                Log.i("****LOGIN BTN", "!active");
            }
            idCnt = editId.length();
            pwCnt = editPw.length();
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

            View under = (v.getId() == JOIN_ID) ? idUnder : pwUnder;
            TextView check = (v.getId() == JOIN_PW) ? idCheck : pwCheck;

            if (hasFocus) {
                if (check.getVisibility() == VISIBLE) {
                    ((EditText)v).setText("");
                    check.setVisibility(INVISIBLE);
                }
                under.setBackgroundColor(COLOR_ACTIVE);
            }
            else {
                under.setBackgroundColor(((EditText)v).getText().length() == 0 ? COLOR_NONE : COLOR_COMPLETE);
            }
        }
    };

    public void setJoinBtn(boolean active) {
        joinBtn.setBackground(active ? JOIN_ABLE : JOIN_DISABLE);
    }
}
