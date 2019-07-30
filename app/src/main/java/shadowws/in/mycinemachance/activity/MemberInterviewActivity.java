package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.tapadoo.alerter.Alerter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.MemberInterviewAdapter;
import shadowws.in.mycinemachance.adapter.MemberTrailerAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.response.MemberInterviewResponse;
import shadowws.in.mycinemachance.response.MemberTrailerResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class MemberInterviewActivity extends AppCompatActivity implements Connection.Receiver {

    RecyclerView rvInterview;
    MemberInterviewAdapter memberInterviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressLoadingJIGB progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_interview);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("MCC INTERVIEW");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvInterview = findViewById(R.id.rv_interview);
        layoutManager = new LinearLayoutManager(this);
        rvInterview.setHasFixedSize(true);
        rvInterview.setNestedScrollingEnabled(false);
        rvInterview.setLayoutManager(layoutManager);

        interview();
    }

    private void interviewConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberInterviewActivity.this)
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

            progress.startLoadingJIGB(MemberInterviewActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberInterviewActivity.this).create(RetrofitAPI.class);
            Call<MemberInterviewResponse> call = api.memberInterview();

            call.enqueue(new Callback<MemberInterviewResponse>() {
                @Override
                public void onResponse(Call<MemberInterviewResponse> call, Response<MemberInterviewResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            MemberInterviewResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();
                                String message = data.getMessage();

                                if (error == false){

                                    progress.finishLoadingJIGB(MemberInterviewActivity.this);

                                    List<MemberInterviewResponse.Data> results = data.getData();
                                    memberInterviewAdapter = new MemberInterviewAdapter(MemberInterviewActivity.this, results);
                                    rvInterview.setAdapter(memberInterviewAdapter);

                                }else {

                                    progress.finishLoadingJIGB(MemberInterviewActivity.this);
                                    Alerter.create(MemberInterviewActivity.this)
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

                                progress.finishLoadingJIGB(MemberInterviewActivity.this);
                                Alerter.create(MemberInterviewActivity.this)
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

                        progress.finishLoadingJIGB(MemberInterviewActivity.this);
                        Alerter.create(MemberInterviewActivity.this)
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
                public void onFailure(Call<MemberInterviewResponse> call, Throwable t) {

                    if (t.getMessage().equalsIgnoreCase("connect timed out")){

                        progress.finishLoadingJIGB(MemberInterviewActivity.this);
                        call.cancel();
                        interview();

                    }else {

                        call.cancel();
                        progress.finishLoadingJIGB(MemberInterviewActivity.this);
                        Alerter.create(MemberInterviewActivity.this)
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

    private void interview() {
        boolean isConnected = Connection.isConnected();
        interviewConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        interviewConnected(isConnected);
    }

}
