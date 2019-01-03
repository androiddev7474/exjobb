package calendar.test.tools.se.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter <String> {

    private Context context;
    private String[] values;
    private int resLayout;

    public MyArrayAdapter(@NonNull Context context, int resLayout, String[] values) {
        super(context, resLayout, values);

        this.context = context;
        this.resLayout = resLayout;
        this.values = values;



    }


    private static class ViewHolder {
        TextView textView1;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resLayout, parent, false);
            holder = new ViewHolder();
            holder.textView1 = (TextView) convertView.findViewById(R.id.year_id);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView1.setText(values[position]);
        //fade(holder.textView1);


        return convertView;

    }

    private void fade(TextView txtView) {



    }
}
