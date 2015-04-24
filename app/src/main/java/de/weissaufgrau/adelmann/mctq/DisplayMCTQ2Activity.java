package de.weissaufgrau.adelmann.mctq;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;


public class DisplayMCTQ2Activity extends ActionBarActivity implements TimePickerDialogListener, NumberPickerFragment.OnNumberDialogDoneListener {

    private static final int MCTQ_B2_A1_ID = 1;
    private static final int MCTQ_B2_A2_ID = 2;
    private static final int MCTQ_B2_A4_ID = 3;
    private static final int MCTQ_B2_A3_ID = 4;
    private static final int MCTQ_B2_A7_ID = 5;

    private boolean busybee;
    private int workdays;
    private Date odbedtime;
    private Date odpreparationtime;
    private Date oduptime;
    private boolean odalarmclock;
    private int odtimetillsleeping;
    private int odtimetillgettingup;
    private Date wdbedtime;
    private Date wdpreparationtime;
    private Date wduptime;
    private boolean wdalarmclock;
    private int wdtimetillsleeping;
    private int wdtimetillgettingup;
    private TextView tp1;
    private TextView tp2;
    private TextView tp3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq2);
        Intent intent = getIntent();

        tp1 = (TextView) findViewById(R.id.MCTQ_block_2_a1_id);
        tp1.setText(Utility.timeToString24h(Utility.getNow()));
        tp2 = (TextView) findViewById(R.id.MCTQ_block_2_a2_id);
        tp2.setText(Utility.timeToString24h(Utility.getNowPlus10m()));
        tp3 = (TextView) findViewById(R.id.MCTQ_block_2_a4_id);
        tp3.setText(Utility.timeToString24h(Utility.getGetNowPlus8h()));

        busybee = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, false);
        workdays = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WORKDAYS, 0);
        wdbedtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_BEDTIME);
        wdpreparationtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_PREPARATIONTIME);
        wdtimetillsleeping = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_TIMETILLSLEEPING, 0);
        wduptime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_UPTIME);
        wdalarmclock = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_ALARMCLOCK, true);
        wdtimetillgettingup = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_TIMETILLGETTINGUP, 0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_mctq2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_credits) {
            Intent intent = new Intent(this, DisplayMCTQ6Activity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows TimePicker for answer 1 in block 2
     *
     * @param view The View in question
     */
    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B2_A1_ID);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Shows TimePicker for answer 2 in block 2
     *
     * @param view The View in question
     */
    public void showTimePickerDialog2(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B2_A2_ID, odbedtime);
        newFragment.show(getFragmentManager(), "timePicker2");
    }

    /**
     * Shows TimePicker for answer 4 in block 2
     *
     * @param view The View in question
     */
    public void showTimePickerDialog3(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B2_A4_ID);
        newFragment.show(getFragmentManager(), "timePicker3");
    }

    /**
     * Shows picked time in the correct TextView-Element
     *
     * @param id        The id of the TextView to use
     * @param view      The View in question
     * @param hourOfDay picked hour
     * @param minute    picked minute
     */
    @Override
    public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute) {
        Date time = Utility.timestringToDate24h(hourOfDay + ":" + minute);

        if (id == 1) {
            tp1.setText(Utility.timeToString24h(time));
            odbedtime = time;
        }

        if (id == 2) {
            tp2.setText(Utility.timeToString24h(time));
            odpreparationtime = time;
        }

        if (id == 3) {
            tp3.setText(Utility.timeToString24h(time));
            oduptime = time;
        }

    }

    /**
     * Show NumberPicker for answer 3 in block 2 with values from 0 to 60, set default value to 1
     *
     * @param view The View in question
     */
    public void showNumberPickerDialog1(View view) {
        DialogFragment newFragment = NumberPickerFragment.newInstance(1, 1, MCTQ_B2_A3_ID, 1, 60);
        newFragment.show(getFragmentManager(), "numberPicker2");
    }

    /**
     * Show NumberPicker for answer 7 in block 2 with values from 0 to 60, set default value to 1
     *
     * @param view The View in question
     */
    public void showNumberPickerDialog2(View view) {
        DialogFragment newFragment = NumberPickerFragment.newInstance(1, 1, MCTQ_B2_A7_ID, 1, 60);
        newFragment.show(getFragmentManager(), "numberPicker3");
    }

    @Override
    public void onDone(int value, int id) {

        if (id == 4) {
            TextView tv = (TextView) findViewById(R.id.MCTQ_block_2_a3_id);
            odtimetillsleeping = value;
            tv.setText(value);
        }

        if (id == 5) {
            TextView tv = (TextView) findViewById(R.id.MCTQ_block_2_a7_id);
            odtimetillgettingup = value;
            tv.setText(value);
        }
    }

    public void odWithAlarm(View view) {
        odalarmclock = true;
    }

    public void odWithoutAlarm(View view) {
        odalarmclock = false;
    }


    public void showMCTQ4(View view) {
        Intent intent = new Intent(this, DisplayMCTQ4Activity.class);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, busybee);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WORKDAYS, workdays);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_BEDTIME, odbedtime);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_PREPARATIONTIME, odpreparationtime);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_TIMETILLSLEEPING, odtimetillsleeping);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_UPTIME, oduptime);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_ALARMCLOCK, odalarmclock);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_TIMETILLGETTINGUP, odtimetillgettingup);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_BEDTIME, wdbedtime);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_PREPARATIONTIME, wdpreparationtime);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_TIMETILLSLEEPING, wdtimetillsleeping);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_UPTIME, wduptime);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_ALARMCLOCK, wdalarmclock);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_TIMETILLGETTINGUP, wdtimetillgettingup);
        startActivity(intent);
    }

}
