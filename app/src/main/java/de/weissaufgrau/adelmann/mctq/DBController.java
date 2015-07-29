package de.weissaufgrau.adelmann.mctq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by adelmann on 24.02.2015.
 */
public class DBController extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "mctq.db", null, 1);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE mctq_data  (" +
                " ID BIGINT unsigned NOT NULL," +
                " workdays_bedtime BIGINT NOT NULL," +
                " workdays_readytosleeptime BIGINT NOT NULL," +
                " workdays_minutestillsleep INT(2) NOT NULL," +
                " workdays_uptime BIGINT NOT NULL," +
                " workdays_minutestillup INT(2) NOT NULL," +
                " offdays_bedtime BIGINT NOT NULL," +
                " offdays_readytosleeptime BIGINT NOT NULL," +
                " offdays_minutestillsleep INT(2) NOT NULL," +
                " offdays_uptime BIGINT NOT NULL," +
                " offdays_minutestillup INT(2) NOT NULL," +
                " sow FLOAT NOT NULL," +
                " sdw FLOAT NOT NULL," +
                " msw FLOAT NOT NULL," +
                " sof FLOAT NOT NULL," +
                " sdf FLOAT NOT NULL," +
                " msf FLOAT NOT NULL," +
                " msfsc FLOAT NOT NULL," +
                " offdays_alarmclock TINYINT(1) NOT NULL," +
                " workdays_alarmclock TINYINT(1) NOT NULL," +
                " socialjetlag FLOAT NOT NULL," +
                " chronotype_id INT(2) NOT NULL," +
                " comments TEXT NOT NULL," +
                " uploaded TINYINT(1) NOT NULL" +
                ")";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS mctq_data";
        database.execSQL(query);
        onCreate(database);
    }

    /**
     * Inserts values into MCTQ-Database
     *
     * @param queryStringValues
     * @param queryBooleanValues
     * @param queryDateValues
     * @param queryIntValues
     * @param queryFloatValues
     */
    public void insertData(HashMap<String, String> queryStringValues, HashMap<String, Boolean> queryBooleanValues, HashMap<String, Long> queryDateValues, HashMap<String, Integer> queryIntValues, HashMap<String, Float> queryFloatValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", new Date().getTime());
        values.put("workdays_bedtime", queryDateValues.get("workdays_bedtime"));
        values.put("workdays_readytosleeptime", queryDateValues.get("workdays_readytosleeptime"));
        values.put("workdays_minutestillsleep", queryIntValues.get("workdays_minutestillsleep"));
        values.put("workdays_uptime", queryDateValues.get("workdays_uptime"));
        values.put("workdays_minutestillup", queryIntValues.get("workdays_minutestillup"));
        values.put("offdays_bedtime", queryDateValues.get("offdays_bedtime"));
        values.put("offdays_readytosleeptime", queryDateValues.get("offdays_readytosleeptime"));
        values.put("offdays_minutestillsleep", queryIntValues.get("offdays_minutestillsleep"));
        values.put("offdays_uptime", queryDateValues.get("offdays_uptime"));
        values.put("offdays_minutestillup", queryIntValues.get("offdays_minutestillup"));
        values.put("sow", queryFloatValues.get("sow"));
        values.put("sdw", queryFloatValues.get("sdw"));
        values.put("msw", queryFloatValues.get("msw"));
        values.put("sof", queryFloatValues.get("sof"));
        values.put("sdf", queryFloatValues.get("sdf"));
        values.put("msf", queryFloatValues.get("msf"));
        values.put("msfsc", queryFloatValues.get("msfsc"));
        values.put("offdays_alarmclock", queryBooleanValues.get("offdays_alarmclock"));
        values.put("workdays_alarmclock", queryBooleanValues.get("workdays_alarmclock"));
        values.put("socialjetlag", queryFloatValues.get("socialjetlag"));
        values.put("chronotype_id", queryIntValues.get("chronotype_id"));
        values.put("comments", queryStringValues.get("comments"));
        values.put("uploaded", queryBooleanValues.get("uploaded"));

        long insertok = database.insert("mctq_data", null, values);
        database.close();

        /* DEBUG
        if (insertok > 0) {
            System.out.println("Lokaler INSERT erfolgreich!");
        } else {
            System.out.println("Lokaler INSERT fehlgeschlagen!");
        }*/
    }

    /**
     * Löscht alle Daten aus einer Tabelle
     *
     * @param tablename
     */
    public void truncTable(String tablename) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + tablename);
    }

    /**
     * Get list of Users from SQLite DB as Array List
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM users";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }

    /**
     * Compose JSON out of SQLite records
     *
     * @return
     */

    public String composeJSONfromSQLite() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM mctq_data where uploaded=0";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                /*map.put("ID", String.valueOf(cursor.getLong(0)));*/
                map.put("workdays_bedtime", String.valueOf(cursor.getLong(1)));
                map.put("workdays_readytosleeptime", String.valueOf(cursor.getLong(2)));
                map.put("workdays_minutestillsleep", String.valueOf(cursor.getInt(3)));
                map.put("workdays_uptime", String.valueOf(cursor.getLong(4)));
                map.put("workdays_minutestillup", String.valueOf(cursor.getInt(5)));
                map.put("offdays_bedtime", String.valueOf(cursor.getLong(6)));
                map.put("offdays_readytosleeptime", String.valueOf(cursor.getLong(7)));
                map.put("offdays_minutestillsleep", String.valueOf(cursor.getInt(8)));
                map.put("offdays_uptime", String.valueOf(cursor.getLong(9)));
                map.put("offdays_minutestillup", String.valueOf(cursor.getInt(10)));
                map.put("sow", String.valueOf(cursor.getFloat(11)));
                map.put("sdw", String.valueOf(cursor.getFloat(12)));
                map.put("msw", String.valueOf(cursor.getFloat(13)));
                map.put("sof", String.valueOf(cursor.getFloat(14)));
                map.put("sdf", String.valueOf(cursor.getFloat(15)));
                map.put("msf", String.valueOf(cursor.getFloat(16)));
                map.put("msfsc", String.valueOf(cursor.getFloat(17)));
                map.put("offdays_alarmclock", String.valueOf(cursor.getInt(18)));
                map.put("workdays_alarmclock", String.valueOf(cursor.getInt(19)));
                map.put("socialjetlag", String.valueOf(cursor.getFloat(20)));
                map.put("chronotype_id", String.valueOf(cursor.getInt(21)));
                map.put("comments", cursor.getString(22));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    /**
     * Get Sync status of SQLite
     *
     * @return
     */
    public String getSyncStatus() {
        String msg = null;
        if (this.dbSyncCount() == 0) {
            msg = "SQLite and Remote MySQL DB are in Sync!";
        } else {
            msg = "DB Sync needed";
        }
        return msg;
    }

    /**
     * Get SQLite records that are yet to be Synced
     *
     * @return
     */
    public int dbSyncCount() {
        int count = 0;
        String selectQuery = "SELECT  * FROM mctq_data where uploaded = 0";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }

    /**
     * Update Sync status
     */
    public void updateSyncStatus() {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE mctq_data SET uploaded = 1 where uploaded=0";
        Log.d("query", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

}