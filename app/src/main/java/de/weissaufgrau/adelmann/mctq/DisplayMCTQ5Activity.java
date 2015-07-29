package de.weissaufgrau.adelmann.mctq;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
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

    private Date lew = new Date(); //Light exposure (workdays)
    private Date lef = new Date(); //Light exposure (work-free days)

    private Long sow = (long) 0; // Sleep onset (workdays) = SPrepW+SLatW
    private Long sdw = (long) 0; //Sleep duration (workdays) = SEw-SOw
    private Long tbtw = (long) 0; // Total time in bed (workdays) = GUw-BTw
    private Long msw = (long) 0; //Mid-Sleep (workdays) = SOw+SDw/2
    private Long msfsc = (long) 0; // Chronotype (only computable if alarmf=false) = If SDf<=SDw: MSF, if SDf>SDw: MSF-(SDf-SDweek)/2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq5);

        Intent intent = getIntent();
        /*busybee = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_BUSYBEE, false);*/
        int wd = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_WD, 0);
        Date btf = intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTF) != null ? (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTF) : new Date();
        Date sprepf = intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPF) != null ? (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPF) : new Date();
        int slatf = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SLATF, 0);
        Date sef = intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEF) != null ? (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEF) : new Date();
        boolean alarmf = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_ALARMF, true);
        int sif = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SIF, 0);
        Date btw = intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTW) != null ? (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_BTW) : new Date();
        Date sprepw = intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPW) != null ? (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SPREPW) : new Date();
        int slatw = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SLATW, 0);
        Date sew = intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEW) != null ? (Date) intent.getSerializableExtra(DisplayMCTQActivity.EXTRA_MCTQ_SEW) : new Date();
        boolean alarmw = intent.getBooleanExtra(DisplayMCTQActivity.EXTRA_MCTQ_ALARMW, true);
        int siw = intent.getIntExtra(DisplayMCTQActivity.EXTRA_MCTQ_SIW, 0);
        String comments = intent.getStringExtra(DisplayMCTQActivity.EXTRA_MCTQ_COMMENTS);

        if (comments == null) {
            comments = "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", Locale.GERMANY);

        TextView tvauswertung = (TextView) findViewById(R.id.MCTQ_Auswertung);
        TextView tvcolldata = (TextView) findViewById(R.id.MCTQ_collecteddata);
        ImageView imglark = (ImageView) findViewById(R.id.lark);
        ImageView imgowl = (ImageView) findViewById(R.id.owl);

        //Computed variables
        int fd = 7 - wd;
        Long sof = sprepf.getTime() + slatf;
        Long guf = sef.getTime() + sif;
        Long tbtf = guf - btf.getTime();
        Long sdf = sef.getTime() - sof;
        Long msf = (sof + sdf) / 2;

        //Computed only if workdays >0
        if (wd > 0) {
            sow = sprepw.getTime() + slatw;
            sdw = sew.getTime() - sow;
            Long guw = sew.getTime() + siw;
            tbtw = guw - btw.getTime();
            msw = (sow + sdw) / 2;
        }

        Long leweek = (lew.getTime() * wd + lef.getTime() * fd) / 7;
        Long sdweek = (sdw * wd + sdf * fd) / 7;
        /*Long sjlrel = msf - msw;*/
        Long sjl = Math.abs(msf - msw);

        //Calculation of chronotype only if alarmf=false (person wakes up on work-free days without alarmclock)
        if (!alarmf) {
            if (sdf <= sdw) {
                msfsc = msf;
            } else {
                msfsc = msf - (sdf - sdweek) / 2;
            }

            //Ausgabe des Chronotyps (Mitte bei 4, darunter Frühtyp, darüber Spättyp)
            if (msfsc < 4) {
                chronotype = "Frühtyp (\"Lerche\")";
                imglark.setVisibility(View.VISIBLE);
                imgowl.setVisibility(View.GONE);
            }

            if (msfsc > 4) {
                chronotype = "Spättyp (\"Eule\")";
                imglark.setVisibility(View.GONE);
                imgowl.setVisibility(View.VISIBLE);
            }

            if (msfsc == 4) {
                chronotype = "Normaltyp";
            }

            queryDateValues.put("workdays_bedtime", btw.getTime());
            queryDateValues.put("workdays_readytosleeptime", sprepw.getTime());
            queryDateValues.put("workdays_uptime", sew.getTime());
            queryIntValues.put("workdays_minutestillsleep", slatw);
            queryIntValues.put("workdays_minutestillup", siw);
            queryBooleanValues.put("workdays_alarmclock", alarmw);
            queryFloatValues.put("sow", sow.floatValue());
            queryFloatValues.put("sdw", sdw.floatValue());
            queryFloatValues.put("msw", msw.floatValue());
        }

        int chronotype_id = Math.round(msfsc);

        //Calculation of weekly sleep loss
        Long slossweek;
        if (sdweek > sdw) {
            slossweek = (sdweek - sdw) * wd;
        } else {
            slossweek = (sdweek - sdf) * fd;
        }


        queryStringValues.put("comments", comments);


        queryDateValues.put("offdays_bedtime", btf.getTime());
        queryDateValues.put("offdays_readytosleeptime", sprepf.getTime());
        queryDateValues.put("offdays_uptime", sef.getTime());
        queryIntValues.put("offdays_minutestillsleep", slatf);
        queryIntValues.put("offdays_minutestillup", sif);
        queryBooleanValues.put("offdays_alarmclock", alarmf);
        queryIntValues.put("chronotype_id", chronotype_id);
        queryBooleanValues.put("uploaded", false);
        queryFloatValues.put("sof", sof.floatValue());
        queryFloatValues.put("sdf", sdf.floatValue());
        queryFloatValues.put("msf", msf.floatValue());
        queryFloatValues.put("msfsc", msfsc.floatValue());
        queryFloatValues.put("socialjetlag", sjl.floatValue());

        tvauswertung.setText("Nach Ihren Angaben sind Sie ein " + chronotype);
        tvcolldata.setText("chronotype_ID: " + chronotype_id
                + "\nEinschlafzeit an Arbeitstagen/freien Tagen: " + dateFormat.format(new Date(sow)) + "/" + dateFormat.format(new Date(sof))
                + "\nSchlafdauer an Arbeitstagen/freien Tagen: " + dateFormat.format(new Date(sdw)) + "/" + dateFormat.format(new Date(sdf))
                + "\nAufwachzeit an Arbeitstagen/freien Tagen: " + dateFormat.format(sew) + "/" + dateFormat.format(sef)
                + "\nSchlafmitte an Arbeitstagen/freien Tagen: " + dateFormat.format(new Date(msw)) + "/" + dateFormat.format(new Date(msf))
                + "\nKorrigierte Schlafmitte: " + dateFormat.format(new Date(msfsc))
                + "\nSocial Jetlag: " + dateFormat.format(new Date(sjl))
                + "\nAnzahl Arbeitstage/Woche: " + wd
                + "\nBettgehzeit an Arbeitstagen/freien Tagen: " + dateFormat.format(btw) + "/" + dateFormat.format(btf)
                + "\nBereit zum Einschlafen an Arbeitstagen/freien Tagen: " + dateFormat.format(sprepw) + "/" + dateFormat.format(sprepf)
                + "\nZeit um einzuschlafen an Arbeitstagen/freien Tagen: " + dateFormat.format(new Date(slatw)) + "/" + dateFormat.format(new Date(slatf))
                + "\nWecker an Arbeitstagen/freien Tagen: " + alarmw + "/" + alarmf
                + "\nZeit bis zum Aufstehen an Arbeitstagen/freien Tagen: " + dateFormat.format(new Date(siw)) + "/" + dateFormat.format(new Date(sif))
                + "\nWöchentlicher Schlafverlust: " + dateFormat.format(new Date(slossweek))
                + "\nDurchschnittliche wöchentliche Lichtexposition: " + dateFormat.format(new Date(leweek))
                + "\nGesamte Zeit im Bett an Arbeitstagen/freien Tagen: " + dateFormat.format(new Date(tbtw)) + "/" + dateFormat.format(new Date(tbtf))
                + "\nBemerkungen: " + comments);
    }

    public void saveOnline(View view) {
        //Neue DBController-Instanz erzeugen
        final DBController controller = new DBController(this);
        //Alle evtl. vorhandenen Daten löschen, da immer nur ein Datensatz gespeichert wird,
        controller.truncTable("mctq_data");
        //Daten in Tabelle schreiben und somit lokal speichern
        controller.insertData(queryStringValues, queryBooleanValues, queryDateValues, queryIntValues, queryFloatValues);
        //Upload der Daten, wenn Netzwerk verfübgar.
        if (Utility.isOnline(this)) {
            Utility.doUpload(String.valueOf(R.string.MCTQ_uploadstring), "mctqDataJSON", controller.composeJSONfromSQLite(), new Utility.OnMyHttpResponseCallback() {
                @Override
                public void onMyHttpResponse(boolean success, String response) {
                    if (success) {
                        Toast.makeText(getApplicationContext(), R.string.MCTQ_datauploadsuccess, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.MCTQ_datauploaderrordialog + response, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.MCTQ_offline, Toast.LENGTH_LONG).show();
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
