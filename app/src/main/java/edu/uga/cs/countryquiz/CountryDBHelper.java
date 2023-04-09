package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * This is a SQLiteOpenHelper class, which Android uses to create, upgrade, delete an SQLite database
 * in an app.
 *
 * This class is a singleton, following the Singleton Design Pattern.
 * Only one instance of this class will exist.  To make sure, the
 * only constructor is private.
 * Access to the only instance is via the getInstance method.
 */
public class CountryDBHelper extends SQLiteOpenHelper {

    Context context;
    private static final String DEBUG_TAG = "CountryDBHelper";

    private static final String DB_NAME = "countrycontinents.db";
    private static final int DB_VERSION = 1;

    // Define all names (strings) for table and column names.
    // This will be useful if we want to change these names later.
    public static final String TABLE_COUNTRYCONTINENTS = "countrycontinents";
    public static final String COUNTRYCONTINENTS_COLUMN_ID = "_id";
    public static final String COUNTRYCONTINENTS_COLUMN_COUNTRY = "country";
    public static final String COUNTRYCONTINENTS_COLUMN_CONTINENT = "continent";

    // This is a reference to the only instance for the helper.
    private static CountryDBHelper helperInstance;

    // A Create table SQL statement to create a table for job leads.
    // Note that _id is an auto increment primary key, i.e. the database will
    // automatically generate unique id values as keys.
    private static final String CREATE_COUNTRYCONTINENTS =
            "create table " + TABLE_COUNTRYCONTINENTS + " ("
                + COUNTRYCONTINENTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COUNTRYCONTINENTS_COLUMN_COUNTRY + " TEXT, "
                + COUNTRYCONTINENTS_COLUMN_CONTINENT + " TEXT"
            + ")";

    // Note that the constructor is private!
    // So, it can be called only from
    // this class, in the getInstance method.
    private CountryDBHelper(Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
        this.context = context;
    }

    // Access method to the single instance of the class.
    // It is synchronized, so that only one thread can executes this method, at a time.
    public static synchronized CountryDBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new CountryDBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    public void populateDatabase( SQLiteDatabase db ) {

        try {

            ContentValues values = new ContentValues();

            // Open the CSV data file in the assets folder
            InputStream in_s = context.getAssets().open("country_continent.csv");

            // read the CSV data
            CSVReader reader = new CSVReader(new InputStreamReader(in_s));
            String[] nextRow;
            while ((nextRow = reader.readNext()) != null) {

                // nextRow[] is an array of values from the line

                for (int i = 0; i < nextRow.length; i++) {

                    values = new ContentValues();

                    values.put(CountryDBHelper.COUNTRYCONTINENTS_COLUMN_COUNTRY, nextRow[i]);
                    i++;
                    values.put(CountryDBHelper.COUNTRYCONTINENTS_COLUMN_CONTINENT, nextRow[i]);

//                  Log.d( "CountryDBHelper", "populateDatabase: " + nextRow[i] );

                    db.insert( CountryDBHelper.TABLE_COUNTRYCONTINENTS, null, values );
                }
            }
            in_s.close();
        }
        catch (Exception e) {
            Log.e( "CountryDBHelper", e.toString() );
        }
    }



    // We must override onCreate method, which will be used to create the database if
    // it does not exist yet.
    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( CREATE_COUNTRYCONTINENTS );
        Log.d( DEBUG_TAG, "Table " + TABLE_COUNTRYCONTINENTS + " created" );
        populateDatabase( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_COUNTRYCONTINENTS + " populated" );
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( "drop table if exists " + TABLE_COUNTRYCONTINENTS );
        onCreate( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_COUNTRYCONTINENTS + " upgraded" );
    }
}
