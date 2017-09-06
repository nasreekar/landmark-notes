package Models;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhijith.home.landmark_notes.MapsActivity;
import com.abhijith.home.landmark_notes.R;
import com.abhijith.home.landmark_notes.NotesListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 31/8/17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements Filterable {

    private Activity context;
    private List<Notes> notesList;
    private List<Notes> mFilteredList;

    private Menu context_menu;
    ActionMode mActionMode;
    boolean isMultiSelect = false;
    ArrayList<Notes> multiselect_list = new ArrayList<>();


    public NotesAdapter(Activity context, List<Notes> notesList) {
        this.context = context;
        this.notesList = notesList;
        this.mFilteredList = notesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);

        return new ViewHolder(v);
    }

    //Bind the data to the view object
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Notes n = mFilteredList.get(position);
        holder.tvListTitle.setText(n.getTitle());
        holder.tvListDescription.setText(n.getDescription());

        holder.notesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMultiSelect) {
                    multi_select(position,v);
                }
                else {
                    //Toast.makeText(context,"You clicked : "+ n.getTitle(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("tag", "single_note");
                    intent.putExtra("serialize_object_data", n);
                    context.startActivity(intent);
                }
            }
        });

        holder.notesLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
              //  v.setSelected(true);
//                int p=position;
//                Log.i("LongClick: ",String.valueOf(p));
//                Toast.makeText(context,"You selected: "+ mFilteredList.get(position).getTitle(), Toast.LENGTH_SHORT ).show();
//                return true;// returning true instead of false, works for me
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Notes>();

                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = ((NotesListActivity) context).startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position,v);
                return true;
            }
        });

    }

    public void multi_select(int position,View view) {
        if (mActionMode != null) {
            if (multiselect_list.contains(notesList.get(position))) {
                view.setSelected(false);
                multiselect_list.remove(notesList.get(position));
            }
            else {
                view.setSelected(true);
                multiselect_list.add(notesList.get(position));
            }

            //text on action bar
            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.finish();

            notifyDataSetChanged();

        }
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = notesList;
                } else {

                    ArrayList<Notes> filteredList = new ArrayList<>();

                    for (Notes androidVersion : notesList) {

                        if (androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getDescription().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Notes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvListTitle;
        public TextView tvListDescription;
        public RelativeLayout notesLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            tvListTitle = (TextView) itemView.findViewById(R.id.tvListTitle);
            tvListDescription = (TextView) itemView.findViewById(R.id.tvListDescription);

            notesLayout = (RelativeLayout) itemView.findViewById(R.id.rlNotes);
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_action_mode, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertCallToDelete();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Notes>();
            notifyDataSetChanged();
        }
    };

    public void alertCallToDelete() {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Delete Confirmation");
        alertDialog.setMessage("Do you want to delete the selected items");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (multiselect_list.size() > 0) {
                            for (int i = 0; i < multiselect_list.size(); i++) {
                                notesList.remove(multiselect_list.get(i));
                                ((NotesListActivity)context).getDatabase().child(multiselect_list.get(i).getId()).removeValue();
                            }
                            notifyDataSetChanged();

                            if (mActionMode != null) {
                                mActionMode.finish();
                            }
                            Toast.makeText(context, "Item(s) Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        alertDialog.show();
    }
}
