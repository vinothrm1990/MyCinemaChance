package shadowws.in.mycinemachance.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shadowws.in.mycinemachance.R;

public class DirectorDocumentAdapter extends RecyclerView.Adapter<DirectorDocumentAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> docList;

    public DirectorDocumentAdapter(Context context, ArrayList<String> docList) {
        this.context = context;
        this.docList = docList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.director_document_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String resume = docList.get(position);
        String title = String.valueOf(resume.lastIndexOf("/")+1);

        if (resume.startsWith("http://mycinemachance.com")){
            holder.tvDoc.setText(title);
        }else {
            holder.tvDoc.setText(resume);
        }

        holder.cvDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = resume;
                String extension = uri.substring(uri.lastIndexOf("."));

                if (extension.equalsIgnoreCase("pdf")) {
                    if (resume.startsWith("http://mycinemachance.com")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resume));
                        context.startActivity(browserIntent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mycinemachance.com/upload/" + resume));
                        context.startActivity(browserIntent);

                    }
                }else {
                    if (resume.startsWith("http://mycinemachance.com")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/gview?embedded=true&url"+resume));
                        context.startActivity(browserIntent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/gview?embedded=true&url=http://mycinemachance.com/upload/" + resume));
                        context.startActivity(browserIntent);

                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cvDoc;
        TextView tvDoc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDoc = itemView.findViewById(R.id.cv_doc);
            tvDoc = itemView.findViewById(R.id.dir_doc_tv);
        }
    }
}
