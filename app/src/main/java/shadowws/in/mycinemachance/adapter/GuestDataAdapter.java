package shadowws.in.mycinemachance.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

import shadowws.in.mycinemachance.R;

import shadowws.in.mycinemachance.response.GuestDataResponse;

public class GuestDataAdapter extends RecyclerView.Adapter<GuestDataAdapter.MyViewHolder> {

    Context mContext;
    List<GuestDataResponse.Data> dataList;

    public GuestDataAdapter(Context mContext, List<GuestDataResponse.Data> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_data_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.tvData.setText(dataList.get(position).getTitle());
        Glide.with(mContext.getApplicationContext())
                .load(dataList.get(position).getImage())
                .error(R.drawable.preview_image)
                .centerCrop()
                .into(holder.ivData);

        holder.cvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "Login or Register to Proceed.", Toast.LENGTH_SHORT).show();
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
            ivData = itemView.findViewById(R.id.guest_data_iv);
            tvData = itemView.findViewById(R.id.guest_data_tv);
            cvData = itemView.findViewById(R.id.cv_data);
        }
    }

}
