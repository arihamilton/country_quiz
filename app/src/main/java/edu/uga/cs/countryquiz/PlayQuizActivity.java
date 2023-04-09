package edu.uga.cs.countryquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayQuizActivity extends AppCompatActivity {

    private static final String TAG = "PlayQuizActivity";

    private CountryData countryData = null;
    private List<Country> allCountriesList;

    public Quiz mainQuiz = new Quiz();

    Quiz savedQuiz = null;

    Random ran = new Random();
    private int[] countryOrder = {1, 2, 3, 4, 5, 6};

    private String[] allContinents = {
            "Asia",
            "Europe",
            "Africa",
            "North America",
            "South America",
            "Oceania",
    };


    // This is an AsyncTask class (it extends AsyncTask) to perform DB reading of countries, asynchronously.
    private class CountryDBReader extends AsyncTask<Void, List<Country>> {
        // This method will run as a background process to read from db.
        // It returns a list of retrieved JobLead objects.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreate callback (the job leads review activity is started).
        @Override
        protected List<Country> doInBackground( Void... params ) {
            List<Country> allCountriesList = countryData.retrieveAllCountries();

            Log.d( TAG, "CountryDBReader: Countries retrieved: " + allCountriesList.size() );

            return allCountriesList;
        }

        // This method will be automatically called by Android once the db reading
        // background process is finished.  It will then create and set an adapter to provide
        // values for the RecyclerView.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( List<Country> countriesList ) {
            Log.d( TAG, "CountryDBReader: countriesList.size(): " + countriesList.size() );
            allCountriesList.addAll( countriesList );

            // Create quiz questions
            Question[] questions;

            // restore the saved questions if available
            if (savedQuiz != null) {
                questions = savedQuiz.getQuestions();
                mainQuiz = savedQuiz;
            }
            else {
                questions = createQuizQuestions();
            }
            mainQuiz.setQuestions(questions);

            ViewPager2 pager = findViewById(R.id.viewPager);

            QuizQuestionAdapter avpAdapter = new QuizQuestionAdapter(getSupportFragmentManager(), getLifecycle(), questions);
            pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

            // Create delay to ascertain setCurrentItem() works
            if (pager.getAdapter() != null) { pager.setAdapter(null); }
            pager.setAdapter(avpAdapter);
            Log.d( TAG, "CountryDBReader: Last Answered: " + mainQuiz.getLastAnswered() );
            pager.setCurrentItem(mainQuiz.getLastAnswered()+1);
        }
    }

    private void shuffleArray(int[] arr, int max) {

        // Returns number between 0-max(exclusive)
        arr[0] = (ran.nextInt(max));

        // Ensure no duplicate values -- arr[1]
        arr[1] = (ran.nextInt(max));
        while (arr[1] == arr[0]) {
            arr[1] = (ran.nextInt(max));
        }

        // Ensure no duplicate values -- arr[2]
        arr[2] = (ran.nextInt(max));
        while ((arr[2] == arr[0]) || (arr[2] == arr[1])) {
            arr[2] = (ran.nextInt(max));
        }

        // Ensure no duplicate values -- arr[3]
        arr[3] = (ran.nextInt(max));
        while ((arr[3] == arr[0]) || (arr[3] == arr[1]) || (arr[3] == arr[2])) {
            arr[3] = (ran.nextInt(max));
        }

        // Ensure no duplicate values -- arr[4]
        arr[4] = (ran.nextInt(max));
        while ((arr[4] == arr[0]) || (arr[4] == arr[1]) || (arr[4] == arr[2]) || (arr[4] == arr[3])) {
            arr[4] = (ran.nextInt(max));
        }

        // Ensure no duplicate values -- arr[5]
        arr[5] = (ran.nextInt(max));
        while ((arr[5] == arr[0]) || (arr[5] == arr[1]) || (arr[5] == arr[2]) || (arr[5] == arr[3])) {
            arr[5] = (ran.nextInt(max));
        }

    }

    private Question createQuestion(int index) {

        Question question = new Question();
        question.setCountry(allCountriesList.get(index).getCountry());

        String correctContinent = allCountriesList.get(index).getContinent();
        question.setCorrectContinent(correctContinent);

        int continentIndex1 = (ran.nextInt(6));
        // Ensure wrong answer is not accidentally correct
        while (allContinents[continentIndex1].equals(correctContinent)) {
            continentIndex1 = (ran.nextInt(6));
        }
        question.setWrongContinent1(allContinents[continentIndex1]);

        // Ensure wrong answer is not accidentally correct + no duplicate values
        int continentIndex2 = (ran.nextInt(6));
        while ((continentIndex2 == continentIndex1) || (allContinents[continentIndex2].equals(correctContinent))) {
            continentIndex2 = (ran.nextInt(6));
        }
        question.setWrongContinent2(allContinents[continentIndex2]);

        return question;
    }

    private Question[] createQuizQuestions() {

        Question[] questions = new Question[6];

        shuffleArray(countryOrder, allCountriesList.size());

        // Create Question 1
        Question question1 = createQuestion(countryOrder[0]);
        questions[0] = question1;

        // Create Question 2
        Question question2 = createQuestion(countryOrder[1]);
        questions[1] = question2;

        // Create Question 3
        Question question3 = createQuestion(countryOrder[2]);
        questions[2] = question3;

        // Create Question 4
        Question question4 = createQuestion(countryOrder[3]);
        questions[3] = question4;

        // Create Question 5
        Question question5 = createQuestion(countryOrder[4]);
        questions[4] = question5;

        // Create Question 6
        Question question6 = createQuestion(countryOrder[5]);
        questions[5] = question6;

        return questions;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);

        allCountriesList = new ArrayList<Country>();


        countryData = new CountryData( this );

        // Open that database for reading of the full list of countries.
        // Note that onResume() hasn't been called yet, so the db open in it
        // was not called yet!
        countryData.open();

        // Execute the retrieval of the countries in an asynchronous way,
        // without blocking the main UI thread.
        new CountryDBReader().execute();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Open the database
        if( countryData != null && !countryData.isDBOpen() ) {
            countryData.open();
            Log.d( TAG, "PlayQuizActivity.onResume(): opening DB" );
        }
    }

    // We need to save job leads into a file as the activity stops being a foreground activity
    @Override
    public void onPause() {
        super.onPause();

        // close the database in onPause
        if( countryData != null ) {
            countryData.close();
            Log.d( TAG, "PlayQuizActivity.onPause(): closing DB" );
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        outState.putParcelable("key", mainQuiz);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {

        Log.d( TAG, "PlayQuizActivity.onRestoreInstanceState()" );
        if(outState != null) {
            savedQuiz = outState.getParcelable("key");
        }

    }

}