package shadowws.in.mycinemachance.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.github.demono.AutoScrollViewPager;


import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.SubMenu;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.libizo.CustomEditText;
import com.pixplicity.easyprefs.library.Prefs;
import com.tapadoo.alerter.Alerter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.GuestDataAdapter;
import shadowws.in.mycinemachance.adapter.MemberDataAdapter;
import shadowws.in.mycinemachance.adapter.MemberWantedAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.CustomFont;
import shadowws.in.mycinemachance.response.DirectorPasswordResponse;
import shadowws.in.mycinemachance.response.GuestDataResponse;
import shadowws.in.mycinemachance.response.MemberDataResponse;
import shadowws.in.mycinemachance.response.MemberPasswordResponse;
import shadowws.in.mycinemachance.response.MemberWantedResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;
import thebat.lib.validutil.ValidUtils;


public class MemberHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Connection.Receiver {

    AutoScrollViewPager slider;
    MemberWantedAdapter memberWantedAdapter;
    ProgressLoadingJIGB progress;
    MemberDataAdapter memberDataAdapter;
    boolean doubleBackToExitPressedOnce = false;
    RecyclerView rvMember;
    RecyclerView.LayoutManager layoutManager;
    ValidUtils validUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home);
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
        View headerView = navigationView.getHeaderView(0);
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
        String name1 = Prefs.getString("lfname", null);
        String name2 = Prefs.getString("llname", null);
        String category = Prefs.getString("lcategory", null);
        TextView navName = headerView.findViewById(R.id.mem_nav_name);
        TextView navCategory = headerView.findViewById(R.id.mem_nav_cat);
        CircleImageView navImage = headerView.findViewById(R.id.mem_nav_iv);
        if (!checkNullOrEmpty(name1) && !checkNullOrEmpty(category)){
            if (name2.matches(name1)){
                navName.setText(name1);
                navCategory.setText(category);
            }else {
                navName.setText(name1+"\t"+name2);
                navCategory.setText(category);
            }

        }else {
            navName.setText("My Cinema Chance");
            navCategory.setText("");
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        validUtils = new ValidUtils();

        slider = findViewById(R.id.mem_wanted_slider);
        rvMember = findViewById(R.id.rv_mem);
        //wantedLayout = findViewById(R.id.wanted_layout);
        layoutManager = new LinearLayoutManager(this);
        rvMember.setHasFixedSize(false);
        rvMember.setNestedScrollingEnabled(true);
        rvMember.setLayoutManager(layoutManager);

        member();

    }

    private void memberConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberHomeActivity.this)
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

            progress.startLoadingJIGB(MemberHomeActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberHomeActivity.this).create(RetrofitAPI.class);
            Call<MemberWantedResponse> call = api.memberWanted();

            call.enqueue(new Callback<MemberWantedResponse>() {
                @Override
                public void onResponse(Call<MemberWantedResponse> call, Response<MemberWantedResponse> response) {

                    try{

                        if (response.isSuccessful()){

                            MemberWantedResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();

                                if (error == false){

                                    List<MemberWantedResponse.Data> results = data.getData();

                                    if (results.size() > 0){

                                        memberWantedAdapter = new MemberWantedAdapter(MemberHomeActivity.this, results);
                                        slider.setAdapter(memberWantedAdapter);
                                        slider.startAutoScroll();
                                    }
                                }else {

                                    Alerter.create(MemberHomeActivity.this)
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

                                Alerter.create(MemberHomeActivity.this)
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

                        Alerter.create(MemberHomeActivity.this)
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
                public void onFailure(Call<MemberWantedResponse> call, Throwable t) {

                    call.cancel();
                    Alerter.create(MemberHomeActivity.this)
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
            });

            RetrofitAPI api1 = RetrofitBASE.getRetrofitInstance(this).create(RetrofitAPI.class);
            Call<MemberDataResponse> call1 = api1.memberData();

            call1.enqueue(new Callback<MemberDataResponse>() {
                @Override
                public void onResponse(Call<MemberDataResponse> call, Response<MemberDataResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            MemberDataResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();

                                if (error == false){

                                    progress.finishLoadingJIGB(MemberHomeActivity.this);

                                    List<MemberDataResponse.Data> results = data.getData();
                                    memberDataAdapter = new MemberDataAdapter(MemberHomeActivity.this, results);
                                    rvMember.setAdapter(memberDataAdapter);

                                }else {

                                    progress.finishLoadingJIGB(MemberHomeActivity.this);
                                    Alerter.create(MemberHomeActivity.this)
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

                                progress.finishLoadingJIGB(MemberHomeActivity.this);
                                Alerter.create(MemberHomeActivity.this)
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

                        progress.finishLoadingJIGB(MemberHomeActivity.this);
                        Alerter.create(MemberHomeActivity.this)
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
                public void onFailure(Call<MemberDataResponse> call, Throwable t) {

                    if (t.getMessage().equalsIgnoreCase("connect timed out")){

                        progress.finishLoadingJIGB(MemberHomeActivity.this);
                        call.cancel();
                        member();

                    }else {

                        call.cancel();
                        progress.finishLoadingJIGB(MemberHomeActivity.this);
                        Alerter.create(MemberHomeActivity.this)
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


    private void member() {
        boolean isConnected = Connection.isConnected();
        memberConnected(isConnected);
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
                Alerter.create(MemberHomeActivity.this)
                        .setTitle("Information :")
                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                        .setText("Click BACK Again to Exit")
                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
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
        getMenuInflater().inflate(R.menu.member_home, menu);
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
            member();
        }else if (id == R.id.nav_profile){
            Intent intent = new Intent(MemberHomeActivity.this, MemberProfileActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(MemberHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_change){
           changePassword();
        }else if (id == R.id.nav_about){
            Intent intent = new Intent(MemberHomeActivity.this, MemberAboutActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(MemberHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

        View view = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        Button btnYes = dialog.findViewById(R.id.logout_btn);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Prefs.clear();
                Intent intent = new Intent(MemberHomeActivity.this, LoginActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(MemberHomeActivity.this);
                startActivity(intent, options.toBundle());
                finish();
            }
        });
        dialog.show();
    }

    private void changePassword() {

        View view = getLayoutInflater().inflate(R.layout.password_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        Button btnChange = dialog.findViewById(R.id.pass_btn_change);
        CustomEditText etOld = dialog.findViewById(R.id.pass_et_opass);
        CustomEditText etNew = dialog.findViewById(R.id.pass_et_npass);
        CustomEditText etConfirmNew = dialog.findViewById(R.id.pass_et_ncpass);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validUtils.validateEditTexts(etOld, etNew, etConfirmNew)){

                    String oldPass = etOld.getText().toString().trim();
                    String newPass = etNew.getText().toString().trim();
                    String confirmNewPass = etConfirmNew.getText().toString().trim();
                    String mobile = Prefs.getString("lmobile", null);

                    if (confirmNewPass.matches(newPass)){

                        progress.startLoadingJIGB(MemberHomeActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

                        RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberHomeActivity.this).create(RetrofitAPI.class);
                        Call<MemberPasswordResponse> call = api.updateMemberPassword(oldPass, confirmNewPass, mobile);

                        call.enqueue(new Callback<MemberPasswordResponse>() {
                            @Override
                            public void onResponse(Call<MemberPasswordResponse> call, Response<MemberPasswordResponse> response) {

                                try{

                                    if (response.isSuccessful()){

                                        MemberPasswordResponse data = response.body();

                                        if (data != null){

                                            boolean error = data.getError();
                                            String message = data.getMessage();

                                            if (error == false){

                                                progress.finishLoadingJIGB(MemberHomeActivity.this);
                                                Alerter.create(MemberHomeActivity.this)
                                                        .setTitle("Response Success :")
                                                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                                        .setText(message)
                                                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                                                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                                        .setIcon(R.drawable.ic_info)
                                                        .setIconColorFilter(0)
                                                        .setBackgroundColorRes(R.color.colorSuccess)
                                                        .show();

                                                finish();
                                                startActivity(getIntent());
                                            }else {

                                                progress.finishLoadingJIGB(MemberHomeActivity.this);
                                                Alerter.create(MemberHomeActivity.this)
                                                        .setTitle("Response Error :")
                                                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                                        .setText(message)
                                                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                                                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                                        .setIcon(R.drawable.ic_info)
                                                        .setIconColorFilter(0)
                                                        .setBackgroundColorRes(R.color.colorError)
                                                        .show();
                                            }

                                        }else {

                                            progress.finishLoadingJIGB(MemberHomeActivity.this);
                                            Alerter.create(MemberHomeActivity.this)
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

                                    progress.finishLoadingJIGB(MemberHomeActivity.this);
                                    Alerter.create(MemberHomeActivity.this)
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
                            public void onFailure(Call<MemberPasswordResponse> call, Throwable t) {

                                call.cancel();
                                progress.finishLoadingJIGB(MemberHomeActivity.this);
                                Alerter.create(MemberHomeActivity.this)
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
                        });


                    }else {

                        Alerter.create(MemberHomeActivity.this)
                                .setTitle("Validation Error :")
                                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                .setText("Password Don't Match")
                                .setTextAppearance(R.style.AlertTextAppearance_Text)
                                .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                .setIcon(R.drawable.ic_info)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(R.color.colorWarning)
                                .show();
                    }

                }else {

                    Alerter.create(MemberHomeActivity.this)
                            .setTitle("Validation Error :")
                            .setTitleAppearance(R.style.AlertTextAppearance_Title)
                            .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                            .setText("Feilds are Empty")
                            .setTextAppearance(R.style.AlertTextAppearance_Text)
                            .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                            .setIcon(R.drawable.ic_info)
                            .setIconColorFilter(0)
                            .setBackgroundColorRes(R.color.colorWarning)
                            .show();
                }
            }
        });
        dialog.show();
    }

    private void fontMenu(MenuItem subMenuItem) {
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_regular.ttf");
        SpannableString mNewTitle = new SpannableString(subMenuItem.getTitle());
        mNewTitle.setSpan(new CustomFont("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        subMenuItem.setTitle(mNewTitle);
    }

    public static boolean checkNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        memberConnected(isConnected);
    }
}
