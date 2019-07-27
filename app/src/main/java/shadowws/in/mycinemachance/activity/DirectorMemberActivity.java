package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.tapadoo.alerter.Alerter;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.response.DirectorLoginResponse;
import shadowws.in.mycinemachance.response.DirectorMemberResponse;
import shadowws.in.mycinemachance.response.MemberLoginResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class DirectorMemberActivity extends AppCompatActivity implements Connection.Receiver {

    String mobile;
    TextView tvFName, tvLName, tvFB, tvEmail, tvMobile, tvDOB, tvGender, tvAddress, tvCity, tvQualification, tvActor, tvLanguage,
                tvYourself, tvAchievements, tvIndustry, tvRegistered;
    LinearLayout llAudio, llVideo, llPicture, llResume;
    ProgressLoadingJIGB progress;
    CircleImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_member);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("MEMBER DETAILS");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        tvFName = findViewById(R.id.mem_fname_tv);
        tvLName = findViewById(R.id.mem_lname_tv);
        tvFB = findViewById(R.id.mem_fb_tv);
        tvEmail = findViewById(R.id.mem_email_tv);
        tvMobile = findViewById(R.id.mem_mobile_tv);
        tvDOB = findViewById(R.id.mem_dob_tv);
        tvGender = findViewById(R.id.mem_gender_tv);
        tvAddress = findViewById(R.id.mem_address_tv);
        tvCity = findViewById(R.id.mem_city_tv);
        tvQualification = findViewById(R.id.mem_qualify_tv);
        tvActor = findViewById(R.id.mem_actor_tv);
        tvLanguage = findViewById(R.id.mem_language_tv);
        tvYourself = findViewById(R.id.mem_about_tv);
        tvAchievements = findViewById(R.id.mem_acheive_tv);
        tvIndustry = findViewById(R.id.mem_industry_tv);
        tvRegistered = findViewById(R.id.mem_registered_tv);
        llAudio = findViewById(R.id.mem_audio_layout);
        llVideo = findViewById(R.id.mem_video_layout);
        llPicture = findViewById(R.id.mem_pic_layout);
        llResume = findViewById(R.id.mem_resume_layout);
        ivProfile = findViewById(R.id.mem_iv);

        llAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DirectorMemberActivity.this, "audio", Toast.LENGTH_SHORT).show();
            }
        });

        llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DirectorMemberActivity.this, "video", Toast.LENGTH_SHORT).show();
            }
        });

        llPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DirectorMemberActivity.this, "pic", Toast.LENGTH_SHORT).show();
            }
        });

        llResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DirectorMemberActivity.this, "cv", Toast.LENGTH_SHORT).show();
            }
        });

        member();

    }

    private void member() {
        boolean isConnected = Connection.isConnected();
        memberConnected(isConnected);
    }

    private void memberConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(DirectorMemberActivity.this)
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

            Bundle bundle = getIntent().getExtras();
            mobile = bundle.getString("mmobile");

            if (!checkNullOrEmpty(mobile)){

                progress.startLoadingJIGB(DirectorMemberActivity.this,
                        R.raw.progress, "Please Wait...",
                        0,500,300);

                RetrofitAPI api = RetrofitBASE.getRetrofitInstance(this).create(RetrofitAPI.class);
                Call<DirectorMemberResponse> call = api.requestedMember(mobile);

                call.enqueue(new Callback<DirectorMemberResponse>() {
                    @Override
                    public void onResponse(Call<DirectorMemberResponse> call, Response<DirectorMemberResponse> response) {

                        try {

                            if (response.isSuccessful()){

                                DirectorMemberResponse data = response.body();

                                if (data != null){

                                    boolean error = data.getError();
                                    String message = data.getMessage();

                                    if (error == false){

                                        progress.finishLoadingJIGB(DirectorMemberActivity.this);

                                        DirectorMemberResponse.User results = data.getUser();

                                        String id = results.getId();
                                        String fname = results.getFname();
                                        String lname = results.getLname();
                                        String mobile = results.getMobile();
                                        String email = results.getEmail();
                                        String language = results.getLanguage();
                                        String category = results.getCategory();
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
                                        String picture = results.getPicture();
                                        String resume = results.getResume();
                                        String audio = results.getAudio();
                                        String video = results.getVideo();
                                        String achievement = results.getAchievement();
                                        String yourself = results.getYourself();
                                        String registered = results.getMemdate();

                                        tvFName.setText(fname);
                                        tvLName.setText(lname);
                                        tvFB.setText(fb);
                                        tvEmail.setText(email);
                                        tvMobile.setText(mobile);
                                        tvDOB.setText(dob);
                                        tvGender.setText(gender);
                                        tvAddress.setText(address);
                                        tvCity.setText(city);
                                        tvQualification.setText(qualification);
                                        tvActor.setText(actor);
                                        tvLanguage.setText(language);
                                        tvYourself.setText(yourself);
                                        tvAchievements.setText(achievement);
                                        tvIndustry.setText(industry);
                                        tvRegistered.setText(registered);

                                        if (profile.startsWith("http://mycinemachance.com/")){
                                            Glide.with(DirectorMemberActivity.this)
                                                    .load(profile)
                                                    .thumbnail(0.1f)
                                                    .placeholder(R.drawable.preview_image)
                                                    .into(ivProfile);
                                        }else {
                                            Glide.with(DirectorMemberActivity.this)
                                                    .load("http://mycinemachance.com/upload/"+profile)
                                                    .thumbnail(0.1f)
                                                    .into(ivProfile);
                                        }

                                    }else {

                                        progress.finishLoadingJIGB(DirectorMemberActivity.this);
                                        Alerter.create(DirectorMemberActivity.this)
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

                                    progress.finishLoadingJIGB(DirectorMemberActivity.this);
                                    Alerter.create(DirectorMemberActivity.this)
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

                            progress.finishLoadingJIGB(DirectorMemberActivity.this);
                            Alerter.create(DirectorMemberActivity.this)
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
                    public void onFailure(Call<DirectorMemberResponse> call, Throwable t) {

                        call.cancel();
                        progress.finishLoadingJIGB(DirectorMemberActivity.this);
                        Alerter.create(DirectorMemberActivity.this)
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
