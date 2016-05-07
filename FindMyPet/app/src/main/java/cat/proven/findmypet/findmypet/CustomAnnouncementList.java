package cat.proven.findmypet.findmypet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cat.proven.findmypet.findmypet.R;
import model.AnnouncementClass;

/**
 * Created by Alumne on 03/05/2016.
 */
public class CustomAnnouncementList extends ArrayAdapter<AnnouncementClass> {

    private final Activity context;
    private final ArrayList<AnnouncementClass> announcements;

    public CustomAnnouncementList(Activity context, ArrayList<AnnouncementClass> announcements) {
        super(context, R.layout.announcement_list, announcements);
        this.context = context;
        this.announcements = announcements;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.announcement_list, null, true);
        TextView desc = (TextView) rowView.findViewById(R.id.desc);
        TextView date = (TextView) rowView.findViewById(R.id.date);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        desc.setText(announcements.get(position).getDescription());
        date.setText(announcements.get(position).getDate());

        imageView.setImageResource(R.drawable.user);
        return rowView;
    }
}
