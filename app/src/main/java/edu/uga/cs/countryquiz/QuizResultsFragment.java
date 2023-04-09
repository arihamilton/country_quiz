package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizResultsFragment extends Fragment {

    private static final String TAG = "QuizResultsFragment";

    // Access main quiz
    public Quiz mainQuiz;

    private QuizData quizData = null;

    private Button buttonStartNew2;
    private Button buttonPastQuizzes2;

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a quiz, asynchronously.
    public class QuizDBWriter extends AsyncTask<Quiz, Quiz> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onClick listener of the Save button.
        @Override
        protected Quiz doInBackground( Quiz... quizzes ) {
            quizData.storeQuiz( quizzes[0] );
            return quizzes[0];
        }

        // This method will be automatically called by Android once the writing to the database
        // in a background process has finished.  Note that doInBackground returns a Quiz object.
        // That object will be passed as argument to onPostExecute.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( Quiz quiz ) {
            Log.d( TAG, "Quiz saved: " + quiz );
        }
    }

    public QuizResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuizResultsFragment.
     */
    public static QuizResultsFragment newInstance() {
        QuizResultsFragment fragment = new QuizResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d( TAG, "onCreate()");

        // Create a QuizData instance, since we will need to save a new Quiz in a file.
        quizData = new QuizData( getActivity() );
        quizData.open();

        mainQuiz = ((PlayQuizActivity)getActivity()).mainQuiz;

        // Set quiz results
        int results = sum(mainQuiz.getCorrectSoFar());
        mainQuiz.setResult(results);

        // Set quiz date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        mainQuiz.setDate(sdf.format(cal.getTime()));

        // Save quiz to DB
        // Store this new job lead in the database asynchronously,
        // without blocking the UI thread.
        new QuizDBWriter().execute( mainQuiz );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_results, container, false);
    }

    private int sum(int[] arr) {
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            total = total + arr[i];
        }
        return total;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState ) {

        Log.d( TAG, "onViewCreated()");

        super.onViewCreated( view, savedInstanceState );

        TextView textViewResults = view.findViewById(R.id.textViewResults);
        textViewResults.setText("" + mainQuiz.getResult() + "/6");

        buttonStartNew2 = view.findViewById(R.id.buttonStartNew2);
        // Set Start New Quiz button listener
        buttonStartNew2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new
                        Intent(view.getContext(),
                        PlayQuizActivity.class);
                startActivity(intent);

            }

        });

        buttonPastQuizzes2 = view.findViewById(R.id.buttonPastQuizzes2);
        // Set View Past Quizzes button listener
        buttonPastQuizzes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new
                        Intent(view.getContext(),
                        PastQuizzesActivity.class);
                startActivity(intent);

            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // open the database in onResume
        if( quizData != null )
            quizData.open();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle( getResources().getString( R.string.app_name ) );
    }

    // We need to save quizzes into a file as the activity stops being a foreground activity
    @Override
    public void onPause() {
        super.onPause();
        // close the database in onPause
        if( quizData != null )
            quizData.close();
    }


}