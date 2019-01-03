package expenditurediary.tool.se.expenditurediary;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IconsGridViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    int[] iconRes;

    public IconsGridViewAdapter(Context context) {

        this.context = context;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        initDrawableRes();

    }


    private void initDrawableRes() {

        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.icons_drawable);
        iconRes = new int[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++)
            iconRes[i] = typedArray.getResourceId(i, 0);


    }

    @Override
    public int getCount() {

        return iconRes.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class ViewHolder {

        ImageView icon;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.icons_gridview_content, parent, false);
            holder.icon = convertView.findViewById(R.id.icon_id);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder)convertView.getTag();


        holder.icon.setImageResource(iconRes[position]);

        return convertView;
    }


    public int[] getIconRes() {
        return iconRes;
    }
}
