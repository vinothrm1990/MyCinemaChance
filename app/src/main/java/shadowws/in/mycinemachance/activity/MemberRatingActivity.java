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
import shadowws.in.mycinemachance.adapter.MemberRatingAdapter;
import shadowws.in.mycinemachance.adapter.MemberTrailerAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.response.MemberRatingResponse;
import shadowws.in.mycinemachance.response.MemberTrailerResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class MemberRatingActivity extends AppCompatActivity implements Connection.Receiver {

    RecyclerView rvRating;
    RecyclerView.LayoutManager layoutManager;
    ProgressLoadingJIGB progress;
    MemberRatingAdapter memberRatingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_rating);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("MOVIE RATINGS");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvRating = findViewById(R.id.rv_rating);
        layoutManager = new LinearLayoutManager(this);
        rvRating.setHasFixedSize(true);
        rvRating.setNestedScrollingEnabled(false);
        rvRating.setLayoutManager(layoutManager);

        rating();
    }

    private void ratingConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberRatingActivity.this)
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

            progress.startLoadingJIGB(MemberRatingActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberRatingActivity.this).create(RetrofitAPI.class);
            Call<MemberRatingResponse> call = api.memberRating();

            call.enqueue(new Callback<MemberRatingResponse>() {
                @Override
                public void onResponse(Call<MemberRatingResponse> call, Response<MemberRatingResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            MemberRatingResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();
                                String message = data.getMessage();

                                if (error == false){

                                    progress.finishLoadingJIGB(MemberRatingActivity.this);

                                    List<MemberRatingResponse.Data> results = data.getData();
                                    memberRatingAdapter = new MemberRatingAdapter(MemberRatingActivity.this, results);
                                    rvRating.setAdapter(memberRatingAdapter);

                                }else {

                                    progress.finishLoadingJIGB(MemberRatingActivity.this);
                                    Alerter.create(MemberRatingActivity.this)
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

                                progress.finishLoadingJIGB(MemberRatingActivity.this);
                                Alerter.create(MemberRatingActivity.this)
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

                        progress.finishLoadingJIGB(MemberRatingActivity.this);
                        Alerter.create(MemberRatingActivity.this)
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
                public void onFailure(Call<MemberRatingResponse> call, Throwable t) {

                    if (t.getMessage().equalsIgnoreCase("connect timed out")){

                        progress.finishLoadingJIGB(MemberRatingActivity.this);
                        call.cancel();
                        rating();

                    }else {

                        call.cancel();
                        progress.finishLoadingJIGB(MemberRatingActivity.this);
                        Alerter.create(MemberRatingActivity.this)
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

    private void rating() {
        boolean isConnected = Connection.isConnected();
        ratingConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        ratingConnected(isConnected);
    }
}
