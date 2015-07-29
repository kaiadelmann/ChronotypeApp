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

import java.util.Calendar;
import java.util.Date;


public class DisplayMCTQ3Activity extends ActionBarActivity implements TimePickerDialogListener, NumberPickerFragment.OnNumberDialogDoneListener {

    private static final int MCTQ_B3_A1_ID = 1;
    private static final int MCTQ_B3_A2_ID = 2;
    private static final int MCTQ_B3_A4_ID = 3;
    private static final int MCTQ_B3_A3_ID = 4;
    private static final int MCTQ_B3_A7_ID = 5;
    private static final int MCTQ_B3_A8_ID = 6;

    private boolean busybee = false;
    private int wd = 0;
    private Date btw = new Date();
    private Date sprepw = new Date();
    private Date sew = new Date();
    private boolean alarmw = false;
    private int slatw = 5;
    private int siw = 5;
    private TextView tp1;
    private TextView tp2;
    private TextView tp3;
    private TextView tp6;
    private Calendar cal = Calendar.getInstance();
    private Date lef = new Date();
    private Date lew = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq3);

        //Get values from previous application activity
        Intent intent = getIntent();
        busybee = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, false);
        wd = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD, 0);

        //Set default value for bedtime to 21:00 ("Ich gehe ins Bett um xx")
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.MINUTE, 0);
        btw = cal.getTime();

        //Set default value for preparationtime to 21:10 ("Ich bin bereit einzuschlafen um xx")
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.MINUTE, 10);
        sprepw = cal.getTime();

        //Set default value for uptime to 08:00 ("Ich wache auf um xx")
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        sew = cal.getTime();

        //Set default value for light exposure to 02:00 ("Ich verbringe durchschnittlich xx:xx Stunden im Freien(Arbeitstage)")
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 0);
        lew = cal.getTime();

        //Set default value for light exposure to 02:00 ("Ich verbringe durchschnittlich xx:xx Stunden im Freien(Freie Tage)")
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 0);
        lef = cal.getTime();

        //Display default values in textview-elements
        tp1 = (TextView) findViewById(R.id.MCTQ_block_3_a1_id);
        tp1.setText(Utility.timeToString24h(btw));
        tp2 = (TextView) findViewById(R.id.MCTQ_block_3_a2_id);
        tp2.setText(Utility.timeToString24h(sprepw));
        tp3 = (TextView) findViewById(R.id.MCTQ_block_3_a4_id);
        tp3.setText(Utility.timeToString24h(sew));
        tp6 = (TextView) findViewById(R.id.MCTQ_block_3_a8_id);
        tp6.setText(Utility.timeToString24h(lew));
        TextView np1 = (TextView) findViewById(R.id.MCTQ_block_3_a3_id);
        np1.setText(Integer.toString(slatw));
        TextView np2 = (TextView) findViewById(R.id.MCTQ_block_3_a7_id);
        np2.setText(Integer.toString(siw));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_mctq3, menu);
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
     * Shows TimePicker for answer 1 in block 3
     *
     * @param view The View in question
     */
    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B3_A1_ID, btw);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /**
     * Shows TimePicker for answer 2 in block 3
     * * Question: "Ich bin bereit einzuschlafen um xx"
     * Answer has to be after the time given in TimePickerDialog (btw)
     *
     * @param view The View in question
     */
    public void showTimePickerDialog2(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B3_A2_ID, btw);
        newFragment.show(getFragmentManager(), "timePicker2");
    }

    /**
     * Shows TimePicker for answer 4 in block 3
     * Question: "Ich wache auf um xx"
     * Answer has to be after the time given in TimePickerDialog2 (sprepw)
     *
     * @param view The View in question
     */
    public void showTimePickerDialog3(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B3_A4_ID, sew);
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
            btw = time;
            tp1.setText(Utility.timeToString24h(time));
            Date time2 = Utility.timestringToDate24h(hourOfDay + ":10");
            tp2.setText(Utility.timeToString24h(time));
        }

        if (id == 2) {
            sprepw = time;
            tp2.setText(Utility.timeToString24h(time));
        }

        if (id == 3) {
            sew = time;
            tp3.setText(Utility.timeToString24h(time));
        }

        if (id == 6) {
            lew = time;
            tp6.setText(Utility.timeToString24h(time));
        }
    }

    /**
     * Show NumberPicker for answer 3 in block 3 with values from 0 to 60, set default value to 5
     *
     * @param view The View in question
     */
    public void showNumberPickerDialog1(View view) {
        DialogFragment newFragment = NumberPickerFragment.newInstance(1, 5, MCTQ_B3_A3_ID, 0, 60);
        newFragment.show(getFragmentManager(), "numberPicker4");
    }

    /**
     * Show NumberPicker for answer 7 in block 3 with values from 0 to 60, set default value to 5
     *
     * @param view The View in question
     */
    public void showNumberPickerDialog2(View view) {
        DialogFragment newFragment = NumberPickerFragment.newInstance(1, 5, MCTQ_B3_A7_ID, 0, 60);
        newFragment.show(getFragmentManager(), "numberPicker5");
    }

    /**
     * Method called when NumberPicker is done
     *
     * @param value The picked value
     * @param id    The id of the TextView to use
     */
    @Override
    public void onDone(int value, int id) {

        if (id == 4) {
            TextView tv = (TextView) findViewById(R.id.MCTQ_block_3_a3_id);
            slatw = value;
            tv.setText(Integer.toString(value));
        }

        if (id == 5) {
            TextView tv = (TextView) findViewById(R.id.MCTQ_block_3_a7_id);
            siw = value;
            tv.setText(Integer.toString(value));
        }
    }

    public void odWithAlarm(View view) {
        alarmw = true;
    }

    public void odWithoutAlarm(View view) {
        alarmw = false;
    }

    public void showMCTQ2(View view) {
        Intent intent = new Intent(this, DisplayMCTQ2Activity.class);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, busybee);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD, wd);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTW, btw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPW, sprepw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SLATW, slatw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEW, sew);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_ALARMW, alarmw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SIW, siw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_LEF, lef);
        startActivity(intent);
    }

    /**
     * Shows TimePicker for answer 8 in block 3
     * Question: "Im Durchschnitt verbringe ich..."
     *
     * @param view The View in question
     */
    public void showTimePickerDialog4(View view) {
        DialogFragment newFragment = TimePickerFragment.newInstance(MCTQ_B3_A8_ID, lew);
        newFragment.show(getFragmentManager(), "timePicker4");
    }
}
