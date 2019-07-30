package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.tapadoo.alerter.Alerter;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.adapter.MemberWantedAdapter;
import shadowws.in.mycinemachance.other.Connection;

public class MemberWantedActivity extends AppCompatActivity implements Connection.Receiver {

    String id, name, mobile, email, title, desc, language, category, createdOn;
    TextView tvName, tvEmail, tvDesc, tvTitle, tvLanguage, tvCategory, tvCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_wanted);

        TextView title = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        title.setLayoutParams(layoutParams);
        title.setText("WANTED");
        title.setTextSize(18);
        title.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface font = Typeface.createFromAsset(getAssets(), "sans_bold.ttf");
        title.setTypeface(font);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setCustomView(title);

        tvName = findViewById(R.id.mem_wan_name);
        tvEmail = findViewById(R.id.mem_wan_email);
        tvDesc = findViewById(R.id.mem_wan_desc);
        tvTitle = findViewById(R.id.mem_wan_title);
        tvLanguage = findViewById(R.id.mem_wan_lang);
        tvCategory = findViewById(R.id.mem_wan_cat);
        tvCreated = findViewById(R.id.mem_wan_created);

        wanted();
    }

    private void wantedConnected(boolean isConnected) {

        if (!isConnected){

            Alerter.create(MemberWantedActivity.this)
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

                id = bundle.getString("wdid");
                name = bundle.getString("wname");
                mobile = bundle.getString("wmobile");
                email = bundle.getString("wemail");
                title = bundle.getString("wtitle");
                desc = bundle.getString("wdesc");
                language = bundle.getString("wlanguage");
                category = bundle.getString("wcategory");
                createdOn = bundle.getString("wcreated");

                Prefs.putString("wdid", id);
                Prefs.putString("wname", name);
                Prefs.putString("wmobile", mobile);
                Prefs.putString("wemail", email);
                Prefs.putString("wtitle", title);
                Prefs.putString("wdesc", desc);
                Prefs.putString("wlanguage", language);
                Prefs.putString("wcategory", category);
                Prefs.putString("wcreated", createdOn);

            }else {

                id = Prefs.getString("wdid", null);
                name = Prefs.getString("wname", null);
                mobile = Prefs.getString("wmobile", null);
                email = Prefs.getString("wemail", null);
                title =Prefs.getString("wtitle", null);
                desc = Prefs.getString("wdesc", null);
                language = Prefs.getString("wlanguage", null);
                category = Prefs.getString("wcategory", null);
                createdOn = Prefs.getString("wcreated", null);


            }

            tvName.setText(name);
            tvEmail.setText(email);
            tvTitle.setText(title);
            tvDesc.setText(desc);
            tvLanguage.setText(language);
            tvCategory.setText(category);
            tvCreated.setText(createdOn);

        }
    }

    private void wanted() {
        boolean isConnected = Connection.isConnected();
        wantedConnected(isConnected);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        wantedConnected(isConnected);
    }
}
