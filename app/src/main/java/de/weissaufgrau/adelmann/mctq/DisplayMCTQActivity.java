package de.weissaufgrau.adelmann.mctq;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;


public class DisplayMCTQActivity extends ActionBarActivity implements NumberPickerFragment.OnNumberDialogDoneListener, DataUploadDialogFragment.DataUploadDialogListener {

    public static final String EXTRA_MCTQ_BUSYBEE = "de.weissaufgrau.adelmann.mctq.BUSYBEE";
    public static final String EXTRA_MCTQ_WORKDAYS = "de.weissaufgrau.adelmann.mctq.WORKDAYS";
    public static final String EXTRA_MCTQ_WD_BEDTIME = "de.weissaufgrau.adelmann.mctq.WORKDAYSBEDTIME";
    public static final String EXTRA_MCTQ_WD_PREPARATIONTIME = "de.weissaufgrau.adelmann.mctq.WORKDAYSPREPARATIONTIME";
    public static final String EXTRA_MCTQ_WD_TIMETILLSLEEPING = "de.weissaufgrau.adelmann.mctq.WORKDAYSTIMETILLSLEEPING";
    public static final String EXTRA_MCTQ_WD_UPTIME = "de.weissaufgrau.adelmann.mctq.WORKDAYSUPTIME";
    public static final String EXTRA_MCTQ_WD_ALARMCLOCK = "de.weissaufgrau.adelmann.mctq.WORKDAYSALARMCLOCK";
    public static final String EXTRA_MCTQ_WD_TIMETILLGETTINGUP = "de.weissaufgrau.adelmann.mctq.WORKDAYSTIMETILLGETTINGUP";
    public static final String EXTRA_MCTQ_OD_BEDTIME = "de.weissaufgrau.adelmann.mctq.OFFDAYSBEDTIME";
    public static final String EXTRA_MCTQ_OD_PREPARATIONTIME = "de.weissaufgrau.adelmann.mctq.OFFDAYSPREPARATIONTIME";
    public static final String EXTRA_MCTQ_OD_TIMETILLSLEEPING = "de.weissaufgrau.adelmann.mctq.OFFDAYSTIMETILLSLEEPING";
    public static final String EXTRA_MCTQ_OD_UPTIME = "de.weissaufgrau.adelmann.mctq.OFFDAYSUPTIME";
    public static final String EXTRA_MCTQ_OD_ALARMCLOCK = "de.weissaufgrau.adelmann.mctq.OFFDAYSALARMCLOCK";
    public static final String EXTRA_MCTQ_OD_TIMETILLGETTINGUP = "de.weissaufgrau.adelmann.mctq.OFFDAYSTIMETILLGETTINGUP";
    public static final String EXTRA_MCTQ_COMMENTS = "de.weissaufgrau.adelmann.mctq.COMMENTS";
    public static final String EXTRA_MCTQ_EXIT = "de.weissaufgrau.adelmann.mctq.EXIT";

    private static final int MCTQ_B1_A2_ID = 1;

    private boolean busybee = false;
    private int workdays = 0;
    private DBController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq);

        //Check, ob bereits Daten in der DB vorhanden sind, die noch hochgeladen werden sollen
        controller = new DBController(this);
        int count = controller.dbSyncCount();
        if (count > 0) {
            DialogFragment uploadfragment = new DataUploadDialogFragment();
            uploadfragment.show(getFragmentManager(), "datauploaddialog");
        }
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the DataUploadDialogFragment.DataUploadDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        //Upload der Daten, wenn Netzwerk verfübgar.
        if (Utility.isOnline(this)) {
            Utility.doUpload("http://10.0.2.2/mctq_db_adapter/insertdata.php", "mctqDataJSON", controller.composeJSONfromSQLite(), new AsyncHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseString, Throwable throwable) {
                            // called when response HTTP status is "4xx" (i.e. 401, 403, 404)
                            Toast.makeText(getApplicationContext(), "Fehler: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                            // called when response HTTP status is "200 OK"
                            controller.updateSyncStatus();
                            Toast.makeText(getApplicationContext(), "Daten hochgeladen!", Toast.LENGTH_LONG).show();
                        }

                    }
            );
        } else {
            Toast.makeText(getApplicationContext(), "Nicht online, bitte später versuchen!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        // Daten verwerfen
        controller.truncTable("mctq_data");
        Toast.makeText(getApplicationContext(), "Daten gelöscht!", Toast.LENGTH_LONG).show();
        controller.close();
    }

    /**
     * Called when the user clicks radio-button 'ja'
     */
    public void busybee(View view) {
        busybee = true;
        workdays = 5;
        TextView tv = (TextView) findViewById(R.id.MCTQ_block_1_a2_id);
        tv.setText(String.valueOf(workdays));
        LinearLayout llcontainer2 = (LinearLayout) findViewById(R.id.llcontainer2);
        llcontainer2.setVisibility(View.VISIBLE);
    }

    /**
     * Called when the user clicks radio-button 'nein'
     */
    public void lazydog(View view) {
        busybee = false;
        workdays = 0;
        TextView tv = (TextView) findViewById(R.id.MCTQ_block_1_a2_id);
        tv.setText(String.valueOf(workdays));
        LinearLayout llcontainer2 = (LinearLayout) findViewById(R.id.llcontainer2);
        llcontainer2.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_mctq, menu);
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
     * Show NumberPicker for answer 2 in block 1 with values from 1 to 7, set default value to 5
     *
     * @param view The view in question
     */
    public void showNumberPickerDialog(View view) {
        DialogFragment newFragment = NumberPickerFragment.newInstance(1, 5, MCTQ_B1_A2_ID, 1, 7);
        newFragment.show(getFragmentManager(), "numberPicker");
    }

    @Override
    public void onDone(int value, int id) {
        TextView tv;

        if (id == 1) {
            tv = (TextView) findViewById(R.id.MCTQ_block_1_a2_id);
            workdays = value;
            tv.setText(value);
        }
    }

    public void showMCTQnext(View view) {
        Intent intent;

        if (busybee) {
            intent = new Intent(this, DisplayMCTQ3Activity.class);
            intent.putExtra(EXTRA_MCTQ_BUSYBEE, busybee);
            intent.putExtra(EXTRA_MCTQ_WORKDAYS, workdays);
        } else {
            intent = new Intent(this, DisplayMCTQ2Activity.class);
            intent.putExtra(EXTRA_MCTQ_BUSYBEE, busybee);
            intent.putExtra(EXTRA_MCTQ_WORKDAYS, workdays);
        }

        startActivity(intent);
    }
}
