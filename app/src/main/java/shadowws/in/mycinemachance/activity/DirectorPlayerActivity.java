package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.DirectorPictureAdapter;
import shadowws.in.mycinemachance.adapter.DirectorPlayerAdapter;
import shadowws.in.mycinemachance.other.Connection;

public class DirectorPlayerActivity extends AppCompatActivity implements Connection.Receiver {

    RecyclerView rvPlayer;
    RecyclerView.LayoutManager layoutManager;
    DirectorPlayerAdapter directorPlayerAdapter;
    String player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_player);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("PLAYLISTS");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        final Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvPlayer = findViewById(R.id.rv_player);

        layoutManager = new GridLayoutManager(this, 2);
        rvPlayer.setHasFixedSize(true);
        rvPlayer.setNestedScrollingEnabled(false);
        rvPlayer.setLayoutManager(layoutManager);

        player();
    }

    private void playerConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(DirectorPlayerActivity.this)
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
            player = bundle.getString("mlink");

            if (player.contains(",")){

                String[] elements = player.split(",");
                List<String> fixedLenghtList = Arrays.asList(elements);
                ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

                directorPlayerAdapter = new DirectorPlayerAdapter(DirectorPlayerActivity.this, listOfString);
                rvPlayer.setAdapter(directorPlayerAdapter);

            }else {

                ArrayList<String> listOfString = new ArrayList<String>();
                listOfString.add(player);

                directorPlayerAdapter = new DirectorPlayerAdapter(DirectorPlayerActivity.this, listOfString);
                rvPlayer.setAdapter(directorPlayerAdapter);
            }

        }
    }


    private void player() {
        boolean isConnected = Connection.isConnected();
        playerConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        playerConnected(isConnected);
    }

    public static boolean checkNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
