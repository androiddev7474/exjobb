package expenditurediary.tool.se.expenditurediary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppPageAdapter extends FragmentPagerAdapter {

    public static final int N_FRAGMENTS = 2;
    private static final int EDIT_INDEX = 0;
    private static final int SUMMARY_INDEX = 1;

    private EditFragment editFragment;
    private SummaryFragment summaryFragment;

    private Context context;

    public AppPageAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;

        editFragment = EditFragment.newInstance("EditFragment");
        summaryFragment = SummaryFragment.newInstance("SummaryFragment");

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return N_FRAGMENTS;
    }

    @Override
    public Fragment getItem(int iFragment) {

       switch (iFragment) {

           case EDIT_INDEX:
               return editFragment;
           case SUMMARY_INDEX:
               return  summaryFragment;
           default:
               return editFragment;
       }

    }


}
