package fhbielefeld.glaubenssaetze_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by philpetschull on 05.03.18.
 * Hilfsklasse zum erzeugen und updaten der SQLite Datenbank
 * Vereinfacht die Arbeit mit der SQLite Datenbank
 */

public class GlaubenssaetzeMemoDbHelper extends SQLiteOpenHelper {
  private static final String LOG_TAG = GlaubenssaetzeMemoDbHelper.class.getSimpleName();

  //Membervariable of SQLite-Database
  SQLiteDatabase db;

  //Name of DB
  public static final String DB_NAME = "sentencelist.db";
  //DB Version
  public static final int DB_VERSION = 2;
  //Name of table
  public static final String TABLE_SENTENCE_LIST = "table_sentencelist_1";
  //Columnnames
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_SENTENCE= "sentence";
  public static final String COLUMN_STATUS = "status";
  public static final String COLUMN_PAIRNUMBER = "pairnumber";



  //String for creating the database
  public static final String SQL_CREATE=
          " CREATE TABLE " + TABLE_SENTENCE_LIST +
                  " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                  COLUMN_STATUS + " TEXT NOT NULL, " +
                  COLUMN_PAIRNUMBER + " INTEGER NOT NULL, " +
                  COLUMN_SENTENCE + " TEXT NOT NULL); ";



  //Creating the database
  public GlaubenssaetzeMemoDbHelper(Context context) {
    super(context,DB_NAME,null,DB_VERSION);
    Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    db = getWritableDatabase();
    onCreate(db);
    Log.d(LOG_TAG,"onCreate wurde gecallt");
  }

  //Funktion zum erstellen der SQLite Datenbank
  //Wird nur aufgerufen, wenn die Datenbank bereits existiert
  @Override
  public void onCreate(SQLiteDatabase db) {

    Log.d(LOG_TAG, "Wir befinden uns in der onCreate Funktion");
    try {

      Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
      db.execSQL(SQL_CREATE);
    }
    catch (Exception ex) {
      Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
    }

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(LOG_TAG, "Die Tabelle wird geupdatet");
    db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE);
    onCreate(db);
  }
}