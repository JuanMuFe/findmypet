package cat.proven.findmypet.findmypet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.AnnouncementClass;
import model.OwnerClass;

/**
 * Created by Alumne on 03/05/2016.
 */
public class CustomOwnerList extends ArrayAdapter<OwnerClass> {

    private final Activity context;
    private final ArrayList<OwnerClass> owners;

    public CustomOwnerList(Activity context, ArrayList<OwnerClass> owners) {
        super(context, R.layout.owner_list, owners);
        this.context = context;
        this.owners = owners;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.announcement_list, null, true);
        TextView desc = (TextView) rowView.findViewById(R.id.desc);
        TextView date = (TextView) rowView.findViewById(R.id.date);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        desc.setText(owners.get(position).getName());


        imageView.setImageResource(R.drawable.user);
        return rowView;
    }
}
