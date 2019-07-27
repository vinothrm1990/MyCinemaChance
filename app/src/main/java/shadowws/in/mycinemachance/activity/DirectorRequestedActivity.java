package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import shadowws.in.mycinemachance.adapter.DirectorRequestedAdapter;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.PaginationScrollListener;
import shadowws.in.mycinemachance.response.DirectorRequestedResponse;
import shadowws.in.mycinemachance.retrofit.RetrofitAPI;
import shadowws.in.mycinemachance.retrofit.RetrofitBASE;

public class DirectorRequestedActivity extends AppCompatActivity implements Connection.Receiver {

    ProgressLoadingJIGB progress;
    SwipeRefreshLayout refreshLayout;
    RecyclerView rvRequested;
    LinearLayoutManager layoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private static int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    RetrofitAPI api;
    DirectorRequestedAdapter directorRequestedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_requested);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("REQUESTED MEMBERS");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvRequested = findViewById(R.id.rv_member);
        refreshLayout = findViewById(R.id.swipe_member);

        directorRequestedAdapter = new DirectorRequestedAdapter(DirectorRequestedActivity.this);
        layoutManager = new LinearLayoutManager(this);
        rvRequested.setLayoutManager(layoutManager);
        rvRequested.setAdapter(directorRequestedAdapter);

        request();

        rvRequested.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                if (TOTAL_PAGES >= 10){
                    isLoading = true;
                    currentPage++;
                    loadNextPage();
                }
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
    }

    private void requestConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(DirectorRequestedActivity.this)
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
            loadFirstPage();
            refreshLayout.setOnRefreshListener(this::onRefresh);

        }
    }

    private void loadFirstPage() {

        progress.startLoadingJIGB(DirectorRequestedActivity.this,
                R.raw.progress, "Please Wait...",
                0,500,300);

        currentPage = PAGE_START;

        callDirectorData().enqueue(new Callback<DirectorRequestedResponse>() {
            @Override
            public void onResponse(Call<DirectorRequestedResponse> call, Response<DirectorRequestedResponse> response) {

                try {

                    if (response.isSuccessful()) {

                        DirectorRequestedResponse data = response.body();

                        if (data != null){

                            boolean error = data.getError();

                            if (error == false){

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorRequestedActivity.this);

                                List<DirectorRequestedResponse.User> results = fetchResults(response);

                                String totalPages = results.get(0).getTotalPages();
                                TOTAL_PAGES = Integer.parseInt(totalPages);
                                directorRequestedAdapter.addAll(results);

                                if (currentPage >= TOTAL_PAGES){
                                    isLastPage = true;
                                }

                            }else {

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                                Alerter.create(DirectorRequestedActivity.this)
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
                            progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                            Alerter.create(DirectorRequestedActivity.this)
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
                    progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                    Alerter.create(DirectorRequestedActivity.this)
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
            public void onFailure(Call<DirectorRequestedResponse> call, Throwable t) {

                if (t.getMessage().equalsIgnoreCase("connect timed out")){

                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                    call.cancel();
                    loadFirstPage();

                }else {

                    call.cancel();
                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                    Alerter.create(DirectorRequestedActivity.this)
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

        progress.startLoadingJIGB(DirectorRequestedActivity.this,
                R.raw.progress, "Please Wait...",
                0,500,300);

        callDirectorData().enqueue(new Callback<DirectorRequestedResponse>() {
            @Override
            public void onResponse(Call<DirectorRequestedResponse> call, Response<DirectorRequestedResponse> response) {

                try {

                    if (response.isSuccessful()) {

                        DirectorRequestedResponse data = response.body();

                        if (data != null){

                            boolean error = data.getError();

                            if (error == false){

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorRequestedActivity.this);

                                List<DirectorRequestedResponse.User> results = fetchResults(response);
                                isLoading = false;
                                String totalPages = results.get(0).getTotalPages();
                                TOTAL_PAGES = Integer.parseInt(totalPages);
                                directorRequestedAdapter.addAll(results);

                                if (currentPage == TOTAL_PAGES){
                                    isLastPage = true;
                                }

                            }else {

                                refreshLayout.setRefreshing(false);
                                progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                                Alerter.create(DirectorRequestedActivity.this)
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
                            progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                            Alerter.create(DirectorRequestedActivity.this)
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
                    progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                    Alerter.create(DirectorRequestedActivity.this)
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
            public void onFailure(Call<DirectorRequestedResponse> call, Throwable t) {

                if (t.getMessage().equalsIgnoreCase("connect timed out")){

                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                    call.cancel();
                    loadNextPage();

                }else {

                    call.cancel();
                    refreshLayout.setRefreshing(false);
                    progress.finishLoadingJIGB(DirectorRequestedActivity.this);
                    Alerter.create(DirectorRequestedActivity.this)
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

    private Call<DirectorRequestedResponse> callDirectorData(){
        return api.allRequestedMember(
                currentPage
        );
    }

    private List<DirectorRequestedResponse.User> fetchResults(Response<DirectorRequestedResponse> response) {
        DirectorRequestedResponse dataResponse = response.body();
        return dataResponse.getUsers();
    }


    private void onRefresh() {
        progress.finishLoadingJIGB(DirectorRequestedActivity.this);
        refreshLayout.setRefreshing(false);
        currentPage = PAGE_START;
        isLastPage = false;
        directorRequestedAdapter.getData().clear();
        directorRequestedAdapter.notifyDataSetChanged();
        request();
    }


    private void request() {
        boolean isConnected = Connection.isConnected();
        requestConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        requestConnected(isConnected);
    }
}
