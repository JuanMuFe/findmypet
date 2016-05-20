package cat.proven.findmypet.findmypet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import model.ReportClass;

/**
 * Created by Alumne on 14/05/2016.
 */
public class CustomReportList extends ArrayAdapter<ReportClass> {
    private final Activity context;
    private final ArrayList<ReportClass> reports;
    private final ArrayList<String> locations;

    public CustomReportList(Activity context, ArrayList<ReportClass> reports, ArrayList<String> locations) {
        super(context, R.layout.report_list, reports);
        this.context = context;
        this.reports = reports;
        this.locations = locations;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.report_list, null, true);

        TextView pet = (TextView) rowView.findViewById(R.id.pet);
        TextView location = (TextView) rowView.findViewById(R.id.location);
        TextView extra = (TextView) rowView.findViewById(R.id.extra);
        TextView date = (TextView) rowView.findViewById(R.id.date);

        pet.setText(reports.get(position).getIdPet());
        location.setText(locations.get(position));
        extra.setText(reports.get(position).getExtra());
        date.setText(reports.get(position).getEntryDate());

        return rowView;
    }
}
