package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forms.sti.progresslitieigb.ProgressLoadingJIGB;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.DirectorPictureAdapter;
import shadowws.in.mycinemachance.other.Connection;

public class DirectorPictureActivity extends AppCompatActivity implements Connection.Receiver {

    RecyclerView rvPicture;
    RecyclerView.LayoutManager layoutManager;
    DirectorPictureAdapter directorPictureAdapter;
    String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_picture);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("PICTURES");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        final Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvPicture = findViewById(R.id.rv_pic);

        layoutManager = new GridLayoutManager(this, 2);
        rvPicture.setHasFixedSize(true);
        rvPicture.setNestedScrollingEnabled(false);
        rvPicture.setLayoutManager(layoutManager);

        picture();
    }

    private void pictureConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(DirectorPictureActivity.this)
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
            picture = bundle.getString("mpicture");

            if (picture.contains(",")){

                String[] elements = picture.split(",");
                List<String> fixedLenghtList = Arrays.asList(elements);
                ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

                directorPictureAdapter = new DirectorPictureAdapter(DirectorPictureActivity.this, listOfString);
                rvPicture.setAdapter(directorPictureAdapter);

            }else {

                ArrayList<String> listOfString = new ArrayList<String>();
                listOfString.add(picture);

                directorPictureAdapter = new DirectorPictureAdapter(DirectorPictureActivity.this, listOfString);
                rvPicture.setAdapter(directorPictureAdapter);

            }
        }
    }


    private void picture() {
        boolean isConnected = Connection.isConnected();
        pictureConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        pictureConnected(isConnected);
    }
}
