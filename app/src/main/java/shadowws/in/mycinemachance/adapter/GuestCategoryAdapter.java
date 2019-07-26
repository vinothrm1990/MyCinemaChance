package shadowws.in.mycinemachance.adapter;

import android.content.Context;
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
import shadowws.in.mycinemachance.response.GuestCategoryResponse;

public class GuestCategoryAdapter extends RecyclerView.Adapter<GuestCategoryAdapter.MyViewHolder> {

    Context context;
    List<GuestCategoryResponse.Data> dataList;

    public GuestCategoryAdapter(Context context, List<GuestCategoryResponse.Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.guest_category_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvData.setText(dataList.get(position).getTitle());
        Glide.with(context.getApplicationContext())
                .load(dataList.get(position).getImage())
                .error(R.drawable.preview_image)
                .into(holder.ivData);

        holder.cvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Login or Register to Proceed.", Toast.LENGTH_SHORT).show();
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
            ivData = itemView.findViewById(R.id.guest_cat_iv);
            tvData = itemView.findViewById(R.id.guest_cat_tv);
            cvData = itemView.findViewById(R.id.cv_cat);
        }
    }
}
