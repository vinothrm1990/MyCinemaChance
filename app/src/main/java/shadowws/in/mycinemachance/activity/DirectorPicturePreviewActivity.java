package shadowws.in.mycinemachance.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.other.Connection;

public class DirectorPicturePreviewActivity extends AppCompatActivity {

    ImageView imageView;
    String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_picture_preview);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        picture = bundle.getString("mpicture");

        imageView = findViewById(R.id.dir_preview_iv);
        imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        imageView.setAdjustViewBounds(false);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide
                .with(DirectorPicturePreviewActivity.this)
                .load(picture)
                .placeholder(R.drawable.preview_image)
                .into(imageView);


    }

}
