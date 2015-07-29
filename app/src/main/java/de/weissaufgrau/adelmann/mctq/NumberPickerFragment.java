package de.weissaufgrau.adelmann.mctq;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * Created by adelmann on 13.02.2015.
 */

public class NumberPickerFragment extends DialogFragment {
    private static final String ARG_numDials = "numDials";
    private static final String ARG_initValue = "initValue";
    private static final String ARG_pickerId = "picker_id";
    private static final String ARG_maxValue = "maxValue";
    private static final String ARG_minValue = "minValue";
    private int mId;
    private int numDials;
    private int currentValue;
    private int mMaxValue;
    private int mMinValue;
    private NumberPicker[] numPickers;
    private OnNumberDialogDoneListener mListener;

    public NumberPickerFragment() {
        // Required empty public constructor
    }

    public static NumberPickerFragment newInstance(int numDials, int initValue, int id, int minValue, int maxValue) {
        NumberPickerFragment numdialog = new NumberPickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_pickerId, id);
        args.putInt(ARG_numDials, numDials);
        args.putInt(ARG_initValue, initValue);
        args.putInt(ARG_maxValue, maxValue);
        args.putInt(ARG_minValue, minValue);
        numdialog.setArguments(args);
        return numdialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_pickerId);
            numDials = getArguments().getInt(ARG_numDials);
            currentValue = getArguments().getInt(ARG_initValue);
            mMaxValue = getArguments().getInt(ARG_maxValue);
            mMinValue = getArguments().getInt(ARG_minValue);
            numPickers = new NumberPicker[numDials];
        }
        if (savedInstanceState != null) {
            currentValue = savedInstanceState.
                    getInt("CurrentValue");
        }
    }

    private int getDigit(int d, int i) {
        String temp = Integer.toString(d);
        if (temp.length() <= i) return 0;
        int r = Character.getNumericValue(temp.charAt(temp.length() - i - 1));
        return r;
    }

    private int getValue() {
        int value = 0;
        int mult = 1;
        for (int i = 0; i < numDials; i++) {
            value += numPickers[i].getValue() * mult;
            mult *= 10;
        }
        return value;
    }

    public void onValueChange(NumberPicker view, int oldVal, int newVal, int id) {
        if (mListener != null) mListener.onDone(getValue(), id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linLayoutH = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutH.setLayoutParams(params);
        for (int i = numDials - 1; i >= 0; i--) {
            numPickers[i] = new NumberPicker(getActivity());
            numPickers[i].setMaxValue(mMaxValue);
            numPickers[i].setMinValue(mMinValue);
            numPickers[i].setValue(getDigit(currentValue, i));
            linLayoutH.addView(numPickers[i]);
        }
        LinearLayout linLayoutV = new LinearLayout(getActivity());
        linLayoutV.setOrientation(LinearLayout.VERTICAL);
        linLayoutV.addView(linLayoutH);
        Button okButton = new Button(getActivity());
        okButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentValue = getValue();
                        if (mListener != null) {
                            mListener.onDone(currentValue, mId);
                        }
                        dismiss();
                    }
                });
        params.gravity = Gravity.CENTER_HORIZONTAL;
        okButton.setLayoutParams(params);
        okButton.setText("OK");
        linLayoutV.addView(okButton);
        return linLayoutV;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNumberDialogDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnNumberDialogDoneListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentValue", getValue());
    }

    public interface OnNumberDialogDoneListener {
        void onDone(int value, int id);
    }
}
