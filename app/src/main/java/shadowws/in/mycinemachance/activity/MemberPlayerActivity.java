package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tapadoo.alerter.Alerter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.other.Connection;

public class MemberPlayerActivity extends YouTubeBaseActivity implements Connection.Receiver {

    public static String YOUTUBE_API_KEY = "AIzaSyDHqIJqS8GB8wwtqA6EVQqVOItineBy2zY";
    YouTubePlayerView youTubePlayerView;
    static String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_player);

        youTubePlayerView = findViewById(R.id.youtubePlayer);

        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("mlink");

        player();
    }

    private void playerConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberPlayerActivity.this)
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
            link = bundle.getString("mlink");

            if (!checkNullOrEmpty(link)){

                youTubePlayerView.initialize(YOUTUBE_API_KEY,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                                youTubePlayer.cueVideo(getYoutubeVideoId(link));
                                youTubePlayer.setFullscreen(true);
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {

                                String errorMessage = error.toString();
                                Alerter.create(MemberPlayerActivity.this)
                                        .setTitle("Player Error :")
                                        .setTitleAppearance(R.style.AlertTextAppearance_Title)
                                        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "sans_bold.ttf"))
                                        .setText(errorMessage)
                                        .setTextAppearance(R.style.AlertTextAppearance_Text)
                                        .setTextTypeface(Typeface.createFromAsset(getAssets(), "sans_regular.ttf"))
                                        .setIcon(R.drawable.no_internet)
                                        .setIconColorFilter(0)
                                        .setBackgroundColorRes(R.color.colorError)
                                        .show();
                            }
                        });
            }
        }
    }

    public static String getYoutubeVideoId(String youtubeUrl) {

        String video_id = link;
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("https"))
        {

            String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches())
            {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public static boolean checkNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }

    private void player() {
        boolean isConnected = Connection.isConnected();
        playerConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        playerConnected(isConnected);
    }
}
