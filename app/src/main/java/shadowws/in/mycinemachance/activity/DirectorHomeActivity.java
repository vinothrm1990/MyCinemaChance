package shadowws.in.mycinemachance.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.SubMenu;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.tapadoo.alerter.Alerter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.DirectorDataAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.CustomFont;
import shadowws.in.mycinemachance.other.PaginationScrollListener;
import shadowws.in.mycinemachance.response.DirectorDataResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class DirectorHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Connection.Receiver {

    boolean doubleBackToExitPressedOnce = false;
    SwipeRefreshLayout refreshLayout;
    ProgressLoadingJIGB progress;
    RecyclerView rvDirector;
    LinearLayoutManager layoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    RetrofitAPI api;
    DirectorDataAdapter directorDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_home);
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

        rvDirector = findViewById(R.id.rv_director);
        refreshLayout = findViewById(R.id.swipe_director);

        directorDataAdapter = new DirectorDataAdapter(DirectorHomeActivity.this);
        layoutManager = new LinearLayoutManager(this);
        rvDirector.setLayoutManager(layoutManager);
        rvDirector.setAdapter(directorDataAdapter);

        director();
    }

    private void directorConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(DirectorHomeActivity.this)
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

            api = RetrofitBASE.getRetrofitInstance(this).create(RetrofitAPI.class);
            refreshLayout.setOnRefreshListener(this::onRefresh);

            rvDirector.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    currentPage += 1;
                    loadNextPage();
                }

                @Override
                public int getTotalPageCount() {
                    return TOTAL_PAGES;
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadFirstPage();
                }
            }, 1000);

        }
    }

    private void loadFirstPage() {

        progress.startLoadingJIGB(DirectorHomeActivity.this,
                R.raw.progress, "Please Wait...",
                0,300,300);

        callDirectorData().enqueue(new Callback<DirectorDataResponse>() {
            @Override
            public void onResponse(Call<DirectorDataResponse> call, Response<DirectorDataResponse> response) {

                try {

                    if (response.isSuccessful()) {

                        DirectorDataResponse data = response.body();

                        if (data != null){

                            boolean error = data.getError();

                            if (error == false){

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorHomeActivity.this);

                                List<DirectorDataResponse.User> results = fetchResults(response);

                                String totalPages = results.get(0).getTotal_pages();
                                TOTAL_PAGES = Integer.parseInt(totalPages);
                                directorDataAdapter.addAll(results);

                                if (currentPage >= TOTAL_PAGES){
                                    isLastPage = true;
                                    Toast.makeText(DirectorHomeActivity.this, "more page left", Toast.LENGTH_SHORT).show();
                                }

                            }else {

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorHomeActivity.this);
                                Alerter.create(DirectorHomeActivity.this)
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

                            refreshLayout.setRefreshing(false);
                            progress.finishLoadingJIGB(DirectorHomeActivity.this);
                            Alerter.create(DirectorHomeActivity.this)
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

                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorHomeActivity.this);
                    Alerter.create(DirectorHomeActivity.this)
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
            public void onFailure(Call<DirectorDataResponse> call, Throwable t) {

                if (t.getMessage().equalsIgnoreCase("connect timed out")){

                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorHomeActivity.this);
                    call.cancel();
                    loadFirstPage();

                }else {

                    call.cancel();
                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorHomeActivity.this);
                    Alerter.create(DirectorHomeActivity.this)
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

    private void loadNextPage() {

        progress.startLoadingJIGB(DirectorHomeActivity.this,
                R.raw.progress, "Please Wait...",
                0,300,300);

        callDirectorData().enqueue(new Callback<DirectorDataResponse>() {
            @Override
            public void onResponse(Call<DirectorDataResponse> call, Response<DirectorDataResponse> response) {

                try {

                    if (response.isSuccessful()) {

                        DirectorDataResponse data = response.body();

                        if (data != null){

                            boolean error = data.getError();

                            if (error == false){

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorHomeActivity.this);

                                List<DirectorDataResponse.User> results = fetchResults(response);
                                isLoading = false;
                                String totalPages = results.get(0).getTotal_pages();
                                TOTAL_PAGES = Integer.parseInt(totalPages);
                                directorDataAdapter.addAll(results);

                                if (currentPage == TOTAL_PAGES){
                                    isLastPage = true;
                                    Toast.makeText(DirectorHomeActivity.this, "last page reached", Toast.LENGTH_SHORT).show();
                                }

                            }else {

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorHomeActivity.this);
                                Alerter.create(DirectorHomeActivity.this)
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

                            refreshLayout.setRefreshing(false);
                            progress.finishLoadingJIGB(DirectorHomeActivity.this);
                            Alerter.create(DirectorHomeActivity.this)
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

                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorHomeActivity.this);
                    Alerter.create(DirectorHomeActivity.this)
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
            public void onFailure(Call<DirectorDataResponse> call, Throwable t) {

                if (t.getMessage().equalsIgnoreCase("connect timed out")){

                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorHomeActivity.this);
                    call.cancel();
                    loadNextPage();

                }else {

                    call.cancel();
                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorHomeActivity.this);
                    Alerter.create(DirectorHomeActivity.this)
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

    private Call<DirectorDataResponse> callDirectorData(){
        return api.directorData(
                currentPage
        );
    }

    private List<DirectorDataResponse.User> fetchResults(Response<DirectorDataResponse> response) {
        DirectorDataResponse dataResponse = response.body();
        return dataResponse.getUsers();
    }

    private void onRefresh() {
        progress.finishLoadingJIGB(DirectorHomeActivity.this);
        directorDataAdapter.getData().clear();
        directorDataAdapter.notifyDataSetChanged();
        loadFirstPage();
        refreshLayout.setRefreshing(false);
    }

    private void director() {
        boolean isConnected = Connection.isConnected();
        directorConnected(isConnected);
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
                Alerter.create(DirectorHomeActivity.this)
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
        getMenuInflater().inflate(R.menu.director_home, menu);
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
        }else if (id == R.id.nav_profile){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorProfileActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_post){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorPostActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_wishlist){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorWishlistActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_change){
            //changePassword();
        }else if (id == R.id.nav_member){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorMemberActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_feedback){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorFeedbackActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_about){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorAboutActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_exit){
            //logoutDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        directorConnected(isConnected);
    }

    private void fontMenu(MenuItem subMenuItem) {
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_regular.ttf");
        SpannableString mNewTitle = new SpannableString(subMenuItem.getTitle());
        mNewTitle.setSpan(new CustomFont("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        subMenuItem.setTitle(mNewTitle);
    }
}
