package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.DirectorDocumentAdapter;
import shadowws.in.mycinemachance.adapter.DirectorPictureAdapter;
import shadowws.in.mycinemachance.other.Connection;

public class DirectorDocumentActivity extends AppCompatActivity implements Connection.Receiver {

    String resume;
    RecyclerView rvDocument;
    RecyclerView.LayoutManager layoutManager;
    DirectorDocumentAdapter directorDocumentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_document);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("DOCUMENTS");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        rvDocument = findViewById(R.id.rv_doc);

        layoutManager = new GridLayoutManager(this, 2);
        rvDocument.setHasFixedSize(true);
        rvDocument.setNestedScrollingEnabled(false);
        rvDocument.setLayoutManager(layoutManager);

        document();
    }

    private void documentConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(DirectorDocumentActivity.this)
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
            if (bundle != null){
                resume = bundle.getString("mresume");
                Prefs.putString("dmresume", resume);
            }else {
                resume = Prefs.getString("dmresume", null);

            }
            if (resume.contains(",")){

                String[] elements = resume.split(",");
                List<String> fixedLenghtList = Arrays.asList(elements);
                ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

                directorDocumentAdapter = new DirectorDocumentAdapter(DirectorDocumentActivity.this, listOfString);
                rvDocument.setAdapter(directorDocumentAdapter);

            }else {

                ArrayList<String> listOfString = new ArrayList<String>();
                listOfString.add(resume);

                directorDocumentAdapter = new DirectorDocumentAdapter(DirectorDocumentActivity.this, listOfString);
                rvDocument.setAdapter(directorDocumentAdapter);
            }
        }
    }

    private void document() {
        boolean isConnected = Connection.isConnected();
        documentConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        documentConnected(isConnected);
    }
}
