package fhbielefeld.glaubenssaetze_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

/**
 * Created by philpetschull on 20.06.18.
 */

public class Help extends AppCompatActivity{

    //Navigation Bar Variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);



        //PLAY SOUNDS TO HELP THE USER

        //Find Sentence
        final MediaPlayer findSentence = MediaPlayer.create(this, R.raw.find_sentences);


        ImageButton playfindSentence = (ImageButton) this.findViewById(R.id.playbutton1);

        //Play Sound
        playfindSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                findSentence.start();
            }

        });
        //Stop Sound
        ImageButton stopfindSentence = (ImageButton) this.findViewById(R.id.stopbutton1);

        stopfindSentence.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    findSentence.pause();
                    findSentence.seekTo(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        });

        //------------- NavigationBar ---------------------
        final NavigationView nav_view_generate = (NavigationView)findViewById(R.id.nav_menu_help);

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
                    Intent generate = new Intent(Help.this, MainActivity.class);
                    startActivity(generate);
                    return false;
                }

                //Menüeintrag zum eingeben
                if (id == R.id.menu_generate)
                {
                    Intent input = new Intent(Help.this, Text_Generate_Activity.class);
                    startActivity(input);
                    return false;
                }

                //Menüeintrag zur Satzliste
                if (id == R.id.menu_input)
                {
                    Intent list = new Intent(Help.this, Text_Output.class);
                    startActivity(list);
                    return false;
                }

                //Menüeintrag zur Hilfeseite
                if (id == R.id.menu_list)
                {
                    Intent help = new Intent(Help.this, Sentence_List.class);
                    startActivity(help);
                    return false;
                }
                return false;
            }
        });


        //------------- NavigationBar ---------------------

        //Useful App
        final MediaPlayer usefullApp = MediaPlayer.create(this,R.raw.usefull_app);

        ImageButton playusefullApp = (ImageButton) this.findViewById(R.id.playbutton2);

        //Play Sound
        playusefullApp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usefullApp.start();
            }
        });

        //Stop Sound
        ImageButton stopusefullSentence = (ImageButton) this.findViewById(R.id.stopbutton2);

        stopusefullSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    usefullApp.pause();
                    usefullApp.seekTo(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    //------------- NavigationBar ---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.navigation_menu_help, menu);
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
