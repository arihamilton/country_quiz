package edu.uga.cs.countryquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is facilitates storing and restoring job leads stored.
 */
public class CountryData {

    public static final String DEBUG_TAG = "CountryData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase   db;
    private SQLiteOpenHelper countryDbHelper;
    private static final String[] allColumns = {
            CountryDBHelper.COUNTRYCONTINENTS_COLUMN_ID,
            CountryDBHelper.COUNTRYCONTINENTS_COLUMN_COUNTRY,
            CountryDBHelper.COUNTRYCONTINENTS_COLUMN_CONTINENT,
    };

    public CountryData( Context context ) {
        this.countryDbHelper = CountryDBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = countryDbHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "CountryData: db open" );
    }

    // Close the database
    public void close() {
        if( countryDbHelper != null ) {
            countryDbHelper.close();
            Log.d(DEBUG_TAG, "CountryData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<Country> retrieveAllCountries() {
        ArrayList<Country> countries = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( CountryDBHelper.TABLE_COUNTRYCONTINENTS, allColumns,
                        null, null, null, null, null );

            // collect all countries into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 3) {

                        // get all attribute values of this country
                        columnIndex = cursor.getColumnIndex( CountryDBHelper.COUNTRYCONTINENTS_COLUMN_ID );
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( CountryDBHelper.COUNTRYCONTINENTS_COLUMN_COUNTRY );
                        String countryName = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( CountryDBHelper.COUNTRYCONTINENTS_COLUMN_CONTINENT );
                        String continent = cursor.getString( columnIndex );

                        // create a new Country object and set its state to the retrieved values
                        Country country = new Country( countryName, continent );
                        country.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        countries.add( country );
                        Log.d(DEBUG_TAG, "Retrieved Country: " + country);
                    }
                }
            }
            if( cursor != null )
                Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
            else
                Log.d( DEBUG_TAG, "Number of records from DB: 0" );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved job leads
        return countries;
    }
}
