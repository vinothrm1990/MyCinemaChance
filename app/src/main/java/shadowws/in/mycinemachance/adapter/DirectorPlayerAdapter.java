package shadowws.in.mycinemachance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.activity.DirectorPicturePreviewActivity;
import shadowws.in.mycinemachance.activity.DirectorPlayerPreviewActivity;

public class DirectorPlayerAdapter extends RecyclerView.Adapter<DirectorPlayerAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> playerList;

    public DirectorPlayerAdapter(Context context, ArrayList<String> playerList) {
        this.context = context;
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.director_player_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String resume = playerList.get(position);
        String title = String.valueOf(resume.lastIndexOf("/")+1);

        if (resume.startsWith("http://mycinemachance.com")){
            holder.tvPlayer.setText(title);
        }else {
            holder.tvPlayer.setText(resume);
        }

        holder.cvPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DirectorPlayerPreviewActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                Bundle bundle = new Bundle();
                bundle.putString("mlink", playerList.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvPlayer;
        TextView tvPlayer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvPlayer = itemView.findViewById(R.id.cv_player);
            tvPlayer = itemView.findViewById(R.id.dir_player_tv);
        }
    }
}
