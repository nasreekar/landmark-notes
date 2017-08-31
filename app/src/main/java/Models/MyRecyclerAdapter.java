package Models;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abhijith.home.landmark_notes.R;

import java.util.List;

/**
 * Created by home on 31/8/17.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private Activity context;
    private List<Notes> notesList;

    public MyRecyclerAdapter(Activity context, List<Notes> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_item,parent,false);

        return new ViewHolder(v);
    }

    //Bind the data to the view object
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Notes n = notesList.get(position);
        holder.tvListTitle.setText(n.getTitle());
        holder.tvListDescription.setText(n.getDescription());

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvListTitle;
        public TextView tvListDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            tvListTitle = (TextView)itemView.findViewById(R.id.tvListTitle);
            tvListDescription = (TextView)itemView.findViewById(R.id.tvListDescription);
        }
    }
}
