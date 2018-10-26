package fhbielefeld.glaubenssaetze_app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

        Button playfindSentence = (Button) this.findViewById(R.id.find_sentences);

        //Play Sound
        playfindSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                findSentence.start();
            }

        });
        //Stop Sound
        Button stopfindSentence = (Button) this.findViewById(R.id.stop_sound1);

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

        Button playusefullApp = (Button) this.findViewById(R.id.usefull_app);

        //Play Sound
        playusefullApp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                usefullApp.start();
            }
        });

        //Stop Sound
        Button stopusefullSentence = (Button) this.findViewById(R.id.stop_sound2);

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
