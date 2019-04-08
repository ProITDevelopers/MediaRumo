package com.mediarumo.loginRegister;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mediarumo.AppDatabase;
import com.mediarumo.AppPref;
import com.mediarumo.BaseActivity;
import com.mediarumo.MainActivity;
import com.mediarumo.NoInternetActivity;
import com.mediarumo.R;
import com.mediarumo.SplashActivity;
import com.mediarumo.Usuario;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity  extends BaseActivity {

    @BindView(R.id.loginBtn)
    LoginButton loginButton;

    @BindView(R.id.fb)
    Button fb;

    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;

    @BindView(R.id.btn_create_account)
    AppCompatButton btn_create_account;

    CallbackManager callbackManager;

    AccessToken accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.statuscolor));
        hideToolbar();


        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        fb.setEnabled(true);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
                fb.setEnabled(false);
//                loader.setVisibility(View.VISIBLE);

            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               accessToken = loginResult.getAccessToken();

               fb.setEnabled(false);
               loader.setVisibility(View.VISIBLE);

               if (!isConnected(10000)) {
                   loader.setVisibility(View.INVISIBLE);
                   LoginManager.getInstance().logOut();
                   Toast.makeText(LoginActivity.this, "Check Net", Toast.LENGTH_SHORT).show();
                   fb.setEnabled(true);
                   Intent intent = new Intent(LoginActivity.this, NoInternetActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
               } else{
                   loaduserProfile(accessToken);
               }


            }

            @Override
            public void onCancel() {
                loader.setVisibility(View.INVISIBLE);
                fb.setEnabled(true);

            }

            @Override
            public void onError(FacebookException error) {
                loader.setVisibility(View.INVISIBLE);
                fb.setEnabled(true);
                Toast.makeText(LoginActivity.this, "FacebookException: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.btn_create_account)
    void onCreateAccountClick() {

        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void loaduserProfile(AccessToken newAccessToken){

        loader.setVisibility(View.VISIBLE);

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String name = first_name + " "+last_name;

                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";

                    Usuario user = new Usuario(id,email,name,image_url);

                    AppDatabase.saveUser(user);

                    AppPref.getInstance().saveAuthToken(newAccessToken);
                    launchSplash(LoginActivity.this);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private boolean isConnected(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }

}
