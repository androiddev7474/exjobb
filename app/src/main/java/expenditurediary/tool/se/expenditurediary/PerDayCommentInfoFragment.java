package expenditurediary.tool.se.expenditurediary;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PerDayCommentInfoFragment extends DialogFragment {

    //bundle key
    public static final String COMMENT_KEY = "daily comment";

    private ImageView close;
    private TextView content;
    private View commentInfoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle onSavedInstanceState) {

        commentInfoView = inflater.inflate(R.layout.comment_info_fragment_content, containter, false);
        content = commentInfoView.findViewById(R.id.comment_content_id);
        close = commentInfoView.findViewById(R.id.comment_info_close);

        initClickListener();

        String comment = getArguments().getString(COMMENT_KEY, "no comment");
        content.setText(comment);

        return commentInfoView;
    }


    private void initClickListener() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }

}
