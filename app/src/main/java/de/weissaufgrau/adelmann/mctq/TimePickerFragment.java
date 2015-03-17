package de.weissaufgrau.adelmann.mctq;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by adelmann on 12.02.2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int mId;
    private TimePickerDialogListener mListener;
    private Date mDate;

    /**
     * newInstance-Contructor for generating a new TimePickerFragment with an connected ID, so one knows where the call came from
     *
     * @param id
     * @return
     */
    public static TimePickerFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("picker_id", id);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * newInstance-Contructor for generating a new TimePickerFragment with an connected ID and starttime, so one knows where the call came from and can check if the picked time is before or after the given time
     *
     * @param id
     * @param date
     * @return
     */
    public static TimePickerFragment newInstance(int id, Date date) {
        Bundle args = new Bundle();
        args.putInt("picker_id", id);
        args.putSerializable("start_time", date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mId = getArguments().getInt("picker_id");
        mDate = (Date) getArguments().getSerializable("start_time");
        mListener = getActivity() instanceof TimePickerDialogListener ? (TimePickerDialogListener) getActivity() : null;

        // Use the current time as the default values for the picker, if no other Date-object is given
        final Calendar c = Calendar.getInstance();
        if (mDate != null) {
            c.setTime(mDate);
        }

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mListener != null) mListener.onTimeSet(mId, view, hourOfDay, minute);
    }
}
