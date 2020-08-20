package fhbielefeld.glaubenssaetze_app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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
// TODO:  Gleiche Groeße für alle Bildschirmgrößen - https://www.youtube.com/watch?v=Fexqyr1MBbo


/*

    TODO:
    Änderungen Ohlhoff
    --------------------------------------------------

    1.) als Überschrift immer und überall "Glaubenssätze knacken" statt einfach nur "Glaubenssätze"

    2.) Die 3 Striche oben links um ins Menü zu kommen, sollten immer und auf allen Ebenen da sein.

    3.) Glaubenssätze sind manchmal zu lang: kürzen oder zum scrollen anbieten, z.B. bei Glaubenssatz lösen: " Es ist in Ordnung, wenn ich mehr Erfolg....."

    4.) bei "Satzpaar generieren" sollte gleich ein Satz kommen, ohne daß man nochmal drücken muß

    5.) wenn ein "zufälliger negativer Glaubenssatz" angezeigt wird, sollte es 2 Buttons geben: " den glaubenssatz auflösen" (gibt es schon) und "anderen zufälligen Satz generieren"

    Änderungen Klenke
    ---------------------------------------------------

 */


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
                if (id == R.id.menu_help)
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
//        getMenuInflater().inflate(R.menu.navigation_menu, menu);
//       return true;
       return false;
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
        //return false;
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


    public Button startButton;

    //init function initialized all clickable buttons
    public void init(){

        //Startbutton to open the NavBar
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
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
//        dataSource.createGlaubenssaetzeMemo(" n " , 4 , "Im Studium erfolgreich zu sein, bedeutet Einzelgänger zu sein oder zu werden.");
//        dataSource.createGlaubenssaetzeMemo(" p " , 4 , "Es ist in Ordnung, wenn ich mehr Erfolg habe als andere. Ich bin eine Inspiration für andere. Meine Freunde und meine Familie sind stolz auf mich");

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
//        dataSource.createGlaubenssaetzeMemo(" n " , 12, "Ich bin nicht fleißig genug, um wirklich im Studium erfolgreich zu sein");
//        dataSource.createGlaubenssaetzeMemo(" p " , 12, "Satz fehlt - bitte nachtragen");

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
        dataSource.createGlaubenssaetzeMemo( " n "  , 21 , "Mir fehlt Grundwissen für das Studium.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 21 , "In mir steckt mehr");
        dataSource.createGlaubenssaetzeMemo( " p "  , 21 , "Ich weiß, ich bin gut genug.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 22 , "Zu viel pauken nimmt mir Lebensfreude.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 22 , "Das Lernen fällt mir leicht. Ich bin ein Lerngenie");
        dataSource.createGlaubenssaetzeMemo( " p "  , 22 , "Ich habe Freude am Lernen.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 23 , "Was mich persönlich interessiert, passt nicht zu dem, was von mir im Studium erwartet wird.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 23 , "Mein Studium liegt mir.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 24 , "Ich muss mich genau an die Anweisung des Professors halten, wenn ich gute Noten bekommen will.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 24 , "Ich habe einen guten Riecher für Menschen, Bücher, Infos die mir weiterhelfen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 24 , "Ich lerne ganz leicht, sobald ich den für mich richtigen Lernweg entdeckt habe.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 25 , "Mir reicht es schon, wenn ich eine Klausur irgendwie bestehe.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 25 , "Ich bestehe meine Prüfung!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 25 , "Ich habe Erfolg und werde jetzt immer erfolgreicher.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 26 , "Mir reicht es schon, wenn ich nur irgendwie meinen Abschluss schaffe.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 26 , "Ich bestehe meine Prüfung!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 26 , "Ich habe Erfolg und werde jetzt immer erfolgreicher.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 27 , "Es fällt mir schwer, andere etwas zu fragen, was ich nicht verstanden habe.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 27 , "Ich frage nach, wenn ich nicht weiterweiß.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 27 , "Ich habe einen guten Riecher für Menschen, Bücher, Infos die mir weiterhelfen.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 28 , "Wenn ich Fragen stelle, merkt jeder, wie dumm ich bin.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 28 , "Ich frage nach, wenn ich nicht weiterweiß.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 29 , "In unserer Familie war noch nie jemand wirklich erfolgreicher Akademiker.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 29 , "In mir steckt mehr!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 29 , "Ich bin einzigartig.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 29 , "Ich akzeptiere, nutze und liebe meine Einzigartigkeit.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 30 , "Ich war schon in der Schule schlecht, wie soll es denn da im Studium besser werden?");
        dataSource.createGlaubenssaetzeMemo( " p "  , 30 , "In mir steckt mehr!");


        dataSource.createGlaubenssaetzeMemo( " n "  , 32 , "Ich habe Prüfungen manchmal nur bestanden, weil ich Glück gehabt habe. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 32 , "Für Prüfungen: Ich habe wirklich genug dafür getant!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 32 , "Ich habe alles getan, was erforderlich war.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 32 , "Ich bin zufrieden mit meiner Leistung.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 32 , "Mein Gehirn saugt neues Wissen auf, wie ein Schwamm das Wasser.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 33 , "Ich erledige Aufgaben immer erst auf den letzten Drücker. Ich schaffe es einfach nicht, mich früher aufzuraffen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 33 , "Ich habe den Dreh raus, wie ich einfacher, entspannter und effektiver studieren und lernen kann.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 33 , "Ich habe mich meinem Ziel verpflichtet und ziehe das jetzt durch.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 34 , "Ich brauche Druck, um in die Gänge zu bekommen. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 34 , "Ich habe den Dreh raus, wie ich einfacher, entspannter und effektiver studieren und lernen kann.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 34 , "Ich habe mich meinem Ziel verpflichtet und ziehe das jetzt durch.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 35 , "Ich bin körperlich zu schwach, um Gutes im Studium zu leisten.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 35 , "In mir steckt mehr!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 35 , "Ich erreiche meine Ziele mit Leichtigkeit.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 35 , "Ich erreiche alles, was ich mir vorgenommen habe.");


        dataSource.createGlaubenssaetzeMemo( " n "  , 37 , "Streber sind doof. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 37 , "Es ist in Ordnung, wenn ich mehr Erfolg habe als andere. Ich bin eine Inspiration für andere.");






        dataSource.createGlaubenssaetzeMemo( " n "  , 41 , "Mein innerer Schweinehund ist stärker, als meine Motivation zu Lernen. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 41 , "Ich weiß, was ich will. Und darauf fokussiere ich mich.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 41 , "Ich schaffe es: Schritt für Schritt.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 42 , "Der Stoff des Studiums ist zu trocken.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 42 , "Die gelernten Selbstcoaching-Strategien setze ich mit Freude und großem Erfolg für mich um.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 42 , "Ich bin und bleibe immer optimistisch.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 42 , "Ich halte durch!");
        dataSource.createGlaubenssaetzeMemo( " n "  , 43 , "Ich habe keine Lust zu lernen. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 43 , "Ich weiß, was ich will. Und darauf fokussiere ich mich.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 43 , "Ich habe Freude am Lernen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 43 , "Ich studiere und lerne zielorientiert.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 44 , "Ich kapiere das Modul für das ich lernen soll nicht. Es ist zu schwer für mich.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 44 , "Je mehr ich übe, umso besser werde ich.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 44 , "Ich lerne ganz leicht, sobald ich den für mich richtigen Lernweg entdeckt habe.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 45 , "Es wäre mir peinlich, wenn die anderen wüssten, wie wenig ich mitbekomme.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 45 , "Ich akzeptiere, nutze und liebe meine Einzigartigkeit.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 45 , "Ich werde Menschen und Informationsquellen auftun, die mich unterstützen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 45 , "Ich habe es auch bis hierher geschafft!");
        dataSource.createGlaubenssaetzeMemo( " n "  , 46 , "Ich habe keine Lust auf Texte schreiben. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 46 , "Ich weiß, was ich will. Und darauf fokussiere ich mich.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 46 , "Ich habe Freude am Lernen.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 47 , "Ich bin nicht intelligent genug, um in einem akademischen Studium erfolgreich zu sein. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 47 , "Ich schaffe das!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 47 , "Schritt für Schritt komme ich meinen Zielen nah.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 47 , "Ich vertraue auf mein Potenzial.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 48 , "Ich kann mich nicht lange genug konzentrieren. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 48 , "Ich bin und bleibe immer optimistisch.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 49 , "Es ist mir unmöglich gut zu lernen, wenn der Stoff so chaotisch präsentiert wird. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 49 , "Das wird schon!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 49 , "Das Studium ist ein Übungsfeld, in dem ich lernen und wachsen kann.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 50 , "Ich kann mir nicht vorstellen, wie ich je das Studium schaffen werde. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 50 , "Ich habe Talent.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 50 , "Ich bin ein Glückskind.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 51 , "Dieser Studiengang ist einfach schlecht konzipiert.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 51 , "Ich sorge gut für mich selbst.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 52 , "Ich bin nicht intelligent genug, um mich angemessen auf die Prüfung vorzubereiten. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 52 , "Ich schaffe das!");
        dataSource.createGlaubenssaetzeMemo( " n "  , 53 , "Es dauert zu lang und ist zu schwer, den Stoff in den Modulen richtig zu verstehen. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 53 , "Ich schaffe das!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 53 , "Ich habe alles getan, was erforderlich war.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 54 , "Ich habe zu viele Wissenslücken.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 54 , "Ich bin gut vorbereitet.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 54 , "Ich weiß, ich bin auf dem richtigen Weg.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 55 , "Ich habe nicht genügend Zeit, mich auf die Klausur vorzubereiten.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 55 , "Ich habe die Zeit, die ich brauche.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 56 , "Ich bin zu langsam.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 56 , "Ich habe die Zeit, die ich brauche.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 57 , "Es ist einfach alles zu viel.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 57 , "Ich finde immer Unterstützung!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 57 , "Ich schaffe es: Schritt für Schritt.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 57 , "Das schaffe ich mit links!");
        dataSource.createGlaubenssaetzeMemo( " n "  , 58 , "Erst die Arbeit, dann das Vergnügen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 58 , "Ich gehe sanft, liebevoll und fürsorglich mit mir um.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 58 , "Ich darf experimentieren und ausprobieren.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 59 , "Als Frau muss ich mich immer zuallererst um meine Kinder kümmern. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 59 , "Ich steuere mein Studium. Ich bin der Boss in meinem Studium.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 60 , "Als Frau sollte ich stets sanft bleiben.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 60 , "Ich bin, wie ich bin.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 61 , "Als Mann muss ich mich außen immer stark zeigen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 61 , "Ich bin, wie ich bin.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 62 , "Ich brauche es garnicht erst versuchen, ich schaffe es ohnehin nicht. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 62 , "In mir steckt mehr!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 62 , "Ich schaffe es: Schritt für Schritt.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 62 , "Ich bin erfolgreich.");


        dataSource.createGlaubenssaetzeMemo( " n "  , 64 , "Ich werde nicht ausreichend beim Lernen unterstützt.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 64 , "Ich erkenne neue Chancen sofort, wenn sie sich auftun.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 65 , "Wenn man offen sagt, was man wirklich denkt, macht man sich unbeliebt. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 65 , "Ich bin, wie ich bin.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 65 , "Ich stehe zu mir und meinen Eigenheiten.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 66 , "Wenn ich auf Fehler hingewiesen werden, fühle ich mich unwohl.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 66 , "Ich vertraue auf mein Potenzial.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 66 , "Ich stehe zu mir und meinen Eigenheiten.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 67 , "Ich leide unter Aufschieberitis. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 67 , "Auch wenn ich jetzt noch nicht ganz die Lösung weiß, ich fange an und bin sicher, dass ich Schritt für Schritt zum Ziel kommen werde.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 67 , "Ich habe mich meinem Ziel verpflichtet und ziehe das jetzt durch.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 67 , "Einfach anfangen, der Rest läuft dann von selbst!");
        dataSource.createGlaubenssaetzeMemo( " n "  , 68 , "Ich habe einfach keine Disziplin. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 68 , "Ich habe mich meinem Ziel verpflichtet und ziehe das jetzt durch.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 68 , "Ich studiere und lerne zielorientiert.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 69 , "Ich bin eben faul.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 69 , "Ich habe mich meinem Ziel verpflichtet und ziehe das jetzt durch.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 70 , "Ich habe Angst vor mündlichen Prüfungen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 70 , "Für Prüfungen: Ich habe wirklich genug dafür getant!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 70 , "Ich habe alles getan, was erforderlich war.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 70 , "Ich bin gut vorbereitet.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 70 , "Das schaffe ich mit links!");
        dataSource.createGlaubenssaetzeMemo( " n "  , 71 , "Ich kann schlecht auswendig lernen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 71 , "Ich experimentiere und probiere solange, bis ich meinen Weg gefunden habe. ");
        dataSource.createGlaubenssaetzeMemo( " n "  , 72 , "Ich bin eine Zumutung für andere. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 72 , "Ich bin, wie ich bin.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 73 , "In der Klausurvorbereitung habe ich versagt. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 73 , "Das wird schon!");
        dataSource.createGlaubenssaetzeMemo( " p "  , 73 , "Ich finde eine Lösung.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 74 , "Ich hätte ein besserer Schüler sein sollen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 74 , "Ich stehe zu mir und meinen Eigenheiten.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 75 , "Ich müsste mich wirklich mehr um mein Studium kümmern. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 75 , "Jeden Tag mache ich einen Schritt in Richtung auf mein Ziel.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 76 , "Ich bin ein Versager. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 76 , "Ich bin intelligent und mutig.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 77 , "Ich schäme mich, dass ich so langsam/faul bin. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 77 , "Ich stehe zu mir und meinen Eigenheiten.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 78 , "Ich darf nicht durchfallen. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 78 , "Jetzt wage ich es und ich werde die Prüfung bestehen.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 79 , "Ich darf keine Fehler machen.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 79 , "Fehler sind ledigleich ein Feedback, aus dem ich lernen kann, besser zu werden.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 80 , "Ich darf nicht so viele Fehler machen. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 80 , "Fehler sind ledigleich ein Feedback, aus dem ich lernen kann, besser zu werden.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 81 , "Ich sollte mich endlich zusammenreißen. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 81 , "Ich erreiche meine Ziele mit Leichtigkeit.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 82 , "Mir fällt nichts mehr ein. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 82 , "Mir fällt immer irgendetwas ein, wenn ein Problem zu lösen ist.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 83 , "Ich habe schon alles probiert: Es ist hoffnungslos.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 83 , "Mir fällt immer irgendetwas ein, wenn ein Problem zu lösen ist.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 84 , "Ich hänge fest und weiß nicht weiter.");
        dataSource.createGlaubenssaetzeMemo( " p "  , 84 , "Jeder Schritt ergibt sich ganz natürlich aus dem vorhergehenden. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 84 , "Ich vertraue auf mein Potenzial.");
        dataSource.createGlaubenssaetzeMemo( " n "  , 85 , "Ich weiß beim besten Willen nicht, was ich jetzt noch tun soll. ");
        dataSource.createGlaubenssaetzeMemo( " p "  , 85 , "Ich werde Menschen und Informationsquellen auftun, die mich unterstützen.");
    }




}