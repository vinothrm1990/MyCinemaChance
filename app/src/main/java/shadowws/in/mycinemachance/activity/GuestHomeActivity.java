package shadowws.in.mycinemachance.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.SubMenu;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.tapadoo.alerter.Alerter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.GuestCategoryAdapter;
import shadowws.in.mycinemachance.adapter.GuestDataAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.CustomFont;
import shadowws.in.mycinemachance.other.MCC;
import shadowws.in.mycinemachance.response.GuestCategoryResponse;
import shadowws.in.mycinemachance.response.GuestDataResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class GuestHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Connection.Receiver {

    boolean doubleBackToExitPressedOnce = false;
    GifImageView ivRegister;
    ProgressLoadingJIGB progress;
    RecyclerView rvCategory, rvGuest;
    RecyclerView.LayoutManager layoutManager1, layoutManager2;
    GuestDataAdapter guestDataAdapter;
    GuestCategoryAdapter guestCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().setAction("created");
        setContentView(R.layout.activity_guest_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("MY CINEMA CHANCE");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(title);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    fontMenu(subMenuItem);
                }
            }
            fontMenu(menuItem);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ivRegister = findViewById(R.id.gif_iv);
        rvGuest = findViewById(R.id.rv_guest);
        rvCategory = findViewById(R.id.rv_category);

        layoutManager1 = new LinearLayoutManager(this);
        rvGuest.setHasFixedSize(true);
        rvGuest.setNestedScrollingEnabled(true);
        rvGuest.setLayoutManager(layoutManager1);
        layoutManager2 = new GridLayoutManager(this, 2);
        rvCategory.setHasFixedSize(true);
        rvCategory.setNestedScrollingEnabled(true);
        rvCategory.setLayoutManager(layoutManager2);

        ivRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GuestHomeActivity.this, RegisterActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(GuestHomeActivity.this);
                startActivity(intent, options.toBundle());
            }
        });

        guest();
        category();

    }

    private void guestConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(GuestHomeActivity.this)
                    .setTitle("Connection Error :")
                    .setTitleAppearance(R.style.AlertTextAppearance_Title)
                    .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                    .setText("No Internet Connection Available")
                    .setTextAppearance(R.style.AlertTextAppearance_Text)
                    .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                    .setIcon(R.drawable.no_internet)
                    .setIconColorFilter(0)
                    .setBackgroundColorRes(R.color.colorWarning)
                    .show();

        }else {

            progress.startLoadingJIGB(GuestHomeActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(this).create(RetrofitAPI.class);
            Call<GuestDataResponse> call = api.guestData();

            call.enqueue(new Callback<GuestDataResponse>() {
                @Override
                public void onResponse(Call<GuestDataResponse> call, Response<GuestDataResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            GuestDataResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();

                                if (error == false){

                                    progress.finishLoadingJIGB(GuestHomeActivity.this);

                                    List<GuestDataResponse.Data> results = data.getData();
                                    guestDataAdapter = new GuestDataAdapter(GuestHomeActivity.this, results);
                                    rvGuest.setAdapter(guestDataAdapter);

                                }else {

                                    progress.finishLoadingJIGB(GuestHomeActivity.this);
                                    Alerter.create(GuestHomeActivity.this)
                                            .setTitle("Response Error :")
                                            .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                            .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                            .setText("Something Went Wrong")
                                            .setTextAppearance(R.style.AlertTextAppearance_Text)
                                            .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                            .setIcon(R.drawable.ic_info)
                                            .setIconColorFilter(0)
                                            .setBackgroundColorRes(R.color.colorError)
                                            .show();
                                }

                            }else {

                                progress.finishLoadingJIGB(GuestHomeActivity.this);
                                Alerter.create(GuestHomeActivity.this)
                                        .setTitle("Null Exception :")
                                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                        .setText("No Data")
                                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                        .setIcon(R.drawable.ic_info)
                                        .setIconColorFilter(0)
                                        .setBackgroundColorRes(R.color.colorError)
                                        .show();
                            }

                        }

                    }catch (Exception e){

                        progress.finishLoadingJIGB(GuestHomeActivity.this);
                        Alerter.create(GuestHomeActivity.this)
                                .setTitle("Exception Caught :")
                                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                .setText(e.toString())
                                .setTextAppearance(R.style.AlertTextAppearance_Text)
                                .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                .setIcon(R.drawable.ic_info)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(R.color.colorError)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<GuestDataResponse> call, Throwable t) {

                    if (t.getMessage().equalsIgnoreCase("connect timed out")){

                        progress.finishLoadingJIGB(GuestHomeActivity.this);
                        call.cancel();
                        guest();

                    }else {

                        call.cancel();
                        progress.finishLoadingJIGB(GuestHomeActivity.this);
                        Alerter.create(GuestHomeActivity.this)
                                .setTitle("Exception Throwed :")
                                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                .setText(t.toString())
                                .setTextAppearance(R.style.AlertTextAppearance_Text)
                                .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                .setIcon(R.drawable.ic_info)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(R.color.colorError)
                                .show();
                    }


                }
            });

        }
    }

    private void categoryConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(GuestHomeActivity.this)
                    .setTitle("Connection Error :")
                    .setTitleAppearance(R.style.AlertTextAppearance_Title)
                    .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                    .setText("No Internet Connection Available")
                    .setTextAppearance(R.style.AlertTextAppearance_Text)
                    .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                    .setIcon(R.drawable.no_internet)
                    .setIconColorFilter(0)
                    .setBackgroundColorRes(R.color.colorWarning)
                    .show();

        }else {

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(this).create(RetrofitAPI.class);
            Call<GuestCategoryResponse> call = api.guestCategory();

            call.enqueue(new Callback<GuestCategoryResponse>() {
                @Override
                public void onResponse(Call<GuestCategoryResponse> call, Response<GuestCategoryResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            GuestCategoryResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();

                                if (error == false){

                                    List<GuestCategoryResponse.Data> results = data.getData();
                                    guestCategoryAdapter = new GuestCategoryAdapter(GuestHomeActivity.this, results);
                                    rvCategory.setAdapter(guestCategoryAdapter);

                                }else {

                                    Alerter.create(GuestHomeActivity.this)
                                            .setTitle("Response Error :")
                                            .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                            .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                            .setText("Something Went Wrong")
                                            .setTextAppearance(R.style.AlertTextAppearance_Text)
                                            .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                            .setIcon(R.drawable.ic_info)
                                            .setIconColorFilter(0)
                                            .setBackgroundColorRes(R.color.colorError)
                                            .show();
                                }

                            }else {

                                Alerter.create(GuestHomeActivity.this)
                                        .setTitle("Null Exception :")
                                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                        .setText("No Data")
                                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                        .setIcon(R.drawable.ic_info)
                                        .setIconColorFilter(0)
                                        .setBackgroundColorRes(R.color.colorError)
                                        .show();
                            }

                        }

                    }catch (Exception e){

                        Alerter.create(GuestHomeActivity.this)
                                .setTitle("Exception Caught :")
                                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                .setText(e.toString())
                                .setTextAppearance(R.style.AlertTextAppearance_Text)
                                .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                .setIcon(R.drawable.ic_info)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(R.color.colorError)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<GuestCategoryResponse> call, Throwable t) {

                    if (t.getMessage().equalsIgnoreCase("connect timed out")){

                        call.cancel();
                        guest();

                    }else {

                        call.cancel();
                        Alerter.create(GuestHomeActivity.this)
                                .setTitle("Exception Throwed :")
                                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                .setText(t.toString())
                                .setTextAppearance(R.style.AlertTextAppearance_Text)
                                .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                .setIcon(R.drawable.ic_info)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(R.color.colorError)
                                .show();
                    }
                }
            });
        }
    }

    private void guest() {
        boolean isConnected = Connection.isConnected();
        guestConnected(isConnected);
    }

    private void category() {
        boolean isConnected = Connection.isConnected();
        categoryConnected(isConnected);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                return;
            }else {

                this.doubleBackToExitPressedOnce = true;
                Alerter.create(GuestHomeActivity.this)
                        .setTitle("Info")
                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "avenir_bold.otf"))
                        .setText("Click BACK Again to Exit")
                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "avenir_light.otf"))
                        .setIcon(R.drawable.ic_info)
                        .setIconColorFilter(0)
                        .setBackgroundColorRes(R.color.colorInfo)
                        .show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 1500);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guest_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home){
            finish();
            startActivity(getIntent());
        }else if (id == R.id.nav_about){
            Intent intent = new Intent(GuestHomeActivity.this, GuestAboutActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(GuestHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_exit){
            exitDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exitDialog() {

        View view = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        Button btnExit = dialog.findViewById(R.id.exit_btn);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });
        dialog.show();

    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        guestConnected(isConnected);
        categoryConnected(isConnected);
    }

    @Override
    protected void onResume() {
        MCC.getInstance().setConnectivityListener(this);
        String action = getIntent().getAction();
        if(action == null || !action.equals("created")) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            getIntent().setAction(null);
        }
        super.onResume();
    }

    private void fontMenu(MenuItem subMenuItem) {
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_regular.ttf");
        SpannableString mNewTitle = new SpannableString(subMenuItem.getTitle());
        mNewTitle.setSpan(new CustomFont("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        subMenuItem.setTitle(mNewTitle);
    }
}
