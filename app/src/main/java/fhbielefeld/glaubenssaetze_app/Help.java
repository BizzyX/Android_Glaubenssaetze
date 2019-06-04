package fhbielefeld.glaubenssaetze_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
//implements View.OnClickListener

public class Help extends AppCompatActivity {


    //Navigation Bar Variables
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //Seekbar
    private SeekBar seekBar;
    private SeekBar seekBar2;
    private Runnable runnable;
    private Handler handler;
    private ImageButton btnPlay1;
    private ImageButton btnStop1;
    private ImageButton btnBack1;
    private MediaPlayer findSentence;
    private MediaPlayer usefullApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        //Find Sentence
        findSentence = MediaPlayer.create(this, R.raw.find_sentences);

        //Useful App
        usefullApp = MediaPlayer.create(this, R.raw.usefull_app);


        //Buttons(Play,Stop,Back)
        btnPlay1 = findViewById(R.id.playbutton1);
        btnStop1 = findViewById(R.id.stopbutton1);
        btnBack1 = findViewById(R.id.backbutton1);

        handler = new Handler();

        //PLAY SOUNDS TO HELP THE USER
        seekBar = findViewById(R.id.seekBar);
        seekBar2 = findViewById(R.id.seekBar2);



        findSentence.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(findSentence.getDuration());
                playCycle();
                findSentence.start();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input)
                {
                    findSentence.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





        //Play Sound
        btnPlay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(findSentence.getDuration());
                playCycle();
                findSentence.start();


            }

        });


        btnStop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    seekBar.setProgress(findSentence.getDuration());
                    playCycle();
                    findSentence.pause();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        });

      btnBack1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              if (findSentence.isPlaying())
              {
                  try {
                      seekBar.setProgress(findSentence.getDuration());
                      playCycle();
                      findSentence.pause();
                      findSentence.seekTo(0);
                  }
                  catch (Exception e)
                  {
                      e.printStackTrace();
                  }
              }
              else return;

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



        //ImageButton playusefullApp = (ImageButton) this.findViewById(R.id.playbutton2);

        /*
        //Play Sound
        playusefullApp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usefullApp.start();
            }
        });
        */
        //Stop Sound
        //ImageButton stopusefullSentence = (ImageButton) this.findViewById(R.id.stopbutton2);

        /*
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
        */





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


    //------------- seekBar1 ---------------------
    public void playCycle()
    {
        seekBar.setProgress(findSentence.getCurrentPosition());
        if (findSentence.isPlaying())
        {
            runnable = new Runnable() {
                @Override
                public void run() {
                playCycle();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        findSentence.release();
        handler.removeCallbacks(runnable);
    }
    //------------- seekBar1 ---------------------


    @Override
    protected void onResume() {
        super.onResume();
        findSentence.start();
        playCycle();
    }

    @Override
    protected void onPause() {
        super.onPause();
        findSentence.pause();
    }
}

