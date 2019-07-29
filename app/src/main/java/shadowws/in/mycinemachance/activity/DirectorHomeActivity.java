package shadowws.in.mycinemachance.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.libizo.CustomEditText;
import com.pixplicity.easyprefs.library.Prefs;
import com.tapadoo.alerter.Alerter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.CustomFont;
import shadowws.in.mycinemachance.response.DirectorPasswordResponse;
import shadowws.in.mycinemachance.response.DirectorPostResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;
import thebat.lib.validutil.ValidUtils;

public class DirectorHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Connection.Receiver {

    boolean doubleBackToExitPressedOnce = false;
    CustomEditText etName, etMobile, etEmail, etTitle, etDesc;
    Spinner spCategory, spLanguage;
    String strCategory, strLanguage;
    Button btnSubmit;
    ValidUtils validUtils;
    ProgressLoadingJIGB progress;
    ArrayList<String> languageList, categoryList;
    ArrayAdapter<String> adapter1, adapter2;
    String name, mobile, email;

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
        String name1 = Prefs.getString("lname", null);
        String category = Prefs.getString("lcategory", null);
        TextView navName = headerView.findViewById(R.id.dir_nav_name);
        TextView navCategory = headerView.findViewById(R.id.dir_nav_cat);
        if (!checkNullOrEmpty(name1) && !checkNullOrEmpty(category)){
            navName.setText(name1);
            navCategory.setText(category);
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

        etName = findViewById(R.id.post_et_name);
        etMobile = findViewById(R.id.post_et_phone);
        etEmail = findViewById(R.id.post_et_email);
        etTitle = findViewById(R.id.post_et_title);
        etDesc = findViewById(R.id.post_et_desc);
        spCategory = findViewById(R.id.post_spin_category);
        spLanguage = findViewById(R.id.post_spin_language);
        btnSubmit = findViewById(R.id.post_btn_submit);


        languageList = new ArrayList<>();
        languageList.add("Tamil");
        languageList.add("English");
        languageList.add("Malayalam");
        languageList.add("Telugu");
        languageList.add("Kannada");
        languageList.add("Hindi");
        languageList.add("Other");

        adapter1 = new ArrayAdapter<String>(DirectorHomeActivity.this,
                android.R.layout.simple_spinner_dropdown_item, languageList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage.setAdapter(adapter1);

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strLanguage = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categoryList = new ArrayList<>();
        categoryList.add("Acting");
        categoryList.add("Direction");
        categoryList.add("Cinematography");
        categoryList.add("Editing");
        categoryList.add("Stunt");
        categoryList.add("Makeup");
        categoryList.add("Costume");
        categoryList.add("Art");
        categoryList.add("Music");
        categoryList.add("Sound");
        categoryList.add("Singer");
        categoryList.add("Lyricist");
        categoryList.add("Dance");
        categoryList.add("Public Relation");
        categoryList.add("DI VFX");
        categoryList.add("Designer");
        categoryList.add("Visual Effects");
        categoryList.add("Animation");
        categoryList.add("Stills");
        categoryList.add("Dubbing & Mimicry");
        categoryList.add("Short Film");
        categoryList.add("Dubsmash");

        adapter2 = new ArrayAdapter<String>(DirectorHomeActivity.this,
                android.R.layout.simple_spinner_dropdown_item, categoryList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter2);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCategory = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        name = Prefs.getString("lname", null);
        email = Prefs.getString("lemail", null);
        mobile = Prefs.getString("lmobile", null);

        if (!checkNullOrEmpty(name) && !checkNullOrEmpty(email) && !checkNullOrEmpty(mobile)) {
            etName.setText(name);
            etMobile.setText(mobile);
            etEmail.setText(email);
        }else {
            etName.setText("");
            etMobile.setText("");
            etEmail.setText("");
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                director();
            }
        });
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


            if (validUtils.validateEditTexts(etName, etEmail, etMobile, etTitle, etDesc)){

                String did = String.valueOf(Prefs.getInt("lid", 0));
                String name = etName.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String title = etTitle.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();
                String category = spCategory.getSelectedItem().toString();
                String language = spLanguage.getSelectedItem().toString();

                progress.startLoadingJIGB(DirectorHomeActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

                RetrofitAPI api = RetrofitBASE.getRetrofitInstance(DirectorHomeActivity.this).create(RetrofitAPI.class);
                Call<DirectorPostResponse> call = api.directorPost(name, mobile, email, language, category, title, desc, did, 0);

                call.enqueue(new Callback<DirectorPostResponse>() {
                    @Override
                    public void onResponse(Call<DirectorPostResponse> call, Response<DirectorPostResponse> response) {

                        try {

                            if (response.isSuccessful()){

                                DirectorPostResponse data = response.body();

                                if (data != null){

                                    boolean error = data.getError();
                                    String message = data.getMessage();

                                    if (error == false){

                                        progress.finishLoadingJIGB(DirectorHomeActivity.this);
                                        Alerter.create(DirectorHomeActivity.this)
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


                                        etName.setText("");
                                        etMobile.setText("");
                                        etEmail.setText("");
                                        etTitle.setText("");
                                        etDesc.setText("");

                                    }else {

                                        progress.finishLoadingJIGB(DirectorHomeActivity.this);
                                        Alerter.create(DirectorHomeActivity.this)
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

                        }catch (Exception e) {

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
                    public void onFailure(Call<DirectorPostResponse> call, Throwable t) {

                        call.cancel();
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
                });

            }else {

                Alerter.create(DirectorHomeActivity.this)
                        .setTitle("Validation Error :")
                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                        .setText("Mandatory Feilds are Empty")
                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                        .setIcon(R.drawable.ic_info)
                        .setIconColorFilter(0)
                        .setBackgroundColorRes(R.color.colorWarning)
                        .show();
            }
        }
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
        }else if (id == R.id.nav_change){
            changePassword();
        }else if (id == R.id.nav_member){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorRequestedActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_about){
            Intent intent = new Intent(DirectorHomeActivity.this, DirectorAboutActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
            startActivity(intent, options.toBundle());
        }else if (id == R.id.nav_exit){
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
                Intent intent = new Intent(DirectorHomeActivity.this, LoginActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectorHomeActivity.this);
                startActivity(intent, options.toBundle());
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

                        progress.startLoadingJIGB(DirectorHomeActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

                        RetrofitAPI api = RetrofitBASE.getRetrofitInstance(DirectorHomeActivity.this).create(RetrofitAPI.class);
                        Call<DirectorPasswordResponse> call = api.updatePassword(oldPass, confirmNewPass, mobile);

                        call.enqueue(new Callback<DirectorPasswordResponse>() {
                            @Override
                            public void onResponse(Call<DirectorPasswordResponse> call, Response<DirectorPasswordResponse> response) {

                                try{

                                    if (response.isSuccessful()){

                                        DirectorPasswordResponse data = response.body();

                                        if (data != null){

                                            boolean error = data.getError();
                                            String message = data.getMessage();

                                            if (error == false){

                                                progress.finishLoadingJIGB(DirectorHomeActivity.this);
                                                Alerter.create(DirectorHomeActivity.this)
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

                                                progress.finishLoadingJIGB(DirectorHomeActivity.this);
                                                Alerter.create(DirectorHomeActivity.this)
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
                            public void onFailure(Call<DirectorPasswordResponse> call, Throwable t) {

                                call.cancel();
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
                        });


                    }else {

                        Alerter.create(DirectorHomeActivity.this)
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

                    Alerter.create(DirectorHomeActivity.this)
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

    public static boolean checkNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
