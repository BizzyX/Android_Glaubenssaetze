package fhbielefeld.glaubenssaetze_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import java.util.List;


public class Text_Output extends AppCompatActivity {

    //LOG-Konstanten
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    // Membervariable dataSource vom Typ GlaubenssatzMemoDataSource
    private GlaubenssatzeMemoDataSource dataSource;

    //Navigation Bar Variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_output);


        //--- Neues Objekt erzeugt ---
        dataSource = new GlaubenssatzeMemoDataSource(this);
        dataSource.open();
        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();




        //------------- NavigationBar ---------------------
        final NavigationView nav_view_generate = (NavigationView)findViewById(R.id.nav_menu_input);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nav_view_generate.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Menüeintrag zum generieren
                if (id == R.id.menu)
                {
                    Intent generate = new Intent(Text_Output.this, MainActivity.class);
                    startActivity(generate);
                    return false;
                }

                //Menüeintrag zum eingeben
                if (id == R.id.menu_generate)
                {
                    Intent input = new Intent(Text_Output.this, Text_Generate_Activity.class);
                    startActivity(input);
                    return false;
                }

                //Menüeintrag zur Satzliste
                if (id == R.id.menu_list)
                {
                    Intent list = new Intent(Text_Output.this, Sentence_List.class);
                    startActivity(list);
                    return false;
                }

                //Menüeintrag zur Hilfeseite
                if (id == R.id.menu_help)
                {
                    Intent help = new Intent(Text_Output.this, Help.class);
                    startActivity(help);
                    return false;
                }
                return false;
            }
        });


        //------------- NavigationBar ---------------------

        //--- Funktion activateSaveButton()
        //Daten in die Datenbank schreiben und zur nächsten Activity
        activateSaveButton();


    }



    private void activateSaveButton() {
        //Button um den eingegebenen Satz zu speichern
        final Button savetxt = (Button) findViewById(R.id.buttonsave);
        //Variable für den eingegebenen Satz
        final EditText edittxt = (EditText) findViewById(R.id.entertext);
        //Variable für die Überschrift
        final TextView headline = (TextView) findViewById(R.id.textView3);


        //--- VideoView - Nuss ---
        //Videosequenz
        //which Videoview is used?
        final VideoView videoview = (VideoView) findViewById(R.id.playvideotextinput);

        //Which video should be played out of the raw folder?
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.explode);
        videoview.setVideoURI(uri);

        //Make the videoview box invisible
        videoview.setVisibility(View.INVISIBLE);



        //Button "Speichern" wird geklickt
        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Eingetippte Satz wird in einer String Variablen gespeichert
                String sentence = edittxt.getText().toString();

                //Wenn die Eingabe leer ist wird eine Fehlermeldung angezeigt
                if (TextUtils.isEmpty(sentence)) {
                    edittxt.setError(getString(R.string.editText_error_message));
                    return;
                }

                savetxt.setVisibility(View.INVISIBLE);
                edittxt.setVisibility(View.INVISIBLE);
                headline.setVisibility(View.INVISIBLE);


                //Shared Prefs Datei öffnen
                SharedPreferences mySPR = getSharedPreferences("MySPFILE" , 0);

                int count = mySPR.getInt("Counter", 100);

                //Satz wird in die Datenbank geschrieben
                dataSource.createGlaubenssaetzeMemo(" n ", count + 1, sentence);


                mySPR.edit().putInt("Counter" , count+1).commit();



                //Der eingegebene Text wird zurück auf null gesetzt
                edittxt.setText("");
                Log.d(LOG_TAG, "Der Satz: " + sentence + " wurde der Datenbank hinzugefügt!");
                dataSource.getAllGlaubenssaetzeMemos();

                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }


                //Make the videoviewbox visible
                videoview.setVisibility(View.VISIBLE);
                //Video wird abgespielt
                videoview.start();



                //go to the next activity when the video is finished
                videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Intent sentence_save = new Intent(Text_Output.this,Text_Output_Positiv.class);
                        startActivity(sentence_save);
                    }
                });


            }
        });

    }


    //------------- NavigationBar ---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.navigation_menu_input, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Welcher Menüeintrag wurde geklickt
        int id = item.getItemId();


        if (mToggle.onOptionsItemSelected(item))
        {

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    //------------- NavigationBar ---------------------


}
