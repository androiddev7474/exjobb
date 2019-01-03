package expenditurediary.tool.se.expenditurediary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class IconsFragment extends DialogFragment {

    //intents (LocalBroadcastReceiver)
    public static final String ICONSFRAGMENT_INTENT = "icons frag intent";

    //viewtaggar
    public static final String OK_TAG_ICONSFRAG = "remove ok iconsfragment";
    public static final String CANCEL_TAG_ICONSFRAG = "remove cancel iconsfragment";

    int iconResID;
    private int resPosition;

    private View iconsView;
    private ImageView headerIcon;
    private GridView gridView;

    private IconsGridViewAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {

        iconsView = inflater.inflate(R.layout.icons_fragment_content, container, false);
        headerIcon = iconsView.findViewById(R.id.header_icon_iconsfrag);
        gridView = iconsView.findViewById(R.id.icons_gridview);
        adapter = new IconsGridViewAdapter(getContext());
        gridView.setAdapter(adapter);

        initOnItemClickListener();

        return iconsView;
    }


    private void initOnItemClickListener() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                iconResID = adapter.getIconRes()[position];
                headerIcon.setImageResource(iconResID);
                resPosition = position;

            }
        });

    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(IconsFragment.ICONSFRAGMENT_INTENT));


    }

    public int getIconResID() {
        return iconResID;
    }


    public int getResPosition() {

        return resPosition;
    }

}
