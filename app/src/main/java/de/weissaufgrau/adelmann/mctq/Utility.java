package de.weissaufgrau.adelmann.mctq;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by adelmann on 21.04.2015.
 * Hosts all the utility-classes for the Chronotype-App
 */
public class Utility {
    private static Calendar cal = Calendar.getInstance();
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.GERMANY);

    /**
     * Prüfen, ob Netzwerk vorhanden und verbunden ist
     *
     * @return false, wenn keine Netzerkverbindung oder nicht online
     */
    public static boolean isOnline(Context mContext) {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Führt einen Upload via HTTP-POST an die angegebene URL mit den angegebenen Daten durch, gibt eine Antwort vom Server zurück.
     *
     * @param url           The URL to open
     * @param parameterName The Name of the Parameter to use for sending data to the server
     * @param data          The data sent to the Server
     * @param handler       The responsehandler for the answer from the server
     */
    public static void doUpload(final String url, String parameterName, String data, AsyncHttpResponseHandler handler) {
        //Create SyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //Create requestparameters
        RequestParams params = new RequestParams();
        //fill params
        params.put(parameterName, data);

        //do POST
        client.post(url, params, handler);
    }

    /**
     * Wandelt ein übergebenes Date-Objekt zu einer Zeitangabe im 24-Stunden-Format
     *
     * @param date The Date to convert
     * @return String der Form HH:mm oder leerer String, wenn date=null
     */

    public static String timeToString24h(Date date) {
        String result = "";

        if (date != null) {
            result = timeFormat.format(date);
            return result;
        }

        return result;
    }

    /**
     * Wandelt eine als String übergebene Zeitangabe in der Form 00:00 in ein Date-Objekt im 24-Stunden-Format
     *
     * @param time The time as a String to convert
     * @return Entsprechendes Date-Objekt oder null, wenn time zu einer Parse-Exception führt.
     */
    public static Date timestringToDate24h(String time) {
        Date result;
        try {
            result = timeFormat.parse(time);
        } catch (ParseException e) {
            result = null;
        }
        return result;
    }

    /**
     * Gibt die jetzige Zeit plus 10 Minuten zurück
     *
     * @return Returns the time plus 10 minutes as a {@code Date} object.
     */
    public static final Date getNowPlus10m() {
        cal.setTime(getNow());
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime();
    }

    /**
     * Gibt die jetzige Zeit plus 8 Stunden zurück
     *
     * @return Returns the time plus 8 hours as a {@code Date} object.
     */
    public static final Date getGetNowPlus8h() {
        cal.setTime(getNow());
        cal.add(Calendar.HOUR, 8);
        return cal.getTime();
    }

    /**
     * Gibt die jetzige Zeit zurück
     *
     * @return Returns the time as a {@code Date} object.
     */
    public static final Date getNow() {
        return cal.getTime();
    }
}