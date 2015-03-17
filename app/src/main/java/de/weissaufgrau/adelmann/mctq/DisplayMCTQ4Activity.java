package de.weissaufgrau.adelmann.mctq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class DisplayMCTQ4Activity extends ActionBarActivity {

    private boolean busybee = false;
    private int workdays = 0;
    private Date wdbedtime = new Date();
    private Date wdpreparationtime = new Date();
    private Date wduptime = new Date();
    private boolean wdalarmclock = true;
    private int wdtimetillsleeping = 0;
    private int wdtimetillgettingup = 0;
    private Date odbedtime = new Date();
    private Date odpreparationtime = new Date();
    private Date oduptime = new Date();
    private boolean odalarmclock = true;
    private int odtimetillsleeping = 0;
    private int odtimetillgettingup = 0;
    private String comments = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq4);

        Intent intent = getIntent();
        busybee = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, false);
        workdays = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WORKDAYS, 0);
        odbedtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_BEDTIME);
        odpreparationtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_PREPARATIONTIME);
        odtimetillsleeping = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_TIMETILLSLEEPING, 0);
        oduptime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_UPTIME);
        odalarmclock = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_ALARMCLOCK, true);
        odtimetillgettingup = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_TIMETILLGETTINGUP, 0);
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
        getMenuInflater().inflate(R.menu.menu_display_mctq4, menu);
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

    public void showMCTQ5(View view) {

        TextView tv = (TextView) findViewById(R.id.MCTQ_block_4_a1);
        comments = tv.getText().toString();

        Intent intent = new Intent(this, DisplayMCTQ5Activity.class);
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
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_COMMENTS, comments);

        startActivity(intent);
    }
}
