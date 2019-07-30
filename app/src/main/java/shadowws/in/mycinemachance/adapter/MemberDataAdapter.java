package shadowws.in.mycinemachance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.activity.MemberInterviewActivity;
import shadowws.in.mycinemachance.activity.MemberLookActivity;
import shadowws.in.mycinemachance.activity.MemberRatingActivity;
import shadowws.in.mycinemachance.activity.MemberTrailerActivity;
import shadowws.in.mycinemachance.response.MemberDataResponse;

public class MemberDataAdapter extends RecyclerView.Adapter<MemberDataAdapter.MyViewHolder> {

    Context context;
    List<MemberDataResponse.Data> dataList;

    public MemberDataAdapter(Context context, List<MemberDataResponse.Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_data_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvData.setText(dataList.get(position).getTitle());
        Glide.with(context.getApplicationContext())
                .load(dataList.get(position).getImage())
                .error(R.drawable.preview_image)
                .centerCrop()
                .into(holder.ivData);

        holder.cvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 0){
                    Intent intent = new Intent(context, MemberTrailerActivity.class);
                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                    context.startActivity(intent, options.toBundle());
                }else if (position == 1){
                    Intent intent = new Intent(context, MemberRatingActivity.class);
                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                    context.startActivity(intent, options.toBundle());
                }else if (position == 2){
                    Intent intent = new Intent(context, MemberLookActivity.class);
                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                    context.startActivity(intent, options.toBundle());
                }else if (position == 3){
                    Intent intent = new Intent(context, MemberInterviewActivity.class);
                    ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                    context.startActivity(intent, options.toBundle());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivData;
        TextView tvData;
        CardView cvData;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivData = itemView.findViewById(R.id.mem_data_iv);
            tvData = itemView.findViewById(R.id.mem_data_tv);
            cvData = itemView.findViewById(R.id.cv_data);
        }
    }
}
