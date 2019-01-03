package expenditurediary.tool.se.expenditurediary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import test1.sql.se.sqltest.SQLhelper;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Object[] baseItems;
    private Object[][] subItems;

    private Context context;

    private SQLhelper sqLhelper;

    public MyExpandableListViewAdapter(Context context, Object[] baseItems, Object[][] subItems) {

        this.context = context;
        this.baseItems = baseItems;
        this.subItems = subItems;

        sqLhelper = new SQLhelper(context);
    }


    @Override
    public int getGroupCount() {

        return baseItems.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return subItems[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {

        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return 0;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    private static class ViewHolder {

        TextView groupLabel;
        TextView subLabel;
        ImageView subIcon;

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.unitdate_groupviewcontent, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.groupLabel = convertView.findViewById(R.id.groupview);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.groupLabel.setText(baseItems[groupPosition].toString());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.unitdate_subviewcontent, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.subLabel = convertView.findViewById(R.id.subview);
            viewHolder.subIcon = convertView.findViewById(R.id.subview_icon);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final String subItemName = subItems[groupPosition][childPosition].toString();
        viewHolder.subLabel.setText(subItemName);

        if (childPosition == 0)
            viewHolder.subIcon.setImageResource(R.drawable.year_48);
        else
            viewHolder.subIcon.setImageResource(R.drawable.month_48);

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }


}
