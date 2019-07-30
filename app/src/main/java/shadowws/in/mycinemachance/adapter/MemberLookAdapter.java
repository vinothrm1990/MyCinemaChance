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

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.response.MemberLookResponse;

public class MemberLookAdapter extends RecyclerView.Adapter<MemberLookAdapter.MyViewHolder> {

    Context mContext;
    List<MemberLookResponse.Data> lookList;

    public MemberLookAdapter(Context mContext, List<MemberLookResponse.Data> lookList) {
        this.mContext = mContext;
        this.lookList = lookList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_look_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText(lookList.get(position).getTitle() + "|" + lookList.get(position).getLanguage());
        if (lookList.get(position).getImage().startsWith("http://mycinemachance.com")){
            Glide.with(mContext)
                    .load(lookList.get(position).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.preview_image)
                    .into(holder.ivImage);
        }else {
            Glide.with(mContext)
                    .load("http://mycinemachance.com/upload/"+lookList.get(position).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.preview_image)
                    .into(holder.ivImage);
        }

    }

    @Override
    public int getItemCount() {
        return lookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.mem_look_title);
            ivImage = itemView.findViewById(R.id.mem_look_image);
        }
    }
}
