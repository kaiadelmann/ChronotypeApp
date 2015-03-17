package de.weissaufgrau.adelmann.mctq;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class DisplayMCTQ5Activity extends ActionBarActivity {

    private String chronotype = "";
    private HashMap<String, String> queryStringValues = new HashMap<>();
    private HashMap<String, Long> queryDateValues = new HashMap<>();
    private HashMap<String, Integer> queryIntValues = new HashMap<>();
    private HashMap<String, Boolean> queryBooleanValues = new HashMap<>();
    private HashMap<String, Float> queryFloatValues = new HashMap<>();
    private Account mAccount = new Account(MainActivity.ACCOUNT, MainActivity.ACCOUNT_TYPE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq5);

        Intent intent = getIntent();
        boolean busybee = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, false);
        int workdays = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WORKDAYS, 0);
        Date odbedtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_BEDTIME);
        Date odpreparationtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_PREPARATIONTIME);
        int odtimetillsleeping = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_TIMETILLSLEEPING, 0);
        Date oduptime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_UPTIME);
        boolean odalarmclock = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_ALARMCLOCK, true);
        int odtimetillgettingup = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_OD_TIMETILLGETTINGUP, 0);
        Date wdbedtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_BEDTIME);
        Date wdpreparationtime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_PREPARATIONTIME);
        int wdtimetillsleeping = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_TIMETILLSLEEPING, 0);
        Date wduptime = (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_UPTIME);
        boolean wdalarmclock = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_ALARMCLOCK, true);
        int wdtimetillgettingup = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD_TIMETILLGETTINGUP, 0);
        String comments = intent.getStringExtra(DisplayMCTQActivity.EXTRA_MCTQ_COMMENTS);
        boolean uploaded = false;

        if (odbedtime == null) {
            odbedtime = new Date();
        }
        if (odpreparationtime == null) {
            odpreparationtime = new Date();
        }
        if (oduptime == null) {
            oduptime = new Date();
        }
        if (wdbedtime == null) {
            wdbedtime = new Date();
        }
        if (wdpreparationtime == null) {
            wdpreparationtime = new Date();
        }
        if (wduptime == null) {
            wduptime = new Date();
        }
        if (comments == null) {
            comments = "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.GERMANY);

        TextView tv = (TextView) findViewById(R.id.MCTQ_Auswertung);

        //Berechnen der Einschlafzeit in Industrieminuten (Dezimal statt Uhrzeit) an Werktagen
        Calendar wdsleeponsettime = Calendar.getInstance();
        wdsleeponsettime.setTime(wdpreparationtime);
        wdsleeponsettime.add(Calendar.MINUTE, wdtimetillsleeping);
        int wdsleeponsethour = wdsleeponsettime.get(Calendar.HOUR_OF_DAY);
        float wdsleeponsetminutesdecimal = ((float) wdsleeponsettime.get(Calendar.MINUTE) / 60);
        float sow = wdsleeponsethour + wdsleeponsetminutesdecimal;

        //Berechnen der Schlafdauer in Industrieminuten (Dezimal statt Uhrzeit) an Werktagen
        Calendar wdwakeuptime = Calendar.getInstance();
        wdwakeuptime.setTime(wduptime);
        int wdwakeuptimehour = wdwakeuptime.get(Calendar.HOUR_OF_DAY);
        float wdwakeuptimeminutesdecimal = ((float) wdwakeuptime.get(Calendar.MINUTE) / 60);
        float sdw = wdwakeuptimehour + wdwakeuptimeminutesdecimal;

        //Berechnen der Schlafmitte an Werktagen
        float msw = sow + (sdw / 2);
        if (msw > 24) {
            msw = msw - 24;
        }

        //Berechnen der Einschlafzeit in Industrieminuten (Dezimal statt Uhrzeit) an freien Tagen

        odpreparationtime.getTime();

        Calendar odsleeponsettime = Calendar.getInstance();
        odsleeponsettime.setTime(odpreparationtime);
        odsleeponsettime.add(Calendar.MINUTE, odtimetillsleeping);
        int odsleeponsethour = odsleeponsettime.get(Calendar.HOUR_OF_DAY);
        float odsleeponsetminutesdecimal = ((float) odsleeponsettime.get(Calendar.MINUTE) / 60);
        float sof = odsleeponsethour + odsleeponsetminutesdecimal;

        //Berechnen der Schlafdauer in Industrieminuten (Dezimal statt Uhrzeit) an freien Tagen
        Calendar odwakeuptime = Calendar.getInstance();
        odwakeuptime.setTime(oduptime);
        int odwakeuptimehour = odwakeuptime.get(Calendar.HOUR_OF_DAY);
        float odwakeuptimeminutesdecimal = ((float) odwakeuptime.get(Calendar.MINUTE) / 60);
        float sdf = odwakeuptimehour + odwakeuptimeminutesdecimal;

        //Berechnen der Schlafmitte an freien Tagen
        float msf = sof + (sdf / 2);
        if (msf > 24) {
            msf = msf - 24;
        }

        //Berechnen der Schlafdauer im Wochendurchscnitt
        // Formel: Schlafdauer werktags mal Anzahl der Werktage plus Schlafdauer an freien Tagen * Anzahl der freien Tage (welche sich aus der Differenz von 7 Wochentagen und den angegebenen Werktagen berechnet) geteilt durch 7 (Wochentage)
        float sdweek = (sdw * workdays) + (sdf * 7 - workdays) / 7;

        //Korrektur der Schlafmitte um "Überschlaf" an freien Tagen, wenn ein unterschied zwischen der Schlafdauer an Werk- und freien Tagen besteht
        float msfsc;
        if (sdw == sdf) {
            msfsc = msf;
        } else {
            msfsc = msf - (sdf - sdweek) / 2;
        }

        //Berechnung des "social jetlag", also der Differenz aus MSF und MSW
        float socialjetlag = Math.abs(msf - msw);

        //Ausgabe des Chronotyps (Mitte bei 4, darunter Frühtyp, darüber Spättyp)
        if (socialjetlag < 4) {
            chronotype = "Frühtyp";
        }
        if (socialjetlag > 4) {
            chronotype = "Spättyp";
        }
        if (socialjetlag == 4) {
            chronotype = "ausgewogen";
        }

        int chronotype_id = Math.round(socialjetlag);

        queryStringValues.put("comments", comments);

        queryDateValues.put("workdays_bedtime", wdbedtime.getTime());
        queryDateValues.put("workdays_readytosleeptime", wdpreparationtime.getTime());
        queryDateValues.put("workdays_uptime", wduptime.getTime());
        queryDateValues.put("offdays_bedtime", odbedtime.getTime());
        queryDateValues.put("offdays_readytosleeptime", odpreparationtime.getTime());
        queryDateValues.put("offdays_uptime", oduptime.getTime());

        queryIntValues.put("workdays_minutestillsleep", wdtimetillsleeping);
        queryIntValues.put("workdays_minutestillup", wdtimetillgettingup);
        queryIntValues.put("offdays_minutestillsleep", odtimetillsleeping);
        queryIntValues.put("offdays_minutestillup", odtimetillgettingup);
        queryIntValues.put("chronotype_id", chronotype_id);

        queryBooleanValues.put("offdays_alarmclock", odalarmclock);
        queryBooleanValues.put("workdays_alarmclock", wdalarmclock);
        queryBooleanValues.put("uploaded", uploaded);

        queryFloatValues.put("sow", sow);
        queryFloatValues.put("sdw", sdw);
        queryFloatValues.put("msw", msw);
        queryFloatValues.put("sof", sof);
        queryFloatValues.put("sdf", sdf);
        queryFloatValues.put("msf", msf);
        queryFloatValues.put("msfsc", msfsc);
        queryFloatValues.put("socialjetlag", socialjetlag);

        tv.setText("berechneter Chronotyp: " + chronotype
                + "\n\n\nchronotype_ID: " + chronotype_id
                + "\n\n\nsow: " + sow
                + "\nsdw: " + sdw
                + "\nwdwakeuptimehour: " + wdwakeuptimehour
                + "\nwdwakeuptimeminutesdecimal: " + wdwakeuptimeminutesdecimal
                + "\nmsw: " + msw
                + "\n\n\nsof: " + sof
                + "\nsdf: " + sdf
                + "\nmsf: " + msf
                + "\nodwakeuptimehour: " + odwakeuptimehour
                + "\nodwakeuptimeminutesdecimal: " + odwakeuptimeminutesdecimal
                + "\nmsfsc: " + msfsc
                + "\nsocialjetlag: " + socialjetlag
                + "\n\n\nbusybee: " + busybee
                + "\nworkdays: " + workdays
                + "\nodbedtime: "
                + dateFormat.format(odbedtime)
                + "\nodpreparationtime: "
                + dateFormat.format(odpreparationtime)
                + "\nodtimetillsleeping: "
                + odtimetillsleeping
                + "\noduptime: "
                + dateFormat.format(oduptime)
                + "\nodalarmclock: "
                + odalarmclock
                + "\nodtimetillgettingup: "
                + odtimetillgettingup
                + "\nwdbedtime: "
                + dateFormat.format(wdbedtime)
                + "\nwdpreparationtime: "
                + dateFormat.format(wdpreparationtime)
                + "\nwdtimetillsleeping: "
                + wdtimetillsleeping
                + "\nwduptime: "
                + dateFormat.format(wduptime)
                + "\nwdalarmclock: "
                + wdalarmclock
                + "\nwdtimetillgettingup: "
                + wdtimetillgettingup
                + "\ncomments: "
                + comments);
    }

    /**
     * Prüfen, ob Netzwerk vorhanden und verbunden ist
     *
     * @return true, wenn Netzwerk vorhanden und online
     */
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void saveOnline(View view) {
        //Neue DBController-Instanz erzeugen
        final DBController controller = new DBController(this);
        //Alle evtl. vorhandenen Daten löschen, da immer nur ein Datensatz gespeichert wird,
        controller.truncTable("mctq_data");
        //Daten in Tabelle schreiben und somit lokal speichern
        controller.insertData(queryStringValues, queryBooleanValues, queryDateValues, queryIntValues, queryFloatValues);

        //Upload der Daten, wenn Netzwerk verfübgar.
        if (isOnline()) {
            //Create AsycHttpClient object
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            params.put("mctqDataJSON", controller.composeJSONfromSQLite());
            client.post("http://weissaufgrau.de/mctq_db_adapter/insertdata.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    System.out.println("#######################" + response);
                }
            });
        } else {
            /*
            * Request the sync for the default account, authority
            * Pass the settings flags by inserting them in a bundle
            */
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            settingsBundle.putString("mctqDataJSON", controller.composeJSONfromSQLite());
            ContentResolver.setSyncAutomatically(mAccount, MainActivity.AUTHORITY, true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_mctq5, menu);
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

    public void ende(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DisplayMCTQActivity.EXTRA_MCTQ_EXIT, true);
        startActivity(intent);
        finish();
    }
}
