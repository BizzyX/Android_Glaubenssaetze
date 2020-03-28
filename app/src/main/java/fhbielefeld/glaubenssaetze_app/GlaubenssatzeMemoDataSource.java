package fhbielefeld.glaubenssaetze_app;

/**
 * Created by philpetschull on 05.03.18.
 * Arbeiterklasse
 * Fuer alle Datenbankzugriffe verantwortlich
 * Schreiben,lesen und loeschen mit dieser Klasse
 *
 * Datenbank erstellen und Verbindung aufrecht erhalten mit
 * Membervariable vom Typ GlaubenssaetzeMemoDbHelper
 *
 * Membervariable vom Typ SQLiteDatabase zum abspeichern in der Datenbank
 *
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GlaubenssatzeMemoDataSource {

    private static final String LOG_TAG = GlaubenssatzeMemoDataSource.class.getSimpleName();

    // Membervariable zum abspeichern in der Datenbank
    private SQLiteDatabase database;

    //Membervariable zum erstellen und Aufrechthalten der Verbindung zur DB
    private GlaubenssaetzeMemoDbHelper dbHelper;

    private String[] columns = {

            //Negative Saetze
            GlaubenssaetzeMemoDbHelper.COLUMN_ID,
            GlaubenssaetzeMemoDbHelper.COLUMN_SENTENCE,
            GlaubenssaetzeMemoDbHelper.COLUMN_PAIRNUMBER,
            GlaubenssaetzeMemoDbHelper.COLUMN_STATUS

    };

    //WhereClause
    public static final String[] WhereClausenegative = new String[] {" status "};

    //WhereArgs
    public static final String[] whereArgsnegative = new String[] { " n "};



    public GlaubenssatzeMemoDataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new GlaubenssaetzeMemoDbHelper(context);
    }


    //open the database
    public void open(){
        //close for troubleshooting.it might work without this
        dbHelper.close();
        //Log-Message
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        //Referenz zur Datenbank wird erfragt. Entweder wird Referenz zurueck geliefert oder eine neue angelegt
        database = dbHelper.getWritableDatabase();
        //Log-Message
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());

    }



    //close the database
    public void close(){
        dbHelper.close();
        //Log-Message
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");

    }



    //create a new entry in the database
    public GlaubenssaetzeMemo createGlaubenssaetzeMemo(String status,long pairnumber, String sentence) {
        ContentValues values = new ContentValues();
        values.put(GlaubenssaetzeMemoDbHelper.COLUMN_STATUS,status);
        values.put(GlaubenssaetzeMemoDbHelper.COLUMN_PAIRNUMBER,pairnumber);
        values.put(GlaubenssaetzeMemoDbHelper.COLUMN_SENTENCE, sentence);


        long insertId = database.insert(GlaubenssaetzeMemoDbHelper.TABLE_SENTENCE_LIST, null, values);

        Cursor cursor = database.query(GlaubenssaetzeMemoDbHelper.TABLE_SENTENCE_LIST,
                columns, GlaubenssaetzeMemoDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        GlaubenssaetzeMemo glaubenssaetzeMemo = cursorToGlaubenssaetzeMemo(cursor);
        cursor.close();

        return glaubenssaetzeMemo;
    }

    //delete a entry in the database at the id position
    public void deleteGlaubenssaetzeMemo(GlaubenssaetzeMemo glaubenssaetzeMemo){
        long id = glaubenssaetzeMemo.getId();

        database.delete(GlaubenssaetzeMemoDbHelper.TABLE_SENTENCE_LIST,
                        GlaubenssaetzeMemoDbHelper.COLUMN_ID + "=" + id,
                        null);

        Log.d(LOG_TAG , "Eintrag gelöscht! ID: " + id +  " Inhalt: " + glaubenssaetzeMemo.toString());
    }//end of delete function

    //update an existing entry in the database
    public GlaubenssaetzeMemo updateGlaubenssatzeMemo(long id, String newSentence){
        ContentValues values = new ContentValues();
        values.put(GlaubenssaetzeMemoDbHelper.COLUMN_SENTENCE,newSentence);

        database.update(GlaubenssaetzeMemoDbHelper.TABLE_SENTENCE_LIST,
                 values , GlaubenssaetzeMemoDbHelper.COLUMN_ID  + "=" + id,
                null );

        Cursor cursor = database.query(GlaubenssaetzeMemoDbHelper.TABLE_SENTENCE_LIST,
                columns, GlaubenssaetzeMemoDbHelper.COLUMN_ID + "=" + id,
                null, null,null,null);

        cursor.moveToFirst();
        GlaubenssaetzeMemo glaubenssaetzeMemo = cursorToGlaubenssaetzeMemo(cursor);
        cursor.close();

        return glaubenssaetzeMemo;

    }//end of update function

    //go to the current position in the database
    private GlaubenssaetzeMemo cursorToGlaubenssaetzeMemo(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(GlaubenssaetzeMemoDbHelper.COLUMN_ID);
        int idSentence = cursor.getColumnIndex(GlaubenssaetzeMemoDbHelper.COLUMN_SENTENCE);
        int idStatus = cursor.getColumnIndex(GlaubenssaetzeMemoDbHelper.COLUMN_STATUS);
        int idPairnumber = cursor.getColumnIndex(GlaubenssaetzeMemoDbHelper.COLUMN_PAIRNUMBER);


        long id = cursor.getLong(idIndex);
        String status = cursor.getString(idStatus);
        long pairnumber = cursor.getLong(idPairnumber);
        String sentence = cursor.getString(idSentence);

        GlaubenssaetzeMemo glaubenssaetzeMemo = new GlaubenssaetzeMemo(id,status,pairnumber, sentence);

        return glaubenssaetzeMemo;
    }



    //get a list with all negative entries of the "glaubenssaetzememodatabase"
    public List<GlaubenssaetzeMemo> getAllGlaubenssaetzeMemos() {
        //New Array List glaubenssaetzeMemoList
        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = new ArrayList<>();

        //Check if the status is "n" for negative
        String whereClause = " status =  ?";
        String[] whereArgs = new String[] {" n "};
        //Set the cursor to the database table 'TABLE_SENTENCE_LIST where whereClause => status = n
        Cursor cursor = database.query(GlaubenssaetzeMemoDbHelper.TABLE_SENTENCE_LIST , columns , whereClause , whereArgs, null , null , null);

        //Set the cursor to the first position
        cursor.moveToFirst();
        GlaubenssaetzeMemo glaubenssaetzeMemo;
        //While cursor is not at the end - add the item to the list
        while(!cursor.isAfterLast()) {
            glaubenssaetzeMemo = cursorToGlaubenssaetzeMemo(cursor);
            glaubenssaetzeMemoList.add(glaubenssaetzeMemo);
            Log.d(LOG_TAG, "ID: " + glaubenssaetzeMemo.getId() + ", Inhalt: " + glaubenssaetzeMemo.toString() + ", Paarnummer: " + glaubenssaetzeMemo.getPairnumber());
            cursor.moveToNext();
        }
        //close the cursor
        cursor.close();
        //return the list
        return glaubenssaetzeMemoList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<GlaubenssaetzeMemo> getPostiveSentences( long Pairnumber) {
        //pairnumber of the related negative sentence
        List<GlaubenssaetzeMemo> positiveMemoList = new ArrayList<>();

        //convert the pairnumber into a string
        long negativePairNumber = Pairnumber;
        String PairNumberString = Objects.toString( negativePairNumber, null);
        //Bedingunen fuer die Datenbankabfrage: Status = Positiv , Paarnummer = die die uebergeben wurde

        //status has to be " p " and the pairnumber has to be the one relating to the negative sentence
        String whereClause = " status = ? AND pairnumber  = ? ";
        String[] whereArgs = new String[] {" p " , PairNumberString };

        //database query with where conditions
        Cursor cursor = database.query(GlaubenssaetzeMemoDbHelper.TABLE_SENTENCE_LIST, columns , whereClause , whereArgs , null , null , null );

        cursor.moveToFirst();

        GlaubenssaetzeMemo glaubenssaetzeMemo;

        while(!cursor.isAfterLast()) {
            glaubenssaetzeMemo = cursorToGlaubenssaetzeMemo(cursor);
            positiveMemoList.add(glaubenssaetzeMemo);
            Log.d(LOG_TAG, "ID: " + glaubenssaetzeMemo.getId() + ", Inhalt: " + glaubenssaetzeMemo.toString() + ", Paarnummer: " + glaubenssaetzeMemo.getPairnumber());
            cursor.moveToNext();
        }
        cursor.close();
        //deliver the list with matching positive sentences
        return positiveMemoList;
    }





}
