package shadowws.in.mycinemachance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.response.DirectorDataResponse;

public class DirectorDataAdapter extends RecyclerView.Adapter<DirectorDataAdapter.MyViewHolder> {

    Context context;
    List<DirectorDataResponse.User> userList;
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public DirectorDataAdapter(Context context) {
        this.context = context;
        userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.director_data_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText(userList.get(position).getProfession());
        holder.tvName.setText(userList.get(position).getFname());
        //holder.tvName.setText(userList.get(position).getFname() +"\t"+userList.get(position).getLname());

        if (userList.get(position).getProfile().startsWith("http://mycinemachance.com/")){
            Glide.with(context)
                    .load(userList.get(position).getProfile())
                    .thumbnail(0.1f)
                    .into(holder.ivPicture);
        }else {
            Glide.with(context)
                    .load("http://mycinemachance.com/upload/"+userList.get(position).getProfile())
                    .thumbnail(0.1f)
                    .into(holder.ivPicture);
        }


    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == userList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvName;
        CircleImageView ivPicture;
        CardView cvData;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.director_tv_title);
            tvName = itemView.findViewById(R.id.director_tv_name);
            ivPicture = itemView.findViewById(R.id.director_iv);
            cvData = itemView.findViewById(R.id.cv_director);
        }
    }

    public void add(DirectorDataResponse.User user) {
        userList.add(user);
        notifyItemInserted(userList.size() - 1);
    }

    public void addAll(List<DirectorDataResponse.User> userList) {
        for (DirectorDataResponse.User list : userList) {
            add(list);
        }
    }

    public List<DirectorDataResponse.User> getData() {
        return userList;
    }
}
