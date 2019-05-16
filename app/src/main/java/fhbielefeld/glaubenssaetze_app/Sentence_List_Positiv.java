package fhbielefeld.glaubenssaetze_app;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    private long currentPairNumber;



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
        currentPairNumber = Pairnumber;

        dataSource.open();
        Log.d(LOG_TAG, "Die erhaltene Paarnummer ist: " +  Pairnumber);
        showAllPositiveEntries(Pairnumber);
        dataSource.close();

        initializeContextualActionBar();

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

    private void initializeContextualActionBar() {



        final ListView sentencelist = (ListView) findViewById(R.id.sentencelist__positiv);
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
                dataSource.open();
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

                        showAllPositiveEntries(currentPairNumber);
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
        dataSource.open();
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

                        showAllPositiveEntries(currentPairNumber);
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


