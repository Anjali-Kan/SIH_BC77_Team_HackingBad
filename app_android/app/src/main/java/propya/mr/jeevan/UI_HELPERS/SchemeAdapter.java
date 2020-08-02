package propya.mr.jeevan.UI_HELPERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import propya.mr.jeevan.BluePrint.Scheme;
import propya.mr.jeevan.R;

public class SchemeAdapter extends ArrayAdapter<Scheme> {
    public SchemeAdapter(Context context, ArrayList<Scheme> schemes) {
        super(context, 0, schemes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.scheme_item_list, parent, false);
        }

        // Get the data item for this position
        Scheme scheme = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.scheme_item_list, parent, false);
        }
        // Lookup view for data population
        TextView schemeName = (TextView) convertView.findViewById(R.id.schemeName);
        TextView schemeStatus = (TextView) convertView.findViewById(R.id.schemeStatus);
        // Populate the data into the template view using the data object
        schemeName.setText(scheme.getSchemeName());
        schemeStatus.setText(scheme.getmSchemeStatus());
        // Return the completed view to render on screen
        return convertView;
    }
}
