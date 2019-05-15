package fhbielefeld.glaubenssaetze_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.widget.AbsListView;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;


public class Sentence_List extends AppCompatActivity {

    // Membervariable dataSource vom Typ GlaubenssatzMemoDataSource
    private GlaubenssatzeMemoDataSource dataSource;

    //Membervariable des Databasehelper
    private GlaubenssaetzeMemoDbHelper dbHelper;

    // Membervariable zum abspeichern in der Datenbank
    private SQLiteDatabase database;

    //LOG-Konstanten
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    public ListView listView;


    //Pairnumber
    private long currentPairNumber;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentencelist);
        final ListView sentenceList = (ListView) findViewById(R.id.sentencelist);

        //Videosequenz
        //which Videoview is used?
        final VideoView videoview = (VideoView) findViewById(R.id.playvideosentencelist);

        //Which video should be played out of the raw folder?
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.explode);
        videoview.setVideoURI(uri);

        //Make the videoview box invisible
        videoview.setVisibility(View.INVISIBLE);


        dataSource = new GlaubenssatzeMemoDataSource(this);

        //show all Entries in the list view on this page
        dataSource.open();
        showAllEntries();
        initializeContextualActionBar();

        //sentencelist shows all of the negtative sentences
        //click on the negative sentence to get to the positive one
        sentenceList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView,View view ,int i , long l){
            //Get the index of the clickable sentence
            int position = i;
            //Get Pairnumber of negative index
            List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();
            GlaubenssaetzeMemo item = glaubenssaetzeMemoList.get(i);
            setCurrentPairNumber(item.getPairnumber());
            final long Pairnumber = item.getPairnumber();
            //Give the pairnumber over the the next activty

            //Make the videoviewbox visible
            videoview.setVisibility(View.VISIBLE);

            sentenceList.setVisibility(View.INVISIBLE);


            //Video wird abgespielt
            videoview.start();



            //go to the next activity when the video is finished
            videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent intent = new Intent(Sentence_List.this , Sentence_List_Positiv.class);
                    //Show the positive sentences there
                    intent.putExtra(" Pairnumber " , Pairnumber);
                    startActivity(intent);
                }
            });

        }

    });



}//End onCreate


private void showAllEntries() {

        //all entries from the db were written into the List "glaubenssaetzeMemoList"

        List<GlaubenssaetzeMemo> glaubenssaetzeMemoList = dataSource.getAllGlaubenssaetzeMemos();

        ViewGroup.LayoutParams layoutParams;

        //Change layout android.R.layout.simple_list_item_multiple_choice
        ArrayAdapter<GlaubenssaetzeMemo> glaubenssaetzeMemoArrayAdapter = new ArrayAdapter<GlaubenssaetzeMemo>(
                this,
                R.layout.custom_item_list_adapter,
                glaubenssaetzeMemoList);


        ListView sentenceList = (ListView) findViewById(R.id.sentencelist);

        sentenceList.setAdapter(glaubenssaetzeMemoArrayAdapter);

}//End showAllEntries

private void initializeContextualActionBar() {



    final ListView sentencelist = (ListView) findViewById(R.id.sentencelist);
    sentencelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);



    sentencelist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

        int selCount = 0;

        // In dieser Callback-Methode zählen wir die ausgewählen Listeneinträge mit
        // und fordern ein Aktualisieren der Contextual Action Bar mit invalidate() an

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if (checked){selCount++;}
            else{selCount--;}
            String cabTitle = selCount + " " + getString(R.string.cab_checked_string);
            mode.setTitle(cabTitle);
            mode.invalidate();
        }

        // In dieser Callback-Methode legen wir die CAB-Menüeinträge an
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_contextual_action_bar, menu);
            return true;
        }

        // In dieser Callback-Methode reagieren wir auf den invalidate() Aufruf
        // Wir lassen das Edit-Symbol verschwinden, wenn mehr als 1 Eintrag ausgewählt ist
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            MenuItem item = menu.findItem(R.id.cab_change);
            if (selCount == 1) {
                item.setVisible(true);
            } else {
                item.setVisible(false);
            }

            return true;

        }



        // In dieser Callback-Methode reagieren wir auf Action Item-Klicks
        // Je nachdem ob das Löschen- oder Ändern-Symbol angeklickt wurde
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            boolean returnValue = true;
            SparseBooleanArray touchedGlaubenssaetzeMemosPosition = sentencelist.getCheckedItemPositions();

            switch (item.getItemId()) {

                //user wants to delete an entry
                case R.id.cab_delete:
                    SparseBooleanArray touchedGlaubenssaetzeMemosPositions = sentencelist.getCheckedItemPositions();
                    for (int i = 0; i < touchedGlaubenssaetzeMemosPositions.size(); i++) {
                        boolean isChecked = touchedGlaubenssaetzeMemosPositions.valueAt(i);
                        if (isChecked) {
                            int postitionInListView = touchedGlaubenssaetzeMemosPositions.keyAt(i);
                            GlaubenssaetzeMemo glaubenssaetzeMemo = (GlaubenssaetzeMemo) sentencelist.getItemAtPosition(postitionInListView);
                            Log.d(LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + glaubenssaetzeMemo.toString());
                            dataSource.deleteGlaubenssaetzeMemo(glaubenssaetzeMemo);
                            }
                    }

                    showAllEntries();
                    mode.finish();
                    break;
                 // user wants to change an entry
                case R.id.cab_change:
                    Log.d(LOG_TAG, "Eintrag ändern");
                    for (int i = 0; i < touchedGlaubenssaetzeMemosPosition.size(); i++) {
                        boolean isChecked = touchedGlaubenssaetzeMemosPosition.valueAt(i);
                        if (isChecked) {
                            int postitionInListView = touchedGlaubenssaetzeMemosPosition.keyAt(i);
                            GlaubenssaetzeMemo glaubenssaetzeMemo = (GlaubenssaetzeMemo) sentencelist.getItemAtPosition(postitionInListView);
                            Log.d(LOG_TAG, "Position im ListView: " + postitionInListView + " Inhalt: " + glaubenssaetzeMemo.toString());

                            AlertDialog editGlaubenssaetzeMemoDialog = createEditShoppingMemoDialog(glaubenssaetzeMemo);
                            editGlaubenssaetzeMemoDialog.show();
                        }
                    }

                    mode.finish();
                    break;

                default:
                        returnValue = false;
                        break;
                }

                return returnValue;
            }

        // In dieser Callback-Methode reagieren wir auf das Schließen der CAB
        // Wir setzen den Zähler auf 0 zurück

        @Override
            public void onDestroyActionMode(ActionMode mode) {
            selCount = 0 ;

            }
        });
    }

    private AlertDialog createEditShoppingMemoDialog(final GlaubenssaetzeMemo glaubenssaetzeMemo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogsView = inflater.inflate(R.layout.dialog_edit_glaubenssaetze_memo, null);

        final EditText editTextNewSentence = (EditText) dialogsView.findViewById(R.id.editText_new_quantity);
        editTextNewSentence.setText(String.valueOf(glaubenssaetzeMemo.getSentence()));

        builder.setView(dialogsView)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String sentence = editTextNewSentence.getText().toString();

                        if ((TextUtils.isEmpty(sentence))) {
                            Log.d(LOG_TAG, "Ein Eintrag enthielt keinen Text. Daher Abbruch der Änderung.");
                            return;
                        }


                        // An dieser Stelle schreiben wir die geänderten Daten in die SQLite Datenbank
                        GlaubenssaetzeMemo updatedGlaubenssaetzeMemo = dataSource.updateGlaubenssatzeMemo(glaubenssaetzeMemo.getId(), sentence);

                        Log.d(LOG_TAG, "Alter Eintrag - ID: " + glaubenssaetzeMemo.getId() + " Inhalt: " + glaubenssaetzeMemo.toString());
                        Log.d(LOG_TAG, "Neuer Eintrag - ID: " + updatedGlaubenssaetzeMemo.getId() + " Inhalt: " + updatedGlaubenssaetzeMemo.toString());

                        showAllEntries();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    public long getCurrentPairNumber() {
        return currentPairNumber;
    }

    public void setCurrentPairNumber(long currentPairNumber) {
        this.currentPairNumber = currentPairNumber;
    }


}


