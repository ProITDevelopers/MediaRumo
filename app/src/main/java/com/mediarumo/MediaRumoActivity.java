package com.mediarumo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.mediarumo.fragments.FragConcurso;
import com.mediarumo.fragments.FragHomeInicial;
import com.mediarumo.fragments.FragMediaRumo;
import com.mediarumo.fragments.FragMercado;
import com.mediarumo.fragments.FragVanguarda;
import com.mediarumo.loginRegister.LoginActivity;
import com.squareup.picasso.Picasso;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;

public class MediaRumoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    CircleImageView circleImageView;
    TextView txtName;
    TextView txtEmail;

    Usuario usuario;

    @BindView(R.id.logoutBtn)
    Button logoutBtn;

    @BindView(R.id.txtList)
    TextView txtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_rumo);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Rumo Store");
        setSupportActionBar(toolbar);


        txtList.setText("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        View navigationHeaderView = navigationView.getHeaderView(0);



        circleImageView = (CircleImageView) navigationHeaderView.findViewById(R.id.profile_pic);
        txtName = (TextView) navigationHeaderView.findViewById(R.id.txtName);
        txtEmail = (TextView) navigationHeaderView.findViewById(R.id.txtEmail);

        usuario = AppDatabase.getUser();

        loaduserProfile(usuario);



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logOut();
                AppDatabase.clearData();
                AppPref.getInstance().clearData();

                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }
        });

//        renderProducts();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragHomeInicial()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void loaduserProfile(Usuario usuario){

        Picasso.with(MediaRumoActivity.this)
                .load(usuario.getUsuarioPic())
                .placeholder(R.mipmap.ic_launcher)
                .into(circleImageView);


        txtName.setText(usuario.getUsuarioNome());
        txtEmail.setText(usuario.getUsuarioEmail());

    }

//    private void renderProducts() {
//        RealmResults<Product> products = AppDatabase.getProducts();
//        txtList.setText(products.toString());
//    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.media_rumo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logOut();
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            FragHomeInicial fragHomeInicial = new FragHomeInicial();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragHomeInicial);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_concurso) {

            FragConcurso fragConcurso = new FragConcurso();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragConcurso);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }



        else if (id == R.id.nav_mercado) {

            FragMercado fragMercado = new FragMercado();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMercado);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_vanguarda) {

            FragVanguarda fragVanguarda = new FragVanguarda();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragVanguarda);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_rumo) {

            FragMediaRumo fragMediaRumo = new FragMediaRumo();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,fragMediaRumo);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_instagram) {

            openInstagram(Common.SOCIAL_INSTAGRAM);

        } else if (id == R.id.nav_facebook) {
            openFbUrl(Common.SOCIAL_FACEBOOK);

        } else if (id == R.id.nav_linkedin) {
            openLinkedInUrl(Common.SOCIAL_LINKEDIN);

        } else if (id == R.id.nav_google) {
            openWebUrlInChrome(Common.SOCIAL_BROWSER);

        } else if (id == R.id.nav_share) {
            shareTheApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut(){
        LoginManager.getInstance().logOut();
        AppDatabase.clearData();
        AppPref.getInstance().clearData();

        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void changeStatusBarColor(int color) {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(color);
        }
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

    protected void openFbUrl(String username){
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/" + username)));
    }



    protected void openLinkedInUrl(String username) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/company/" + username)));

    }



    protected void openInstagram(String username) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + username);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + username)));
        }
    }

    protected void openWebUrlInChrome(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    //Sharing the app
    private void shareTheApp() {

        final String appPackageName = getPackageName();
        String appName = getString(R.string.app_name);
        String appCategory = Common.APP_CATEGORY;

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String postData = "Get " + appName + "for latest " + appCategory + "+News: https://play.google.com/store/apps/details?id=" + appPackageName;

        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Now!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, postData);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Post Via"));
    }
}
