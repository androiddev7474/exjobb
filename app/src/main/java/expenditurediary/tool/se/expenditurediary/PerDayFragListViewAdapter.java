package expenditurediary.tool.se.expenditurediary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PerDayFragListViewAdapter extends ArrayAdapter <Object> {

    private Context context;
    private LayoutInflater inflater;

    private Object[][] data;

    private TextView header;

    private boolean[] listPosToBeRemoved;

    private PerDayCommentInfoFragment commentFragment = new PerDayCommentInfoFragment();

    public PerDayFragListViewAdapter(Context context, Object[][] data) {
        super(context, R.layout.per_day_content_fragment, data);

        this.context = context;
        this.data = data;

        listPosToBeRemoved = new boolean[data.length];

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {

        return data.length;
    }

    static class ViewHolder {

        CheckBox checkBox;
        TextView rowId;
        TextView expenditure;
        ImageView iconInfo;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.per_day_listview_content, parent, false);
            holder.checkBox = convertView.findViewById(R.id.per_item_checkbox);
            holder.rowId = convertView.findViewById(R.id.sql_row_id);
            holder.expenditure = convertView.findViewById(R.id.per_item_expenditure);
            holder.iconInfo = convertView.findViewById(R.id.info_icon_id);
            convertView.setTag(holder);

            initIconOnClickListener(position, holder);
        } else
            holder = (ViewHolder)convertView.getTag();

        holder.rowId.setText(data[position][0].toString());

        if (data[position][4].equals("")) {
            holder.iconInfo.setVisibility(View.INVISIBLE);

        } else {
            holder.iconInfo.setImageResource(R.drawable.info2);
            holder.iconInfo.setVisibility(View.VISIBLE);
        }


        holder.expenditure.setText(data[position][3].toString());

        initCheckBoxListeners(holder.checkBox, position);

        return convertView;
    }


    private void initIconOnClickListener(final int position, ViewHolder holder) {

        holder.iconInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String comment = data[position][4].toString();

                Bundle b = new Bundle();
                b.putString(PerDayCommentInfoFragment.COMMENT_KEY, comment);
                commentFragment.setArguments(b);
                commentFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), "commentfragment");

            }
        });
    }


    private void initCheckBoxListeners(CheckBox checkBox, final int position) {

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    listPosToBeRemoved[position] = true;
                else
                    listPosToBeRemoved[position] = false;
            }
        });

    }


    public boolean[] getListPosToBeRemoved() {

        return listPosToBeRemoved;
    }

    public void setData(Object[][] data) {
        this.data = data;
        this.listPosToBeRemoved = new boolean[data.length];
    }
}
