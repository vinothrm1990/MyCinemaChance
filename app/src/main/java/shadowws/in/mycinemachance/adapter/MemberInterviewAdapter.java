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
import shadowws.in.mycinemachance.activity.MemberPlayerActivity;
import shadowws.in.mycinemachance.response.MemberInterviewResponse;

public class MemberInterviewAdapter extends RecyclerView.Adapter<MemberInterviewAdapter.MyViewHolder> {

    Context mContext;
    List<MemberInterviewResponse.Data> interList;
    String vId,img_url;

    public MemberInterviewAdapter(Context mContext, List<MemberInterviewResponse.Data> interList) {
        this.mContext = mContext;
        this.interList = interList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_interview_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText(interList.get(position).getTitle());

        if (interList.get(position).getVideo().startsWith("https://www.youtube.com")){

            try {

                vId = extractYoutubeId(interList.get(position).getVideo());
                img_url = "http://img.youtube.com/vi/"+vId+"/0.jpg";

                Glide.with(mContext)
                        .load(img_url)
                        .placeholder(R.drawable.preview_image)
                        .into(holder.ivThumbnail);

            }catch (Exception e){
                e.printStackTrace();
            }


        }else {

            Glide.with(mContext)
                    .load(R.drawable.preview_image)
                    .placeholder(R.drawable.preview_image)
                    .into(holder.ivThumbnail);

        }

        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, MemberPlayerActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext);
                Bundle bundle = new Bundle();
                bundle.putString("mlink", interList.get(position).getVideo());
                intent.putExtras(bundle);
                mContext.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return interList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlay, ivThumbnail;
        TextView tvTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.mem_interview_title);
            ivThumbnail = itemView.findViewById(R.id.mem_interview_thumbnail);
            ivPlay = itemView.findViewById(R.id.mem_interview_play);
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
