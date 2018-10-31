package fhbielefeld.glaubenssaetze_app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

/**
 * Created by philpetschull on 20.06.18.
 */

public class Help extends AppCompatActivity{


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
}
