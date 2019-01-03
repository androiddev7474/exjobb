package expenditurediary.tool.se.expenditurediary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IrregularDateSpanAdapter extends ArrayAdapter <String> {

    private  LayoutInflater inflater;
    private String[] dateLabels;
    private int[] iconIDs;
    private Context context;

    public IrregularDateSpanAdapter(Context context, String[] dateLabels) {

        super(context, R.layout.irregulardate_fragment, dateLabels);
        this.context = context;
        this.dateLabels = dateLabels;

        getIconResIDs();

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void getIconResIDs() {

        TypedArray typedThumbsArray = context.getResources().obtainTypedArray(R.array.time_resources);
        iconIDs = new int[typedThumbsArray.length()];
        for (int i = 0; i < iconIDs.length; i++) {
            iconIDs[i] = typedThumbsArray.getResourceId(i, 0);
        }

    }

    private class ViewHolder {

        ImageView icon;
        TextView label;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
           convertView = inflater.inflate(R.layout.irregulardate_listview_content, parent, false);
           holder.icon = convertView.findViewById(R.id.irregdate_icon_id);
           holder.label = convertView.findViewById(R.id.irregdate_label_id);
           convertView.setTag(holder);

        } else {

            holder = (ViewHolder)convertView.getTag();
        }


        holder.icon.setImageResource(iconIDs[position]);
        holder.label.setText(dateLabels[position]);


        return convertView;
    }
}
