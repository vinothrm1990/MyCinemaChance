package shadowws.in.mycinemachance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.activity.DirectorPlayerPreviewActivity;
import shadowws.in.mycinemachance.activity.MemberPlayerActivity;
import shadowws.in.mycinemachance.response.MemberTrailerResponse;

public class MemberTrailerAdapter extends RecyclerView.Adapter<MemberTrailerAdapter.MyViewHolder>  {

    Context context;
    List<MemberTrailerResponse.Data> trailerList;
    String vId,img_url;

    public MemberTrailerAdapter(Context context, List<MemberTrailerResponse.Data> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_trailer_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvLang.setText("Language : "+trailerList.get(position).getLanguage());

        if (trailerList.get(position).getUrl().startsWith("https://www.youtube.com")){

            try {

                vId = extractYoutubeId(trailerList.get(position).getUrl());
                img_url = "http://img.youtube.com/vi/"+vId+"/0.jpg";

                Glide.with(context)
                        .load(img_url)
                        .placeholder(R.drawable.preview_image)
                        .into(holder.ivThumbnail);

            }catch (Exception e){
                e.printStackTrace();
            }

        }else {

            Glide.with(context)
                    .load(R.drawable.preview_image)
                    .placeholder(R.drawable.preview_image)
                    .into(holder.ivThumbnail);
        }

        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MemberPlayerActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                Bundle bundle = new Bundle();
                bundle.putString("mlink", trailerList.get(position).getUrl());
                intent.putExtras(bundle);
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLang;
        ImageView ivPlay, ivThumbnail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlay = itemView.findViewById(R.id.mem_trailer_play);
            ivThumbnail = itemView.findViewById(R.id.mem_trailer_thumbnail);
            tvLang = itemView.findViewById(R.id.mem_trailer_tv);
        }
    }

    private String extractYoutubeId(String url) {

        String query = null;
        try {
            query = new URL(url).getQuery();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }
}
