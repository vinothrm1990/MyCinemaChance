package shadowws.in.mycinemachance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.acra.annotation.AcraMailSender;

import java.util.ArrayList;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.activity.DirectorMemberActivity;
import shadowws.in.mycinemachance.activity.DirectorPictureActivity;
import shadowws.in.mycinemachance.activity.DirectorPicturePreviewActivity;

public class DirectorPictureAdapter extends RecyclerView.Adapter<DirectorPictureAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> picList;

    public DirectorPictureAdapter(Context context, ArrayList<String> picList) {
        this.context = context;
        this.picList = picList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.director_picture_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context)
                .load("http://mycinemachance.com/upload/"+picList.get(position))
                .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
                })
                .placeholder(R.drawable.preview_image)
                .into(holder.ivPic);

        holder.cvPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DirectorPicturePreviewActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                Bundle bundle = new Bundle();
                bundle.putString("mpicture", "http://mycinemachance.com/upload/"+picList.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvPic;
        ImageView ivPic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvPic = itemView.findViewById(R.id.cv_pic);
            ivPic = itemView.findViewById(R.id.dir_pic_view);
        }
    }
}
