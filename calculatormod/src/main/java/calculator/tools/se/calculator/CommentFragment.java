package calculator.tools.se.calculator;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CommentFragment extends DialogFragment {

    public static final String CANCEL_TAG = "comment_fragment_cancel";
    public static final String OK_TAG = "comment_fragment_ok";
    public static final int COMMENT_TAG = 123;


    private View commentView;
    private EditText editText;
    private Button cancelButton, okButton;
    private boolean okPressed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        commentView = inflater.inflate(R.layout.fragment_comment, container, false);
        editText = commentView.findViewById(R.id.editComment);
        cancelButton = commentView.findViewById(R.id.cancelButton);
        okButton = commentView.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                okPressed = true;
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                okPressed = false;
                dismiss();
            }
        });


        return commentView;
    }

    @Override
    public void onPause() {
        super.onPause();

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Log.i("commentfragment onPause", "hide keyboard");


        //
    }

    @Override
    public void onStart() {
        super.onStart();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle b = new Bundle();
        b.putString(getContext().getString(R.string.CALCULATOR_COMMENT), editText.getText().toString());
        b.putBoolean(getContext().getString(R.string.COMMENT_OK), okPressed);
        setArguments(b);

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(getContext().getString(R.string.INTENT_GETACTION_NAME)));


    }






}
