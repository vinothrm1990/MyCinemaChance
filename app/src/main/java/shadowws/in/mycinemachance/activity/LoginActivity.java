package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.libizo.CustomEditText;
import com.pixplicity.easyprefs.library.Prefs;
import com.tapadoo.alerter.Alerter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.MCC;
import shadowws.in.mycinemachance.response.DirectorLoginResponse;
import shadowws.in.mycinemachance.response.MemberLoginResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;
import thebat.lib.validutil.ValidUtils;

public class LoginActivity extends AppCompatActivity implements Connection.Receiver {

    CustomEditText etMobile, etPass;
    Button btnLogin;
    TextView tvRegister, tvSkip, tvSwitch;
    ValidUtils validUtils;
    ProgressLoadingJIGB progress;
    Switch switchBtn;
    String mobile, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        validUtils = new ValidUtils();

        etMobile = findViewById(R.id.log_et_mobile);
        etPass = findViewById(R.id.log_et_pass);
        tvRegister = findViewById(R.id.log_tv_register);
        tvSkip = findViewById(R.id.log_tv_skip);
        btnLogin = findViewById(R.id.log_btn_login);
        switchBtn = findViewById(R.id.switch_btn);
        tvSwitch = findViewById(R.id.switch_tv);

        String writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        int writeResult = this.checkCallingOrSelfPermission(writePermission);
        int readResult = this.checkCallingOrSelfPermission(readPermission);

        if (writeResult == PackageManager.PERMISSION_DENIED &&
                readResult == PackageManager.PERMISSION_DENIED){
            requestPermission();
        }

        mobile = Prefs.getString("rmobile", null);
        password = Prefs.getString("rpassword", null);

        if (!checkNullOrEmpty(mobile) && !checkNullOrEmpty(password)){

            etMobile.setText(mobile);

        }else {

            etMobile.setText("");
        }

        tvSwitch.setText("MEMBER");

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    tvSwitch.setText("DIRECTOR");
                }else {
                    tvSwitch.setText("MEMBER");
                }

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               login();
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                skip();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                startActivity(intent, options.toBundle());

            }
        });

    }

    private void loginConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(LoginActivity.this)
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

            if (validUtils.validateEditTexts(etMobile, etPass)){

                String mobile = etMobile.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                String category = tvSwitch.getText().toString().trim();

                if (category.equalsIgnoreCase("member")){

                    progress.startLoadingJIGB(LoginActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

                    RetrofitAPI api = RetrofitBASE.getRetrofitInstance(LoginActivity.this).create(RetrofitAPI.class);
                    Call<MemberLoginResponse> call = api.memberLogin(mobile, pass);

                    call.enqueue(new Callback<MemberLoginResponse>() {
                        @Override
                        public void onResponse(Call<MemberLoginResponse> call, Response<MemberLoginResponse> response) {

                            try{

                                if (response.isSuccessful()){

                                    MemberLoginResponse data = response.body();

                                    if (data != null){

                                        boolean error = data.getError();
                                        String message = data.getMessage();

                                        if (error == false){

                                            progress.finishLoadingJIGB(LoginActivity.this);

                                            MemberLoginResponse.User results = data.getUser();

                                            int id = results.getId();
                                            String fname = results.getFname();
                                            String lname = results.getLname();
                                            String email = results.getEmail();
                                            String mobile = results.getMobile();
                                            String category = results.getCategory();
                                            String type = results.getType();

                                            /*Prefs.putBoolean("memberLoggedIn", true);
                                            Prefs.putInt("lid", id);
                                            Prefs.putString("lfname", fname);
                                            Prefs.putString("llname", lname);
                                            Prefs.putString("lemail", email);
                                            Prefs.putString("lmobile", mobile);
                                            Prefs.putString("lcategory", category);
                                            Prefs.putString("ltype", type);*/

                                            Intent intent = new Intent(LoginActivity.this, MemberHomeActivity.class);
                                            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                                            startActivity(intent, options.toBundle());


                                        }else {

                                            progress.finishLoadingJIGB(LoginActivity.this);
                                            Alerter.create(LoginActivity.this)
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

                                        progress.finishLoadingJIGB(LoginActivity.this);
                                        Alerter.create(LoginActivity.this)
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

                                progress.finishLoadingJIGB(LoginActivity.this);
                                Alerter.create(LoginActivity.this)
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
                        public void onFailure(Call<MemberLoginResponse> call, Throwable t) {

                            call.cancel();
                            progress.finishLoadingJIGB(LoginActivity.this);
                            Alerter.create(LoginActivity.this)
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

                    progress.startLoadingJIGB(LoginActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

                    RetrofitAPI api = RetrofitBASE.getRetrofitInstance(LoginActivity.this).create(RetrofitAPI.class);
                    Call<DirectorLoginResponse> call = api.directorLogin(mobile, pass);

                    call.enqueue(new Callback<DirectorLoginResponse>() {
                        @Override
                        public void onResponse(Call<DirectorLoginResponse> call, Response<DirectorLoginResponse> response) {

                            try {

                                if (response.isSuccessful()){

                                    DirectorLoginResponse data = response.body();

                                    if (data != null){

                                        boolean error = data.getError();
                                        String message = data.getMessage();

                                        if (error == false){

                                            progress.finishLoadingJIGB(LoginActivity.this);

                                            DirectorLoginResponse.User results = data.getUser();

                                            int id = results.getId();
                                            String name = results.getName();
                                            String email = results.getEmail();
                                            String mobile = results.getMobile();
                                            String category = results.getCategory();
                                            String language = results.getLanguage();

                                             /*Prefs.putBoolean("directorLoggedIn", true);
                                            Prefs.putInt("lid", id);
                                            Prefs.putString("lfname", fname);
                                            Prefs.putString("llname", lname);
                                            Prefs.putString("lemail", email);
                                            Prefs.putString("lmobile", mobile);
                                            Prefs.putString("lcategory", category);
                                            Prefs.putString("ltype", type);*/

                                            Intent intent = new Intent(LoginActivity.this, DirectorHomeActivity.class);
                                            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                                            startActivity(intent, options.toBundle());

                                        }else {

                                            progress.finishLoadingJIGB(LoginActivity.this);
                                            Alerter.create(LoginActivity.this)
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

                                        progress.finishLoadingJIGB(LoginActivity.this);
                                        Alerter.create(LoginActivity.this)
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

                                progress.finishLoadingJIGB(LoginActivity.this);
                                Alerter.create(LoginActivity.this)
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
                        public void onFailure(Call<DirectorLoginResponse> call, Throwable t) {

                            call.cancel();
                            progress.finishLoadingJIGB(LoginActivity.this);
                            Alerter.create(LoginActivity.this)
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
                }

            }else {

                Alerter.create(LoginActivity.this)
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

    private void skipConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(LoginActivity.this)
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

            Intent intent = new Intent(LoginActivity.this, GuestHomeActivity.class);
            ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
            startActivity(intent, options.toBundle());
        }
    }

    private void requestPermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()){

                            Alerter.create(LoginActivity.this)
                                    .setTitle("Permission Granted :")
                                    .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                    .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                    .setText("All Permissions are Granted")
                                    .setTextAppearance(R.style.AlertTextAppearance_Text)
                                    .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                    .setIcon(R.drawable.ic_info)
                                    .setIconColorFilter(0)
                                    .setBackgroundColorRes(R.color.colorWarning)
                                    .show();

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {

                        Alerter.create(LoginActivity.this)
                                .setTitle("Permission Error :")
                                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                .setText(error.toString())
                                .setTextAppearance(R.style.AlertTextAppearance_Text)
                                .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                .setIcon(R.drawable.ic_info)
                                .setIconColorFilter(0)
                                .setBackgroundColorRes(R.color.colorWarning)
                                .show();


                    }
                })
                .onSameThread()
                .check();
    }


    private void login() {
        boolean isConnected = Connection.isConnected();
        loginConnected(isConnected);
    }

    private void skip() {
        boolean isConnected = Connection.isConnected();
        skipConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        loginConnected(isConnected);
        skipConnected(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MCC.getInstance().setConnectivityListener(this);
    }

    public static boolean checkNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
