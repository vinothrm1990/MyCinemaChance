package shadowws.in.mycinemachance.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;

import com.github.demono.adapter.InfinitePagerAdapter;

import java.util.List;

import shadowws.in.mycinemachance.R;
import shadowws.in.mycinemachance.activity.MemberWantedActivity;
import shadowws.in.mycinemachance.response.MemberWantedResponse;

public class MemberWantedAdapter extends InfinitePagerAdapter {

    Context context;
    List<MemberWantedResponse.Data> wantedList;

    public MemberWantedAdapter(Context context, List<MemberWantedResponse.Data> wantedList) {
        this.context = context;
        this.wantedList = wantedList;
    }

    @Override
    public int getItemCount() {
        return wantedList == null ? 0 : wantedList.size();
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup container) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.member_wanted_adapter,null);

        }

        TextView title = view.findViewById(R.id.mem_wanted_title);
        TextView requirement = view.findViewById(R.id.mem_wanted_requirement);
        ImageView image = view.findViewById(R.id.mem_wanted_iv);
        LinearLayout layout = view.findViewById(R.id.mem_wanted_layout);

        title.setText(wantedList.get(position).getCategory() +"\t:");
        requirement.setText(wantedList.get(position).getTitle() +",\t"+ wantedList.get(position).getLanguage());
        image.setImageDrawable(context.getResources().getDrawable(R.drawable.new_img));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MemberWantedActivity.class);
                ActivityOptionsCompat options  = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context);
                Bundle bundle = new Bundle();
                bundle.putString("wname", wantedList.get(position).getName());
                bundle.putString("wmobile", wantedList.get(position).getMobile());
                bundle.putString("wemail", wantedList.get(position).getEmail());
                bundle.putString("wlanguage", wantedList.get(position).getLanguage());
                bundle.putString("wcategory", wantedList.get(position).getCategory());
                bundle.putString("wtitle", wantedList.get(position).getTitle());
                bundle.putString("wdesc", wantedList.get(position).getDesc());
                bundle.putString("wdid", wantedList.get(position).getDid());
                bundle.putString("wcreated", wantedList.get(position).getCreated());
                intent.putExtras(bundle);
                context.startActivity(intent, options.toBundle());

            }
        });

        return view;
    }
}
