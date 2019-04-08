package com.mediarumo;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NoInternetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_no_internet);
        hideToolbar();
        changeStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));


        final Button retryBtn = findViewById(R.id.retry_btn);
        final ProgressBar retryProgress = findViewById(R.id.retry_progress);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //retryBtn.setEnabled(false);
                retryProgress.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isConnected(10000)) {
                            launchSplash(NoInternetActivity.this);
                        } else {
                            retryProgress.setVisibility(View.GONE);
                            retryBtn.setEnabled(true);
                            retryBtn.setText("TRY AGAIN");
                        }
                    }
                },2000);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_no_internet;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
