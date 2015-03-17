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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DisplayMCTQ2Activity extends ActionBarActivity implements TimePickerDialogListener, NumberPickerFragment.OnNumberDialogDoneListener {

    private static final int MCTQ_B2_A1_ID = 1;
    private static final int MCTQ_B2_A2_ID = 2;
    private static final int MCTQ_B2_A4_ID = 3;
    private static final int MCTQ_B2_A3_ID = 4;
    private static final int MCTQ_B2_A7_ID = 5;

    private boolean busybee = false;
    private int workdays = 0;
    private Date odbedtime = new Date();
    private Date odpreparationtime = new Date();
    private Date oduptime = new Date();
    private boolean odalarmclock = true;
    private int odtimetillsleeping = 0;
    private int odtimetillgettingup = 0;
    private Date wdbedtime = new Date();
    private Date wdpreparationtime = new Date();
    private Date wduptime = new Date();
    private boolean wdalarmclock = true;
    private int wdtimetillsleeping = 0;
    private int wdtimetillgettingup = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq2);
        Intent intent = getIntent();
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
     * @param view
     */
    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B2_A1_ID);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Shows TimePicker for answer 2 in block 2
     *
     * @param view
     */
    public void showTimePickerDialog2(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B2_A2_ID, odbedtime);
        newFragment.show(getFragmentManager(), "timePicker2");
    }

    /**
     * Shows TimePicker for answer 4 in block 2
     *
     * @param view
     */
    public void showTimePickerDialog3(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B2_A4_ID);
        newFragment.show(getFragmentManager(), "timePicker3");
    }

    /**
     * Shows picked time in the correct TextView-Element
     *
     * @param id
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute) {
        TextView tv = null;
        String time = hourOfDay + ":" + minute;

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.GERMANY);

        if (id == 1) {
            tv = (TextView) findViewById(R.id.MCTQ_block_2_a1_id);
            try {
                odbedtime = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (id == 2) {
            tv = (TextView) findViewById(R.id.MCTQ_block_2_a2_id);
            try {
                odpreparationtime = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (id == 3) {
            tv = (TextView) findViewById(R.id.MCTQ_block_2_a4_id);
            try {
                oduptime = dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        tv.setText(time);
    }

    /**
     * Show NumberPicker for answer 3 in block 2 with values from 0 to 60, set default value to 1
     *
     * @param view
     */
    public void showNumberPickerDialog1(View view) {
        DialogFragment newFragment = NumberPickerFragment.newInstance(1, 1, MCTQ_B2_A3_ID, 1, 60);
        newFragment.show(getFragmentManager(), "numberPicker2");
    }

    /**
     * Show NumberPicker for answer 7 in block 2 with values from 0 to 60, set default value to 1
     *
     * @param view
     */
    public void showNumberPickerDialog2(View view) {
        DialogFragment newFragment = NumberPickerFragment.newInstance(1, 1, MCTQ_B2_A7_ID, 1, 60);
        newFragment.show(getFragmentManager(), "numberPicker3");
    }

    @Override
    public void onDone(int value, int id) {
        TextView tv = null;

        if (id == 4) {
            tv = (TextView) findViewById(R.id.MCTQ_block_2_a3_id);
            odtimetillsleeping = value;
        }

        if (id == 5) {
            tv = (TextView) findViewById(R.id.MCTQ_block_2_a7_id);
            odtimetillgettingup = value;
        }

        System.out.println(tv.getText() + "----" + tv.getId());
        System.out.println(value);
        tv.setText("" + value);
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
