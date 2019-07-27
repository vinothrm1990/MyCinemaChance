package shadowws.in.mycinemachance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.activity.DirectorAboutActivity;
import shadowws.in.mycinemachance.activity.DirectorMemberActivity;
import shadowws.in.mycinemachance.response.DirectorRequestedResponse;

public class DirectorRequestedAdapter extends RecyclerView.Adapter<DirectorRequestedAdapter.MyViewHolder> {

    Context context;
    List<DirectorRequestedResponse.User> userList;
    private boolean isLoadingAdded = false;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    public DirectorRequestedAdapter(Context context) {
        this.context = context;
        userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.director_requested_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(userList.get(position).getMfname()+"\t"+userList.get(position).getMlname());
        holder.tvCategory.setText(userList.get(position).getMcategory());

        if (userList.get(position).getMprofile().startsWith("http://mycinemachance.com/")){
            Glide.with(context)
                    .load(userList.get(position).getMprofile())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.preview_image)
                    .into(holder.ivProfile);
        }else {
            Glide.with(context)
                    .load("http://mycinemachance.com/upload/"+userList.get(position).getMprofile())
                    .thumbnail(0.1f)
                    .into(holder.ivProfile);
        }

        holder.cvRequested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DirectorMemberActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                Bundle bundle = new Bundle();
                bundle.putString("mmobile", userList.get(position).getMmobile());
                intent.putExtras(bundle);
                context.startActivity(intent, options.toBundle());

                //Toast.makeText(context, userList.get(position).getMid(), Toast.LENGTH_SHORT).show();
            }
        });


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
        TextView tvName, tvCategory;
        CircleImageView ivProfile;
        CardView cvRequested;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.req_name_tv);
            tvCategory = itemView.findViewById(R.id.req_cat_tv);
            ivProfile = itemView.findViewById(R.id.req_iv);
            cvRequested = itemView.findViewById(R.id.cv_requested);
        }
    }

    public void add(DirectorRequestedResponse.User user) {
        userList.add(user);
        notifyItemInserted(userList.size() - 1);
    }

    public void addAll(List<DirectorRequestedResponse.User> userList) {
        for (DirectorRequestedResponse.User list : userList) {
            add(list);
        }
    }

    public List<DirectorRequestedResponse.User> getData() {
        return userList;
    }
}
