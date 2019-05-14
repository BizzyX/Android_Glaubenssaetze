package fhbielefeld.glaubenssaetze_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_output);


        //--- Neues Objekt erzeugt ---
        dataSource = new GlaubenssatzeMemoDataSource(this);
        dataSource.open();
        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();

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

                savetxt.setVisibility(View.INVISIBLE);
                edittxt.setVisibility(View.INVISIBLE);


                //Shared Prefs Datei öffnen
                SharedPreferences mySPR = getSharedPreferences("MySPFILE" , 0);

                int count = mySPR.getInt("Counter", 100);

                //Satz wird in die Datenbank geschrieben
                dataSource.createGlaubenssaetzeMemo(" n ", count + 1, sentence);


                mySPR.edit().putInt("Counter" , count+1).commit();

                //Wenn die Eingabe leer ist wird eine Fehlermeldung angezeigt
                if (TextUtils.isEmpty(sentence)) {
                    edittxt.setError(getString(R.string.editText_error_message));
                    return;
                }

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


}
