package fhbielefeld.glaubenssaetze_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

public class Text_Generate_Activity extends AppCompatActivity {

    // Membervariable dataSource vom Typ GlaubenssatzMemoDataSource
    private GlaubenssatzeMemoDataSource dataSource;

    //LOG-Konstanten
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Pairnumber
    private long currentPairNumber;

    //Navigation Bar Variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_generate_);

        dataSource = new GlaubenssatzeMemoDataSource(this);

        final Button btn = (Button) findViewById(R.id.button3);
        final Button btn2 = (Button) findViewById(R.id.button);
        btn.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.INVISIBLE);





        //------------- NavigationBar ---------------------
        final NavigationView nav_view_generate = (NavigationView)findViewById(R.id.nav_menu_generate);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Textview
        final TextView randomText = (TextView) findViewById(R.id.randomText);

        //Videosequenz
        //which Videoview is used?
        final VideoView videoview = (VideoView) findViewById(R.id.playvideosequenz);

        //Which video should be played out of the raw folder?
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.explode);
        videoview.setVideoURI(uri);

        //Make the videoview box invisible
        videoview.setVisibility(View.INVISIBLE);


        nav_view_generate.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //Menüeintrag zum generieren
                if (id == R.id.menu)
                {
                    Intent generate = new Intent(Text_Generate_Activity.this, MainActivity.class);
                    startActivity(generate);
                    return false;
                }

                //Menüeintrag zum eingeben
                if (id == R.id.menu_input)
                {
                    Intent input = new Intent(Text_Generate_Activity.this, Text_Output.class);
                    startActivity(input);
                    return false;
                }

                //Menüeintrag zur Satzliste
                if (id == R.id.menu_list)
                {
                    Intent list = new Intent(Text_Generate_Activity.this, Sentence_List.class);
                    startActivity(list);
                    return false;
                }

                //Menüeintrag zur Hilfeseite
                if (id == R.id.menu_help)
                {
                    Intent help = new Intent(Text_Generate_Activity.this, Help.class);
                    startActivity(help);
                    return false;
                }
                return false;
            }
        });


        //------------- NavigationBar ---------------------



        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                //Make the videoview box invisible
                videoview.setVisibility(View.INVISIBLE);

                randomText.setError(null);

                //Make the button invisible after a click
                btn.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.VISIBLE);

                //generate a sentece out of the database
                Random randomGenerator = new Random();
                //Datenquelle wird geoeffnet
                dataSource.open();
                //Liste
                List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();
                //get a random sentence from the list
                int index = randomGenerator.nextInt(glaubenssaetzeMemoList.size());
                GlaubenssaetzeMemo item = glaubenssaetzeMemoList.get(index);
                String randomObject = item.getSentence().toString();
                randomText.setText(randomObject);
                setCurrentPairNumber(item.getPairnumber());
                long Pairnumber = item.getPairnumber();
                Log.d(LOG_TAG, "Die gematchte Paarnummer ist: " + Pairnumber);
                dataSource.close();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
        {

            if(!randomText.getText().toString().matches(""))
            {

                Button button = (Button) v;
                button.setVisibility(View.INVISIBLE);

                //Make the textview invisible
                randomText.setVisibility(View.INVISIBLE);

                //Make the button invisible
                btn.setVisibility(View.INVISIBLE);

                //Make the videoviewbox visible
                videoview.setVisibility(View.VISIBLE);

                //start the video
                videoview.start();


                //go to the next activity when the video is finished
                videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        long Pairnumber = getCurrentPairNumber();
                        Intent positive_sentence = new Intent(Text_Generate_Activity.this,Sentence_List_Positiv.class);
                        positive_sentence.putExtra(" Pairnumber " , Pairnumber);
                        startActivity(positive_sentence);
                    }
                });

            }
            else
            {

                randomText.setError(getString(R.string.sentence_generate_error));
                return;
            }



        }
        });

    }


    public long getCurrentPairNumber() {
        return currentPairNumber;
    }

    public void setCurrentPairNumber(long currentPairNumber) {
        this.currentPairNumber = currentPairNumber;
    }

    //------------- NavigationBar ---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.navigation_menu_generate, menu);
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
