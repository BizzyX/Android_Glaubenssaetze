package fhbielefeld.glaubenssaetze_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.widget.VideoView;
import java.util.List;
import java.util.Random;

public class Text_Generate_Activity extends AppCompatActivity {

    // Membervariable dataSource vom Typ GlaubenssatzMemoDataSource
    private GlaubenssatzeMemoDataSource dataSource;

    //LOG-Konstanten
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Pairnumber
    private long currentPairNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_generate_);

        dataSource = new GlaubenssatzeMemoDataSource(this);

        Button btn = (Button) findViewById(R.id.button3);
        Button btn2 = (Button) findViewById(R.id.button);

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



        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                //Make the videoview box invisible
                videoview.setVisibility(View.INVISIBLE);


                //generate a sentece out of the database
                Random randomGenerator = new Random();
                //Datenquelle wird geoeffnet
                dataSource.open();
                //Liste
                List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();
                //Einen Random Eintrag aus der Liste
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

            //Make the textview invisible
            randomText.setVisibility(View.INVISIBLE);

            //Make the videoviewbox visible
            videoview.setVisibility(View.VISIBLE);

            //start the video
            videoview.start();


            //go to the next activity when the video is finished
            videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
              @Override
              public void onCompletion(MediaPlayer mp) {

                  long Pairnumber = getCurrentPairNumber();
                  Intent positive_sentence = new Intent(Text_Generate_Activity.this,Text_Generate_Activity_Answer.class);
                  positive_sentence.putExtra(" Pairnumber " , Pairnumber);
                  startActivity(positive_sentence);
              }
          });
        }
        });

    }


    public long getCurrentPairNumber() {
        return currentPairNumber;
    }

    public void setCurrentPairNumber(long currentPairNumber) {
        this.currentPairNumber = currentPairNumber;
    }
}
