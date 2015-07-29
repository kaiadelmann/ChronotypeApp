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
    private int wd = 0;
    private Date btw = new Date();
    private Date sprepw = new Date();
    private Date sew = new Date();
    private boolean alarmw = true;
    private int slatw = 0;
    private int siw = 0;
    private Date btf = new Date();
    private Date sprepf = new Date();
    private Date sef = new Date();
    private boolean alarmf = true;
    private int slatf = 0;
    private int sif = 0;
    private String comments = "";
    private Date lew = new Date();
    private Date lef = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq4);

        Intent intent = getIntent();
        busybee = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, false);
        wd = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD, 0);
        btf = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTF);
        sprepf = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPF);
        slatf = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SLATF, 0);
        sef = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEF);
        alarmf = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_ALARMF, true);
        sif = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SIF, 0);
        btw = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTW);
        sprepw = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPW);
        slatw = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SLATW, 0);
        sew = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEW);
        alarmw = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_ALARMW, true);
        siw = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SIW, 0);
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
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD, wd);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTF, btf);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPF, sprepf);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SLATF, slatf);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEF, sef);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_ALARMF, alarmf);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SIF, sif);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTW, btw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPW, sprepw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SLATW, slatw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEW, sew);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_ALARMW, alarmw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_SIW, siw);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_COMMENTS, comments);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_LEW, lew);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_LEF, lef);
        startActivity(intent);
    }
}
