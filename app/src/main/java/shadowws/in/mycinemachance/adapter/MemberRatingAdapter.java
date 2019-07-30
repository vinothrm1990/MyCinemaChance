package shadowws.in.mycinemachance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hyogeun.github.com.colorratingbarlib.ColorRatingBar;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.response.MemberRatingResponse;

public class MemberRatingAdapter extends RecyclerView.Adapter<MemberRatingAdapter.MyViewHolder> {

    Context mContext;
    List<MemberRatingResponse.Data> rateList;

    public MemberRatingAdapter(Context mContext, List<MemberRatingResponse.Data> rateList) {
        this.mContext = mContext;
        this.rateList = rateList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_rating_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText(rateList.get(position).getTitle());
        holder.tvDesc.setText(rateList.get(position).getDesc());
        holder.ratingBar.setRating(Float.parseFloat(rateList.get(position).getStar()));
        if (rateList.get(position).getImage().startsWith("http://mycinemachance.com")){
            Glide.with(mContext)
                    .load(rateList.get(position).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.preview_image)
                    .into(holder.ivImage);
        }else {
            Glide.with(mContext)
                    .load("http://mycinemachance.com/upload/" + rateList.get(position).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.preview_image)
                    .into(holder.ivImage);
        }

    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ColorRatingBar ratingBar;
        TextView tvTitle, tvDesc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.mem_rate_iv);
            tvTitle = itemView.findViewById(R.id.mem_rate_title);
            tvDesc = itemView.findViewById(R.id.mem_rate_desc);
            ratingBar = itemView.findViewById(R.id.mem_rate_bar);
        }
    }
}
