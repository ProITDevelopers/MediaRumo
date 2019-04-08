package com.mediarumo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mediarumo.loginRegister.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.loginBtn)
    LoginButton loginButton;

    @BindView(R.id.logoutBtn)
    Button logoutBtn;



    @BindView(R.id.profile_pic)
    CircleImageView circleImageView;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    CallbackManager callbackManager;

    AccessToken accessToken;

    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        changeStatusBarColor();

        usuario = AppDatabase.getUser();

        loaduserProfile(usuario);

//
//
//        callbackManager = CallbackManager.Factory.create();
//        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//
//
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase.clearData();
                AppPref.getInstance().clearData();
                LoginManager.getInstance().logOut();
                txtName.setText("");
                txtEmail.setText("");
                circleImageView.setImageResource(0);
                launchLogin(MainActivity.this);

            }
        });

//        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//
//                if (currentAccessToken==null){
//                    AppDatabase.clearData();
//                    AppPref.getInstance().clearData();
//                    txtName.setText("");
//                    txtEmail.setText("");
//                    circleImageView.setImageResource(0);
//                    launchLogin(MainActivity.this);
//                    Toast.makeText(MainActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
//                }else {
//                    loaduserProfile();
//                }
//            }
//        };





    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }








    private void loaduserProfile(Usuario usuario){

        Glide.with(MainActivity.this)
                .load(usuario.getUsuarioPic())
                .into(circleImageView);
        txtName.setText(usuario.getUsuarioNome());
        txtEmail.setText(usuario.getUsuarioEmail());

    }
}
