package fhbielefeld.glaubenssaetze_app;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by philpetschull on 06.06.18.
 *
 *
 */

public class Sentence_List_Positiv extends AppCompatActivity {

    // Membervariable dataSource vom Typ GlaubenssatzMemoDataSource
    private GlaubenssatzeMemoDataSource dataSource;

    //LOG-Konstanten
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentencelist_positiv);
        TextView matchingTextPositiv = (TextView) findViewById(R.id.randomText);
        dataSource = new GlaubenssatzeMemoDataSource(this);

        Bundle extras = getIntent().getExtras();
        long Pairnumber = 1;
        if (extras != null) {
            Pairnumber = extras.getLong(" Pairnumber ");
        }

        dataSource.open();
        Log.d(LOG_TAG, "Die erhaltene Paarnummer ist: " +  Pairnumber);
        showAllPositiveEntries(Pairnumber);
        dataSource.close();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showAllPositiveEntries(long Pairnumber) {

        long currentPairNumber = Pairnumber;
        Log.d(LOG_TAG, " Funktion : showAllPositiveEntries Die erhaltene Paarnummer ist: " + currentPairNumber );

        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getPostiveSentences( currentPairNumber );

        ArrayAdapter<GlaubenssaetzeMemo> glaubenssaetzeMemoArrayAdapter = new ArrayAdapter<GlaubenssaetzeMemo>(
                this,
                R.layout.custom_item_list_adapter,
                glaubenssaetzeMemoList);


        ListView sentenceList = (ListView) findViewById(R.id.sentencelist__positiv);
        sentenceList.setAdapter(glaubenssaetzeMemoArrayAdapter);
        dataSource.close();

    }



}


