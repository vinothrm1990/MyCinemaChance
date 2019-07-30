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
import shadowws.in.mycinemachance.adapter.MemberLookAdapter;
import shadowws.in.mycinemachance.adapter.MemberRatingAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.response.MemberLookResponse;
import shadowws.in.mycinemachance.response.MemberRatingResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class MemberLookActivity extends AppCompatActivity implements Connection.Receiver {

    RecyclerView rvLook;
    MemberLookAdapter memberLookAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressLoadingJIGB progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_look);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("FIRST LOOK");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvLook = findViewById(R.id.rv_look);
        layoutManager = new LinearLayoutManager(this);
        rvLook.setHasFixedSize(true);
        rvLook.setNestedScrollingEnabled(false);
        rvLook.setLayoutManager(layoutManager);

        look();
    }

    private void lookConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberLookActivity.this)
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

            progress.startLoadingJIGB(MemberLookActivity.this, R.raw.progress, "Please Wait...", 0,500,300);

            RetrofitAPI api = RetrofitBASE.getRetrofitInstance(MemberLookActivity.this).create(RetrofitAPI.class);
            Call<MemberLookResponse> call = api.memberLook();

            call.enqueue(new Callback<MemberLookResponse>() {
                @Override
                public void onResponse(Call<MemberLookResponse> call, Response<MemberLookResponse> response) {

                    try {

                        if (response.isSuccessful()){

                            MemberLookResponse data = response.body();

                            if (data != null){

                                boolean error = data.getError();
                                String message = data.getMessage();

                                if (error == false){

                                    progress.finishLoadingJIGB(MemberLookActivity.this);

                                    List<MemberLookResponse.Data> results = data.getData();
                                    memberLookAdapter = new MemberLookAdapter(MemberLookActivity.this, results);
                                    rvLook.setAdapter(memberLookAdapter);

                                }else {

                                    progress.finishLoadingJIGB(MemberLookActivity.this);
                                    Alerter.create(MemberLookActivity.this)
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

                                progress.finishLoadingJIGB(MemberLookActivity.this);
                                Alerter.create(MemberLookActivity.this)
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

                        progress.finishLoadingJIGB(MemberLookActivity.this);
                        Alerter.create(MemberLookActivity.this)
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
                public void onFailure(Call<MemberLookResponse> call, Throwable t) {

                    if (t.getMessage().equalsIgnoreCase("connect timed out")){

                        progress.finishLoadingJIGB(MemberLookActivity.this);
                        call.cancel();
                        look();

                    }else {

                        call.cancel();
                        progress.finishLoadingJIGB(MemberLookActivity.this);
                        Alerter.create(MemberLookActivity.this)
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

    private void look() {
        boolean isConnected = Connection.isConnected();
        lookConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        lookConnected(isConnected);
    }
}
