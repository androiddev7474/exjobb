package expenditurediary.tool.se.expenditurediary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import test1.sql.se.sqltest.SQLhelper;

public class EditCategoryArrayAdapter extends ArrayAdapter <String> {

    private Context context;
    private String[] labels;
    private LayoutInflater inflater;

    private int clickedPosition;

    public EditCategoryArrayAdapter(Context context, String[] labels) {
        super(context, R.layout.activity_edit_category, labels);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        this.labels = labels;
        this.context = context;
    }


    static class ViewHolder {

        RadioButton chooseCategory;
        TextView cat_label;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.edit_cat_list_content, parent, false);
            holder.chooseCategory = convertView.findViewById(R.id.radio_change_cat_id);
            holder.cat_label = convertView.findViewById(R.id.cat_label_id);
            convertView.setTag(holder);

            initRadioButtonListeners(holder, position);

        } else
            holder = (ViewHolder) convertView.getTag();

        if (position != clickedPosition) {
            holder.chooseCategory.setChecked(false);
        }

        holder.cat_label.setText(labels[position]);

        return convertView;
    }


    private void initRadioButtonListeners(final ViewHolder holder, final int position) {

        holder.chooseCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(context, "EditCatAct:" + position, Toast.LENGTH_SHORT).show();

                    clickedPosition = position;

                    notifyDataSetChanged();

                } else {


                }
            }

        });
    }


    public int getClickedPosition() {
        return clickedPosition;
    }


    public void setLabels(String[] labels) {

        this.labels = labels;
    }
}
