package shadowws.in.mycinemachance.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.libizo.CustomEditText;
import com.pixplicity.easyprefs.library.Prefs;
import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.FilePath;
import shadowws.in.mycinemachance.response.MemberProfileResponse;
import shadowws.in.mycinemachance.response.MemberRegisterResponse;
import shadowws.in.mycinemachance.response.UploadResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;
import thebat.lib.validutil.ValidUtils;

public class MemberProfileActivity extends AppCompatActivity implements Connection.Receiver {

    TextView tvDob, tvPic, tvCv;
    CustomEditText etName, etMobile, etEmail, etPass, etCPass, etFb, etQualify, etAddress, etState, etActor, etAudio, etVideo, etAchieve, etYourself;
    Button btnUpdate;
    ImageButton ibtnDate, ibtnPic, ibtnCv;
    ImageView ivPicUploded, ivCvUploaded;
    Spinner spLanguage, spProfession, spGender, spIndustry;
    ValidUtils validUtils;
    ProgressLoadingJIGB progress;
    Calendar calendar;
    String picPath, cvPath, filePath;
    String strLanguage, strProfession, strGender, strIndustry;
    public static final int PIC_REQUEST = 1;
    public static final int CV_REQUEST = 2;
    String aPath, vPath, bPath, pPath, fbData, actorData, achieveData, yourselfData;
    ArrayList<String> languageList, professionList, genderList, industryList;
    String picture, audio, video, resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("PROFILE");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        validUtils = new ValidUtils();

        etName = findViewById(R.id.mem_et_name);
        etMobile = findViewById(R.id.mem_et_mobile);
        etEmail = findViewById(R.id.mem_et_email);
        etFb = findViewById(R.id.mem_et_fb);
        tvDob = findViewById(R.id.mem_tv_dob);
        etQualify = findViewById(R.id.mem_et_qualify);
        etAddress = findViewById(R.id.mem_et_address);
        etState = findViewById(R.id.mem_et_state);
        etActor = findViewById(R.id.mem_et_actor);
        tvPic = findViewById(R.id.mem_tv_pic);
        tvCv = findViewById(R.id.mem_tv_cv);
        etAchieve = findViewById(R.id.mem_et_achieve);
        etYourself = findViewById(R.id.mem_et_yourself);
        btnUpdate = findViewById(R.id.mem_update_btn);
        spLanguage = findViewById(R.id.mem_spin_language);
        spProfession = findViewById(R.id.mem_spin_profession);
        spGender = findViewById(R.id.mem_spin_gender);
        spIndustry = findViewById(R.id.mem_spin_industry);
        ibtnDate = findViewById(R.id.mem_dob);
        ibtnPic = findViewById(R.id.mem_upload_pic);
        ibtnCv = findViewById(R.id.mem_upload_cv);
        ivPicUploded = findViewById(R.id.mem_uploaded_pic);
        ivCvUploaded = findViewById(R.id.mem_uploaded_cv);
        etAudio = findViewById(R.id.mem_et_audio);
        etVideo = findViewById(R.id.mem_et_video);

        profile();

        languageList = new ArrayList<>();
        languageList.add("Tamil");
        languageList.add("English");
        languageList.add("Malayalam");
        languageList.add("Telugu");
        languageList.add("Kannada");
        languageList.add("Hindi");
        languageList.add("Other");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(),
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

        professionList = new ArrayList<>();
        professionList.add("Acting");
        professionList.add("Direction");
        professionList.add("Cinematography");
        professionList.add("Editing");
        professionList.add("Stunt");
        professionList.add("Makeup");
        professionList.add("Costume");
        professionList.add("Art");
        professionList.add("Music");
        professionList.add("Sound");
        professionList.add("Singer");
        professionList.add("Lyricist");
        professionList.add("Dance");
        professionList.add("Public Relation");
        professionList.add("DI VFX");
        professionList.add("Designer");
        professionList.add("Visual Effects");
        professionList.add("Animation");
        professionList.add("Stills");
        professionList.add("Dubbing & Mimicry");
        professionList.add("Short Film");
        professionList.add("Dubsmash");

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, professionList);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProfession.setAdapter(adapter3);

        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProfession = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Transgender");

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, genderList);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter4);

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strGender = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        industryList = new ArrayList<>();
        industryList.add("Tamil Cinema");
        industryList.add("Hindi Cinema");
        industryList.add("Malayalam Cinema");
        industryList.add("Telugu Cinema");
        industryList.add("Kannada Cinema");
        industryList.add("English Cinema");

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, industryList);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIndustry.setAdapter(adapter5);

        spIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strIndustry = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar ccalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String cDate = sdf.format(ccalendar.getTime());
        tvDob.setText(cDate);

        calendar =  Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDateFormat();
            }

        };

        ibtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(MemberProfileActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ibtnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                picture();
            }
        });

        ibtnCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resume();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                update();
            }
        });
    }


    private void profileConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberProfileActivity.this)
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

            String mobile = Prefs.getString("lmobile", null);

            progress.startLoadingJIGB(MemberProfileActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberProfileActivity.this).create(RetrofitAPI.class);
            Call<MemberProfileResponse> call = api.memberProfile(mobile);

            call.enqueue(new Callback<MemberProfileResponse>() {
                @Override
                public void onResponse(Call<MemberProfileResponse> call, Response<MemberProfileResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            MemberProfileResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();
                                String message = data.getMessage();

                                if (error == false){

                                    progress.finishLoadingJIGB(MemberProfileActivity.this);

                                    MemberProfileResponse.User results = data.getUser();

                                    String id = results.getId();
                                    String fname = results.getFname();
                                    String lname = results.getLname();
                                    String mobile = results.getMobile();
                                    String email = results.getEmail();
                                    String language = results.getLanguage();
                                    String profession = results.getProfession();
                                    String fb = results.getFb();
                                    String dob = results.getDob();
                                    String gender = results.getGender();
                                    String qualification = results.getQualification();
                                    String address = results.getAddress();
                                    String city = results.getState();
                                    String actor = results.getActor();
                                    String industry = results.getIndustry();
                                    String profile = results.getProfile();
                                    String achievement = results.getAchievement();
                                    String yourself = results.getYourself();
                                    picture = results.getPicture();
                                    resume = results.getResume();
                                    audio = results.getAudio();
                                    video = results.getVideo();

                                    etName.setText(fname+"\t"+lname);
                                    etFb.setText(fb);
                                    etEmail.setText(email);
                                    etMobile.setText(mobile);
                                    tvDob.setText(dob);
                                    tvPic.setText(profile);
                                    tvCv.setText(resume);
                                    etAudio.setText(audio);
                                    etVideo.setText(video);
                                    etAddress.setText(address);
                                    etState.setText(city);
                                    etQualify.setText(qualification);
                                    etActor.setText(actor);
                                    etYourself.setText(yourself);
                                    etAchieve.setText(achievement);

                                    ArrayAdapter genderAdapter = (ArrayAdapter) spGender.getAdapter();
                                    int spinnerPosition1 = genderAdapter.getPosition(gender);
                                    spGender.setSelection(spinnerPosition1);

                                    ArrayAdapter langaugeAdapter = (ArrayAdapter) spLanguage.getAdapter();
                                    int spinnerPosition2 = langaugeAdapter.getPosition(language);
                                    spLanguage.setSelection(spinnerPosition2);

                                    ArrayAdapter professionAdapter = (ArrayAdapter) spProfession.getAdapter();
                                    int spinnerPosition3 = professionAdapter.getPosition(profession);
                                    spProfession.setSelection(spinnerPosition3);

                                    ArrayAdapter industryAdapter = (ArrayAdapter) spIndustry.getAdapter();
                                    int spinnerPosition4 = industryAdapter.getPosition(profession);
                                    spIndustry.setSelection(spinnerPosition4);


                                }else {

                                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                                    Alerter.create(MemberProfileActivity.this)
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

                                progress.finishLoadingJIGB(MemberProfileActivity.this);
                                Alerter.create(MemberProfileActivity.this)
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

                        progress.finishLoadingJIGB(MemberProfileActivity.this);
                        Alerter.create(MemberProfileActivity.this)
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
                public void onFailure(Call<MemberProfileResponse> call, Throwable t) {

                    call.cancel();
                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                    Alerter.create(MemberProfileActivity.this)
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
    }


    private void updateConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberProfileActivity.this)
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

            if (validUtils.validateEditTexts(etName, etMobile, etEmail, etPass, etCPass,
                    etQualify, etAddress, etState)){

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String pass = etPass.getText().toString().trim();
                String cpass = etCPass.getText().toString().trim();
                String dob = tvDob.getText().toString().trim();
                String qualify = etQualify.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String state = etState.getText().toString().trim();

                String picture = tvPic.getText().toString().trim();
                if (picture.length() > 0){
                    pPath = tvPic.getText().toString().trim();
                }else {
                    pPath = "null";
                }
                String resume = tvCv.getText().toString().trim();
                if (resume.length() > 0){
                    bPath = tvCv.getText().toString().trim();
                }else {
                    bPath = "null";
                }
                String audio = etAudio.getText().toString().trim();
                if (audio.length() > 0){
                    aPath = etAudio.getText().toString().trim();
                }else {
                    aPath = "null";
                }
                String video = etVideo.getText().toString().trim();
                if (video.length() > 0){
                    vPath = etVideo.getText().toString().trim();
                }else {
                    vPath = "null";
                }
                String fb = etFb.getText().toString().trim();
                if (fb.length() > 0){
                    fbData = etFb.getText().toString().trim();
                }else {
                    fbData = "null";
                }
                String actor = etActor.getText().toString().trim();
                if (actor.length() > 0){
                    actorData = etActor.getText().toString().trim();
                }else {
                    actorData = "null";
                }
                String achieve = etAchieve.getText().toString().trim();
                if (achieve.length() > 0){
                    achieveData = etAchieve.getText().toString().trim();
                }else {
                    achieveData = "null";
                }
                String yourself = etYourself.getText().toString().trim();
                if (yourself.length() > 0){
                    yourselfData = etYourself.getText().toString().trim();
                }else {
                    yourselfData = "null";
                }


                String language = spLanguage.getSelectedItem().toString().trim();
                String profession = spProfession.getSelectedItem().toString().trim();
                String gender = spGender.getSelectedItem().toString().trim();
                String industry = spIndustry.getSelectedItem().toString().trim();

                progress.startLoadingJIGB(MemberProfileActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

                /*RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberProfileActivity.this).create(RetrofitAPI.class);
                Call<MemberRegisterResponse> call = api.memberRegister(name, mobile, email, language, profession, fbData, dob, gender,
                        qualify, address, state, actorData, industry, pPath, bPath, aPath, vPath, achieveData, yourselfData);

                call.enqueue(new Callback<MemberRegisterResponse>() {
                    @Override
                    public void onResponse(Call<MemberRegisterResponse> call, Response<MemberRegisterResponse> response) {

                        try {

                            if (response.isSuccessful()){

                                MemberRegisterResponse data = response.body();

                                if (data != null){

                                    boolean error = data.getError();
                                    String message = data.getMessage();

                                    if (error == false){

                                        progress.finishLoadingJIGB(MemberProfileActivity.this);

                                        Alerter.create(MemberProfileActivity.this)
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

                                        Prefs.putString("rmobile", mobile);
                                        Prefs.putString("rpassword", cpass);

                                        new Handler().postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                // This method will be executed once the timer is over

                                            }
                                        }, 1500);

                                    }else {

                                        progress.finishLoadingJIGB(MemberProfileActivity.this);
                                        Alerter.create(MemberProfileActivity.this)
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

                                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                                    Alerter.create(MemberProfileActivity.this)
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

                            progress.finishLoadingJIGB(MemberProfileActivity.this);
                            Alerter.create(MemberProfileActivity.this)
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
                    public void onFailure(Call<MemberRegisterResponse> call, Throwable t) {

                        call.cancel();
                        progress.finishLoadingJIGB(MemberProfileActivity.this);
                        Alerter.create(MemberProfileActivity.this)
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
                });*/

            }else {

                Alerter.create(MemberProfileActivity.this)
                        .setTitle("Validation Error :")
                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                        .setText("All Feilds are Empty")
                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                        .setIcon(R.drawable.ic_info)
                        .setIconColorFilter(0)
                        .setBackgroundColorRes(R.color.colorWarning)
                        .show();
            }

        }
    }

    private void resumeConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberProfileActivity.this)
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

            Intent intent = new Intent();
            intent.setType("*/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, ""), CV_REQUEST);
        }
    }

    private void pictureConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberProfileActivity.this)
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

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, ""), PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {

            if (requestCode == PIC_REQUEST && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                filePath = FilePath.getPath(this, selectedImage);
                picPath = filePath.substring(filePath.lastIndexOf("/")+1);
                tvPic.setText(picPath);
                tvPic.setSelected(true);

                uploadPicture(filePath);

            }else if (requestCode == CV_REQUEST && resultCode == RESULT_OK && null != data){

                Uri selectedDocument = data.getData();
                filePath = FilePath.getPath(this, selectedDocument);
                cvPath = filePath.substring(filePath.lastIndexOf("/")+1);
                tvCv.setText(cvPath);
                tvCv.setSelected(true);

                uploadResume(filePath);

            }

        }catch (Exception e){

            Alerter.create(MemberProfileActivity.this)
                    .setTitle("Connection Error :")
                    .setTitleAppearance(R.style.AlertTextAppearance_Title)
                    .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                    .setText(e.getMessage())
                    .setTextAppearance(R.style.AlertTextAppearance_Text)
                    .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                    .setIcon(R.drawable.ic_info)
                    .setIconColorFilter(0)
                    .setBackgroundColorRes(R.color.colorError)
                    .show();
        }

    }

    private void uploadResume(String filePath) {

        File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        progress.startLoadingJIGB(MemberProfileActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

        RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberProfileActivity.this).create(RetrofitAPI.class);
        Call<UploadResponse> call = api.uploadFile(fileToUpload, filename);

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {

                try {

                    if (response.isSuccessful()){

                        UploadResponse data = response.body();

                        if (data != null){

                            boolean error = data.getError();
                            String message = data.getMessage();

                            if (error == false){

                                progress.finishLoadingJIGB(MemberProfileActivity.this);
                                Alerter.create(MemberProfileActivity.this)
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

                                ibtnCv.setVisibility(View.GONE);
                                ivCvUploaded.setVisibility(View.VISIBLE);

                            }else {

                                progress.finishLoadingJIGB(MemberProfileActivity.this);
                                Alerter.create(MemberProfileActivity.this)
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

                            progress.finishLoadingJIGB(MemberProfileActivity.this);
                            Alerter.create(MemberProfileActivity.this)
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

                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                    Alerter.create(MemberProfileActivity.this)
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
            public void onFailure(Call<UploadResponse> call, Throwable t) {

                if (t.getMessage().equalsIgnoreCase("connect timed out")){

                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                    call.cancel();
                    uploadResume(filePath);

                }else {

                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                    call.cancel();
                    Alerter.create(MemberProfileActivity.this)
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

    private void uploadPicture(String filePath) {

        File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        progress.startLoadingJIGB(MemberProfileActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

        RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberProfileActivity.this).create(RetrofitAPI.class);
        Call<UploadResponse> call = api.uploadFile(fileToUpload, filename);

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {

                try {

                    if (response.isSuccessful()){

                        UploadResponse data = response.body();

                        if (data != null){

                            boolean error = data.getError();
                            String message = data.getMessage();

                            if (error == false){

                                progress.finishLoadingJIGB(MemberProfileActivity.this);
                                Alerter.create(MemberProfileActivity.this)
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

                                ibtnPic.setVisibility(View.GONE);
                                ivPicUploded.setVisibility(View.VISIBLE);

                            }else {

                                progress.finishLoadingJIGB(MemberProfileActivity.this);
                                Alerter.create(MemberProfileActivity.this)
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

                            progress.finishLoadingJIGB(MemberProfileActivity.this);
                            Alerter.create(MemberProfileActivity.this)
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

                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                    Alerter.create(MemberProfileActivity.this)
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
            public void onFailure(Call<UploadResponse> call, Throwable t) {

                if (t.getMessage().equalsIgnoreCase("connect timed out")){

                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                    call.cancel();
                    uploadPicture(filePath);

                }else {

                    progress.finishLoadingJIGB(MemberProfileActivity.this);
                    call.cancel();
                    Alerter.create(MemberProfileActivity.this)
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

    private void setDateFormat() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDob.setText(sdf.format(calendar.getTime()));
    }

    private void profile() {
        boolean isConnected = Connection.isConnected();
        profileConnected(isConnected);
    }

    private void resume() {
        boolean isConnected = Connection.isConnected();
        resumeConnected(isConnected);
    }



    private void picture() {
        boolean isConnected = Connection.isConnected();
        pictureConnected(isConnected);
    }




    private void update() {
        boolean isConnected = Connection.isConnected();
        updateConnected(isConnected);
    }



    @Override
    public void onConnectionChanged(boolean isConnected) {
        updateConnected(isConnected);
        resumeConnected(isConnected);
        pictureConnected(isConnected);
        profileConnected(isConnected);
    }
}
