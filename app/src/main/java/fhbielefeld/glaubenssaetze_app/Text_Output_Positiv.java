package fhbielefeld.glaubenssaetze_app;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class Text_Output_Positiv extends AppCompatActivity {



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
        setContentView(R.layout.activity_text__output__positiv);

        //------------- NavigationBar ---------------------
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_output_positiv);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();


                //Menüeintrag zum generieren
                if (id == R.id.menu_main)
                {
                    Intent generate = new Intent(Text_Output_Positiv.this, MainActivity.class);
                    startActivity(generate);
                    return false;
                }

                //Menüeintrag zum eingeben
                if (id == R.id.menu_input)
                {
                    Intent input = new Intent(Text_Output_Positiv.this, Text_Output.class);
                    startActivity(input);
                    return false;
                }

                //Menüeintrag zur Satzliste
                if (id == R.id.menu_list)
                {
                    Intent list = new Intent(Text_Output_Positiv.this, Sentence_List.class);
                    startActivity(list);
                    return false;
                }

                //Menüeintrag zur Hilfeseite
                if (id == R.id.menu_help)
                {
                    Intent help = new Intent(Text_Output_Positiv.this, Help.class);
                    startActivity(help);
                    return false;
                }

                return false;
            }
        });

        //Neues Objekt erzeugt
        dataSource = new GlaubenssatzeMemoDataSource(this);
        dataSource.open();
        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();

        activateSaveButton();
    }

    //------------- NavigationBar ---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
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

    private void activateSaveButton() {
        //Button to save the user input
        Button savetxt = (Button) findViewById(R.id.buttonsave2);
        //Buton to save and type in another sentence
        Button anothertxt = (Button) findViewById(R.id.anothersentence);

        //Type in a sentence
        final EditText edittxt = (EditText) findViewById(R.id.entertext);

        //save the sentence from the edittxt and return to the input page to type in another sentence
        anothertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v)
            {
                String sentence = edittxt.getText().toString();


                //Shared Prefs Datei öffnen
                SharedPreferences mySPR = getSharedPreferences("MySPFILE" , 0);

                int count = mySPR.getInt("Counter", 100);


                dataSource.createGlaubenssaetzeMemo(" p ", count , sentence);

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
                Intent sentence_save = new Intent(Text_Output_Positiv.this,Text_Output_Positiv.class);
                startActivity(sentence_save);

            }

        });

        //save the sentence from the edittxt
        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentence = edittxt.getText().toString();


                //Shared Prefs Datei öffnen
                SharedPreferences mySPR = getSharedPreferences("MySPFILE" , 0);

                int count = mySPR.getInt("Counter", 100);


                dataSource.createGlaubenssaetzeMemo(" p ", count , sentence);


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


                Intent sentence_save = new Intent(Text_Output_Positiv.this,MainActivity.class);
                startActivity(sentence_save);

            }
        });


    }


}

