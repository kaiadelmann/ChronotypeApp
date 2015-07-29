package de.weissaufgrau.adelmann.mctq;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMCTQ6Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_mctq6);

        String link = "Icons made by <a href=\"http://www.freepik.com\" title=\"Freepik\">Freepik</a> from <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\">CC BY 3.0</a>";
        String link2 = "Icon made by <a href=\"http://www.freepik.com\" title=\"Freepik\">Freepik</a> from <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a> is licensed under <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\">CC BY 3.0</a>";
        String link3 = "Owl and lark pictures are taken from <a href=\"http://openclipart.org\" title=\"openclipart.org\">openclipart.org</a>";

        TextView tv1 = (TextView) findViewById(R.id.link1);
        tv1.setText(link);
        Linkify.addLinks(tv1, Linkify.ALL);
        TextView tv2 = (TextView) findViewById(R.id.link2);
        tv2.setText(link2);
        Linkify.addLinks(tv2, Linkify.ALL);
        TextView tv3 = (TextView) findViewById(R.id.link3);
        tv3.setText(link3);
        Linkify.addLinks(tv3, Linkify.ALL);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_mctq6, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
