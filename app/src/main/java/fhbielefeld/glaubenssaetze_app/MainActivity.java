package fhbielefeld.glaubenssaetze_app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Button;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;





/*
    GlaubenssaetzeDbHelper – Hilfsklasse.Eigenschaften der Datenbank und Tabelle definiert und die Tabelle mit einem SQL-Kommando erstellt.
    GlaubensatzeDataSource – Datenquelle - hält die Verbindung zur Datenbank aufrecht. Hier haben wir eine Referenz zu dem Datenbankobjekt angefragt und damit den Erstellungsprozess der Tabelle gestartet.
    MainActivity – Mit unserer Hauptklasse steuern wir die Datenbankzugriffe. In ihr haben wir unsere Datenquelle geöffnet und wieder geschlossen.
 */

// TODO : AUFRÄUMEN!
// TODO : Icons auf die Buttons -> Buttons schöner machen
// TODO : Weiteren Seiten aufräumen und schöner gestalten
// TODO:  Credit Nut Icon Source:Cartoon Nut Clip Art from ca.clipartlogo.com (by lemmling)



/*

    Mindeste API fuer diese APP = KITKAT

 */


public class MainActivity extends AppCompatActivity {

    /*
    *
    *--------Konstanten-Deklaration------------
    *
    */

    //LOG-Konstante
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    // Membervariable dataSource vom Typ GlaubenssatzMemoDataSource
    private GlaubenssatzeMemoDataSource dataSource;
    //Membervariable des Databasehelper
    private GlaubenssaetzeMemoDbHelper dbHelper;


    //Navigation Bar Variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;








    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        //------------- NavigationBar ---------------------
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView nav_view = (NavigationView)findViewById(R.id.nav_menu);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Menüeintrag zum generieren
                if (id == R.id.menu_generate)
                {
                    Intent generate = new Intent(MainActivity.this, Text_Generate_Activity.class);
                    startActivity(generate);
                    return false;
                }

                //Menüeintrag zum eingeben
                if (id == R.id.menu_input)
                {
                    Intent input = new Intent(MainActivity.this, Text_Output.class);
                    startActivity(input);
                    return false;
                }

                //Menüeintrag zur Satzliste
                if (id == R.id.menu_list)
                {
                    Intent list = new Intent(MainActivity.this, Sentence_List.class);
                    startActivity(list);
                    return false;
                }

                //Menüeintrag zur Hilfeseite
                if (id == R.id.helppage)
                {
                    Intent help = new Intent(MainActivity.this, Help.class);
                    startActivity(help);
                    return false;
                }

                return false;
            }
        });

        //------------- NavigationBar ---------------------








        //------------- Neues Objekt anlegen --------------
        dataSource = new GlaubenssatzeMemoDataSource(this);

        //Datenbank wird geoeffnet
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();
        if (dataSource.getAllGlaubenssaetzeMemos().isEmpty())
        {
            addAllSentences();
        }

        //Anzeigen der Einträge in der Datenbank
        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        //Entries written into a List
        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();
        //List is converted into a string to show it in a log message
        String dbentries = glaubenssaetzeMemoList.toString();
        Log.d(LOG_TAG,dbentries);
        //------------- Neues Objekt anlegen --------------


        //init function to initialize all buttons!
        init();



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


    private void showAllEntries() {

        //all entries from the db were written into the List "glaubenssaetzeMemoList"
        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();

        ArrayAdapter<GlaubenssaetzeMemo> glaubenssaetzeMemoArrayAdapter = new ArrayAdapter<GlaubenssaetzeMemo>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                glaubenssaetzeMemoList);

    }






    @Override
    protected void onResume(){
        super.onResume();

        Log.d(LOG_TAG,"Die Datenquelle wird geöffnet.");
        dataSource.open();
        Log.d(LOG_TAG,"Folgende Einträge sind in der Datenbank vorhanden:");
        //all entries were deleted
        //dbHelper.clearTable();
        showAllEntries();



    }



    //Buttons with Functions
    public Button textGenerate;
    public Button textInput;
    public Button showList;
    public Button helpButton;

    //init function initialized all clickable buttons
    public void init(){

        //Button is directing to the page where the user sees a generated sentence
        textGenerate = (Button)findViewById(R.id.generatetext);
        textGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sentence_creater = new Intent(MainActivity.this,Text_Generate_Activity.class);
                startActivity(sentence_creater);

            }
        });

        //Button is directing to the page where the user can type in a new sentence
        textInput = (Button)findViewById(R.id.inputtext);
        textInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sentence_input = new Intent(MainActivity.this,Text_Output.class);
                startActivity(sentence_input);
            }
        });

        //Button is directing to the page where the user gets help to use the app
        helpButton = (Button)findViewById(R.id.helppage);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , Help.class);
                startActivity(intent);
            }
        });



        //Button is directing to the page where the user sees all of the database entries
        showList = (Button)findViewById(R.id.showlist);
        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataSource.open();
                Intent sentence_list = new Intent(MainActivity.this,Sentence_List.class);
                startActivity(sentence_list);
            }
        });




    }//End of init function

//Function to add all sentences if the list is empty
public void addAllSentences()

    {
        //Pairnumber 1
        dataSource.createGlaubenssaetzeMemo(" n " , 1 , "Um im Studium erfolgreich zu sein, muss man Tag und Nacht pauken.");
        dataSource.createGlaubenssaetzeMemo(" p " , 1 , "Ich erreiche meine Ziele mit Leichtigkeit.");

        //Pairnumber 2
        dataSource.createGlaubenssaetzeMemo(" n " , 2 , "Im Studium erfolgreich zu sein ist sehr anstrengend");
        dataSource.createGlaubenssaetzeMemo(" p " , 2 , "Ich erreiche meine Ziele mit Leichtigkeit");
        dataSource.createGlaubenssaetzeMemo(" p " , 2 , "Das Lernen fällt mir leicht. Ich bin ein Lerngenie!");
        //Pairnumber 3
        dataSource.createGlaubenssaetzeMemo(" n " , 3 , "Im Studium wirklich erfolgreich zu sein bedeutet, keine Freizeit mehr zu haben");
        dataSource.createGlaubenssaetzeMemo(" p " , 3 , "Ich erreiche meine Ziele mit Leichtigkeit");
        dataSource.createGlaubenssaetzeMemo(" p " , 3 , "Ich löse alle Aufgaben schnell und leicht");
        //Pairnumber 4
        dataSource.createGlaubenssaetzeMemo(" n " , 4 , "Im Studium erfolgreich zu sein, bedeutet Einzelgänger zu sein oder zu werden.");
        dataSource.createGlaubenssaetzeMemo(" p " , 4 , "Es ist in Ordnung, wenn ich mehr Erfolg habe als andere. Ich bin eine Inspiration für andere. Meine Freunde und meine Familie sind stolz auf mich");

        //Pairnumber 5
        dataSource.createGlaubenssaetzeMemo(" n " , 5 , "Ich bin nicht Intelligent genug, um wirklich erfolgreich im Studium zu sein");
        dataSource.createGlaubenssaetzeMemo(" p " , 5 , "Ich bin gut!");

        //Pairnumber 6
        dataSource.createGlaubenssaetzeMemo(" n " , 6 , "Das schaffe ich nicht.");
        dataSource.createGlaubenssaetzeMemo(" p " , 6 , "Einfach anfangen, der Rest läuft dann von selbst!");

        //Pairnumber 7
        dataSource.createGlaubenssaetzeMemo(" n " , 7 , "Ich kann das nicht");
        dataSource.createGlaubenssaetzeMemo(" p " , 7 , "Ich habe eine gute, schnelle Auffassungsgabe");

        //Pairnumber 8
        dataSource.createGlaubenssaetzeMemo(" n " , 8, "Ich bin nicht so gut wie andere");
        dataSource.createGlaubenssaetzeMemo(" p " , 8, "Ich bin gut/ausreichend vorbereitet");
        dataSource.createGlaubenssaetzeMemo(" p " , 8, "Ich bin gut!");

        //Pairnumber 9
        dataSource.createGlaubenssaetzeMemo(" n " , 9 , "Ich bin nicht so selbstbewusst wie andere");
        dataSource.createGlaubenssaetzeMemo(" p " , 9 , "Ich habe Talent");
        dataSource.createGlaubenssaetzeMemo(" p " , 9 , "Ich weiß, was ich will. Und darauf fokussiere ich mich");

        //Pairnumber 10
        dataSource.createGlaubenssaetzeMemo(" n " , 10 , "Fehler sind peinlich");
        dataSource.createGlaubenssaetzeMemo(" p " , 10 , "Ich frage nach, wenn ich nicht weiterweiß");
        dataSource.createGlaubenssaetzeMemo(" p " , 10 , "Egal, was heute passiert, ich bleibe innerlich ruhig und entspannt");

        //Pairnumber 11
        dataSource.createGlaubenssaetzeMemo(" n " ,11 , "Es ist mir peinlich, wenn jemand bemerkt, dass ich etwas nicht verstanden habe");
        dataSource.createGlaubenssaetzeMemo(" p " ,11 , "Ich frage nach, wenn ich nicht weiterweiß");

        //Pairnumber 12
        dataSource.createGlaubenssaetzeMemo(" n " , 12, "Ich bin nicht fleißig genug, um wirklich im Studium erfolgreich zu sein");
        dataSource.createGlaubenssaetzeMemo(" p " , 12, "Satz fehlt - bitte nachtragen");

        //Pairnumber 13
        dataSource.createGlaubenssaetzeMemo(" n " , 13, "Wenn ich deutlich erfolgreicher wäre, würden andere mich für einen Streber halten");
        dataSource.createGlaubenssaetzeMemo(" p " , 13 , "Es ist in Ordnung, wenn ich mehr Erfolg habe als andere. Ich bin eine Inspiration für andere. Meine Freunde und meine Familie sind stolz auf mich");

        //Pairnumber 14
        dataSource.createGlaubenssaetzeMemo(" n " , 14, "Wenn ich deutlich erfolgreicher wäre, wären andere neidisch auf mich");
        dataSource.createGlaubenssaetzeMemo(" p " , 14 , "Meine Ideen und Anmerkungen im Unterricht sind interessant für andere");
        dataSource.createGlaubenssaetzeMemo(" p " , 14 , "Es ist in Ordnung, wenn ich mehr Erfolg habe als andere. Ich bin eine Inspiration für andere. Meine Freunde und meine Familie sind stolz auf mich");

        //Pairnumber 15
        dataSource.createGlaubenssaetzeMemo(" n " , 15 , "Im Moment habe ich doch schon so zu viel um die Ohren, da kann ich jetzt nicht auch noch zusätzlich ein Selbstcoaching-Programm durchführen");
        dataSource.createGlaubenssaetzeMemo(" p " , 15, "Die gelernten Selbstcoaching-Strategien setze ich mit Freude und großem Erfolg für mich um");
        dataSource.createGlaubenssaetzeMemo(" p " , 15, "Selbstcoaching gehört zu meinem Alltag");

        //Pairnumber 16
        dataSource.createGlaubenssaetzeMemo(" n " , 16, "Unsichere Menschen können nicht lernen, selbstsicher(er) zu werden");
        dataSource.createGlaubenssaetzeMemo(" p " , 16 , "Weil ich mich selbst mag und zu mir stehe, kommt der Erfolg ganz von alleine");

        //Pairnumber 17
        dataSource.createGlaubenssaetzeMemo(" n " , 17 , "Ich glaube nicht, dass man seine innere Einstellung verändern kann");
        dataSource.createGlaubenssaetzeMemo(" p " , 17 , "Weil ich mich selbst mag und zu mir stehe, kommt der Erfolg ganz von alleine");

        //Pairnumber 18
        dataSource.createGlaubenssaetzeMemo(" n " , 18 , "So wie man ist, ist man eben. Dagegen kann man nichts tun!");
        dataSource.createGlaubenssaetzeMemo(" p " , 18 , "In mir steckt mehr");

        //Pairnumber 19
        /*
        dataSource.createGlaubenssaetzeMemo(" n " , 19, "");
        dataSource.createGlaubenssaetzeMemo(" p " ,19 , "");
        */

        //Pairnumber 20
        dataSource.createGlaubenssaetzeMemo(" n " , 20, "Bei mir funktionieren solche Psychotricks nicht");
        dataSource.createGlaubenssaetzeMemo(" p " , 20, "Selbstcoaching gehört zu meinem Alltag.");
    }


}