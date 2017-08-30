package Models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abhijith.home.landmark_notes.R;

import java.util.List;

/**
 * Created by home on 30/8/17.
 */

public class NotesList extends ArrayAdapter<Notes> {

    private Activity context;
    private List<Notes> notesList;

    public NotesList(Activity context,List<Notes> notesList){
        super(context, R.layout.list_layout,notesList);
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView tvListTitle = (TextView)listViewItem.findViewById(R.id.tvListTitle);
        TextView tvListDescription = (TextView)listViewItem.findViewById(R.id.tvListDescription);

        Notes notez = notesList.get(position);
        Log.d("notez in NOTELIST: ", notez.toString());
        tvListTitle.setText(notez.getTitle());
        tvListDescription.setText(notez.getDescription());

        return listViewItem;
    }
}
