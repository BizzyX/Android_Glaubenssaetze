package fhbielefeld.glaubenssaetze_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Random;

public class Text_Generate_Activity_Answer extends AppCompatActivity {


    // Membervariable dataSource vom Typ GlaubenssatzMemoDataSource
    private GlaubenssatzeMemoDataSource dataSource;

    GlaubenssaetzeMemo glaubenssaetzeMemo;

    //Membervariable der Class "Text_Generate_Activity"
    Text_Generate_Activity textgenerateactivity;

    //membervariable of SQLiteDatabse
    SQLiteDatabase db;

    //LOG-Konstanten
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Navigation Bar Variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_generate_answer);

        TextView matchingTextPositiv = (TextView) findViewById(R.id.randomText_positiv);

        dataSource = new GlaubenssatzeMemoDataSource(this);



        final Button menu       = (Button) findViewById(R.id.menu);
        final Button weiter     = (Button) findViewById(R.id.weiter);


        //------------- NavigationBar ---------------------
        final NavigationView nav_view_generate = (NavigationView)findViewById(R.id.nav_menu_generate);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //generate a sentece out of the database
        Random randomGenerator = new Random();

        Bundle extras = getIntent().getExtras();
        long Pairnumber = 1;
        if (extras != null) {
            Pairnumber = extras.getLong(" Pairnumber ");
        }


        nav_view_generate.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Menüeintrag zum generieren
                if (id == R.id.menu_main)
                {
                    Intent generate = new Intent(Text_Generate_Activity_Answer.this, MainActivity.class);
                    startActivity(generate);
                    return false;
                }

                //Menüeintrag zum eingeben
                if (id == R.id.menu_input)
                {
                    Intent input = new Intent(Text_Generate_Activity_Answer.this, Text_Output.class);
                    startActivity(input);
                    return false;
                }

                //Menüeintrag zur Satzliste
                if (id == R.id.menu_list)
                {
                    Intent list = new Intent(Text_Generate_Activity_Answer.this, Sentence_List.class);
                    startActivity(list);
                    return false;
                }

                //Menüeintrag zur Hilfeseite
                if (id == R.id.menu_help)
                {
                    Intent help = new Intent(Text_Generate_Activity_Answer.this, Help.class);
                    startActivity(help);
                    return false;
                }
                return false;
            }
        });

        dataSource.open();

        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getPostiveSentences(Pairnumber);

        int index = randomGenerator.nextInt(glaubenssaetzeMemoList.size());
        GlaubenssaetzeMemo item = glaubenssaetzeMemoList.get(index);
        String randomObject = item.getSentence().toString();
        matchingTextPositiv.setText(randomObject);

        dataSource.close();


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Text_Generate_Activity_Answer.this, MainActivity.class);
                startActivity(intent);
            }
        });

        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Text_Generate_Activity_Answer.this, Text_Generate_Activity.class);
                startActivity(intent);
            }
        });


    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showAllPositiveEntries(long Pairnumber) {

        long currentPairNumber = Pairnumber;
        Log.d(LOG_TAG, " Funktion : showAllPositiveEntries Die erhaltene Paarnummer ist: " + currentPairNumber );

        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getPostiveSentences( currentPairNumber );

        ArrayAdapter<GlaubenssaetzeMemo> glaubenssaetzeMemoArrayAdapter = new ArrayAdapter<GlaubenssaetzeMemo>(
                this,
                android.R.layout.simple_list_item_1,
                glaubenssaetzeMemoList);


        ListView sentenceList = (ListView) findViewById(R.id.sentencelist__positiv);
        sentenceList.setAdapter(glaubenssaetzeMemoArrayAdapter);
        dataSource.close();

    }


}