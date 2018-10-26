package fhbielefeld.glaubenssaetze_app;

import android.util.Log;

/**
 * Created by philpetschull on 05.03.18.
 */

public class GlaubenssaetzeMemo {

    //Konstantendeklaration
    private static final String LOG_TAG = GlaubenssaetzeMemoDbHelper.class.getSimpleName();

    /*
        Variablen
    */
    private long id;
    private String sentence;
    private String status;
    private long pairnumber;


    //Konstruktor

    public GlaubenssaetzeMemo(long id, String status, long pairnumber, String sentence) {
        this.id = id;
        this.status = status;
        this.pairnumber = pairnumber;
        this.sentence = sentence;

    }

    /*
        Getter&Setter
    */
    //Sentence
    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    //ID
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String Status) {
        this.status = status;
    }

    //Pairnumber
    public long getPairnumber() {
        return pairnumber;
    }

    public void setPairnumber(long pairnumber) {
        this.pairnumber = pairnumber;
        Log.d(LOG_TAG, "Set Funktion. Die gesettete Paarnummer ist: " + pairnumber);
    }


    /*
        Public-Functions
    */
    public String toString() {
        String output = id + " : " + sentence;
        return output;
    }

}



