package com.mediarumo.loginRegister;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.mediarumo.BaseActivity;
import com.mediarumo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.btn_login_account)
    Button btn_login_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.statuscolor));
        hideToolbar();


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.btn_login_account)
    void onCreateAccountClick() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
