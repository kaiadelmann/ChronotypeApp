package de.weissaufgrau.adelmann.mctq;

import android.widget.TimePicker;

public interface TimePickerDialogListener {
    public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute);
}
