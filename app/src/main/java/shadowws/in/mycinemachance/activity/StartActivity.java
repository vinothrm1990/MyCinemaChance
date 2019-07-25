package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import com.pixplicity.easyprefs.library.Prefs;
import com.tapadoo.alerter.Alerter;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.other.Connection;
import shadowws.in.mycinemachance.other.MCC;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                if (Prefs.getBoolean("memberLoggedIn", false)) {
                    Intent intent = new Intent(StartActivity.this, MemberHomeActivity.class);
                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(StartActivity.this);
                    startActivity(intent, options.toBundle());
                } else if (Prefs.getBoolean("directorLoggedIn", false)){
                    Intent intent = new Intent(StartActivity.this, DirectorHomeActivity.class);
                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(StartActivity.this);
                    startActivity(intent, options.toBundle());
                }else {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation(StartActivity.this);
                    startActivity(intent, options.toBundle());
                }
            }
        }, 1500);


    }

}
