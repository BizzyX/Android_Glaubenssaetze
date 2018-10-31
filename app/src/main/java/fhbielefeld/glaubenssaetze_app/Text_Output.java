package fhbielefeld.glaubenssaetze_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


        //Neues Objekt erzeugt
        dataSource = new GlaubenssatzeMemoDataSource(this);
        dataSource.open();
        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();

        activateSaveButton();


    }



    private void activateSaveButton() {
        //Button to save the user input
        Button savetxt = (Button) findViewById(R.id.buttonsave);
        //Button to save the user input
        final EditText edittxt = (EditText) findViewById(R.id.entertext);

        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentence = edittxt.getText().toString();


                //Shared Prefs Datei öffnen
                SharedPreferences mySPR = getSharedPreferences("MySPFILE" , 0);

                int count = mySPR.getInt("Counter", 100);


                dataSource.createGlaubenssaetzeMemo(" n ", count + 1, sentence);


                mySPR.edit().putInt("Counter" , count+1).commit();

                //If the user input is empty
                if (TextUtils.isEmpty(sentence)) {
                    edittxt.setError(getString(R.string.editText_error_message));
                    return;
                }
                //EditText is set back to zero
                edittxt.setText("");
                Log.d(LOG_TAG, "Der Satz: " + sentence + " wurde der Datenbank hinzugefügt!");
                dataSource.getAllGlaubenssaetzeMemos();

                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

                Intent sentence_save = new Intent(Text_Output.this,Text_Output_Positiv.class);
                startActivity(sentence_save);
            }
        });

    }


}
