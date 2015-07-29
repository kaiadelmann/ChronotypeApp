package de.weissaufgrau.adelmann.mctq;

import android.widget.TimePicker;

public interface TimePickerDialogListener {
    void onTimeSet(int id, TimePicker view, int hourOfDay, int minute);
}
