package expenditurediary.tool.se.expenditurediary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpenditureDataAdapter extends ArrayAdapter <String> {

    private Context context;
    private LayoutInflater inflater;
    private String[] labels;
    private String[] iconRes;
    private float[] sums;

    public ExpenditureDataAdapter(Context context, String[] labels, String[] iconRes) {
        super(context, R.layout.summary_fragment, labels);

        this.context = context;
        this.labels = labels;
        this.iconRes = iconRes;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {

        return labels.length;
    }

    static class ViewHolder {

        TextView category;
        TextView expenditure;
        ImageView icon;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null) {

            convertView = inflater.inflate(R.layout.expend_data_listview_content, parent, false);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.categoryIcon);
            holder.category = convertView.findViewById(R.id.categoryLabel);
            holder.expenditure = convertView.findViewById(R.id.expenditure);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder)convertView.getTag();
        }

        //holder.icon.setImageResource(iconRes[position]);
        holder.icon.setImageResource(MainActivity.getImageId(context, iconRes[position]));

        holder.category.setText(labels[position]);

        if (sums != null)
            holder.expenditure.setText("" + (int)sums[position]);

        return convertView;
    }



    public void setSums(float[] sums) {

        this.sums = sums;
    }

    public void setLabels(String[] labels) {

        this.labels = labels;
    }

    public void setIconRes(String[] iconRes) {

        this.iconRes = iconRes;
    }
}
