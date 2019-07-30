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
import shadowws.in.mycinemachance.adapter.MemberDataAdapter;
import shadowws.in.mycinemachance.adapter.MemberTrailerAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.response.MemberTrailerResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class MemberTrailerActivity extends AppCompatActivity implements Connection.Receiver {

    RecyclerView rvTrailer;
    MemberTrailerAdapter memberTrailerAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressLoadingJIGB progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_trailer);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("TRAILER");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvTrailer = findViewById(R.id.rv_trailer);
        layoutManager = new LinearLayoutManager(this);
        rvTrailer.setHasFixedSize(true);
        rvTrailer.setNestedScrollingEnabled(false);
        rvTrailer.setLayoutManager(layoutManager);

        trailer();
    }

    private void trailerConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberTrailerActivity.this)
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

            progress.startLoadingJIGB(MemberTrailerActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberTrailerActivity.this).create(RetrofitAPI.class);
            Call<MemberTrailerResponse> call = api.memberTrailer();

            call.enqueue(new Callback<MemberTrailerResponse>() {
                @Override
                public void onResponse(Call<MemberTrailerResponse> call, Response<MemberTrailerResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            MemberTrailerResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();
                                String message = data.getMessage();

                                if (error == false){

                                    progress.finishLoadingJIGB(MemberTrailerActivity.this);

                                    List<MemberTrailerResponse.Data> results = data.getData();
                                    memberTrailerAdapter = new MemberTrailerAdapter(MemberTrailerActivity.this, results);
                                    rvTrailer.setAdapter(memberTrailerAdapter);

                                }else {

                                    progress.finishLoadingJIGB(MemberTrailerActivity.this);
                                    Alerter.create(MemberTrailerActivity.this)
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

                                progress.finishLoadingJIGB(MemberTrailerActivity.this);
                                Alerter.create(MemberTrailerActivity.this)
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

                        progress.finishLoadingJIGB(MemberTrailerActivity.this);
                        Alerter.create(MemberTrailerActivity.this)
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
                public void onFailure(Call<MemberTrailerResponse> call, Throwable t) {

                    if (t.getMessage().equalsIgnoreCase("connect timed out")){

                        progress.finishLoadingJIGB(MemberTrailerActivity.this);
                        call.cancel();
                        trailer();

                    }else {

                        call.cancel();
                        progress.finishLoadingJIGB(MemberTrailerActivity.this);
                        Alerter.create(MemberTrailerActivity.this)
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

    private void trailer() {
        boolean isConnected = Connection.isConnected();
        trailerConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        trailerConnected(isConnected);
    }
}
