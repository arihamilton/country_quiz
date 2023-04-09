package edu.uga.cs.countryquiz;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class (a POJO) represents a single quiz, including the id, date,
 * and result.
 * The id is -1 if the object has not been persisted in the database yet, and
 * the db table's primary key value, if it has been persisted.
 */
public class Quiz implements Parcelable {

    private long id;
    private String date;
    private int result;

    private Question[] questions;

    private int[] correctSoFar = {0, 0, 0, 0, 0, 0};

    private int lastAnswered = -1;

    public Quiz() {
        this.id = -1;
        this.date = null;
        this.result = 0;
        this.questions = null;
    }

    public Quiz(String date, int result) {
        this.id = -1;  // the primary key id will be set by a setter method
        this.date = date;
        this.result = result;
        this.questions = null;  // will be set by a setter method
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public int[] getCorrectSoFar() {
        return correctSoFar;
    }

    public void setCorrectSoFar(int index, boolean correct) {
        correctSoFar[index] = correct ? 1 : 0;
    }

    public int getLastAnswered() {
        return lastAnswered;
    }

    public void setLastAnswered(int lastAnswered) { this.lastAnswered = lastAnswered; }

    public String toString() {
        return id + ": " + date + " - " + result;
    }

    // Parcelable Methods -- used to save instance state
    // Source: https://stackoverflow.com/questions/3172333/how-to-save-an-instance-of-a-custom-class-in-onsaveinstancestate
    private int mData;

    public int describeContents() {
        return 0;
    }

    // save object in parcel
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    public static final Parcelable.Creator<Quiz> CREATOR
            = new Parcelable.Creator<Quiz>() {
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    /**
     * recreate object from parcel
     */
    private Quiz(Parcel in) {
        mData = in.readInt();
    }


}
