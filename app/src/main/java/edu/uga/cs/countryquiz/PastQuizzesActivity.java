package edu.uga.cs.countryquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PastQuizzesActivity extends AppCompatActivity {

    private static final String TAG = "PastQuizzesActivity";

    Context context = this;

    private QuizData quizData = null;
    private List<Quiz> allQuizzesList;

    private RecyclerView recyclerView;
    private PastQuizzesRecyclerAdapter recyclerAdapter;

    // This is an AsyncTask class (it extends AsyncTask) to perform DB reading of quizzes, asynchronously.
    private class QuizDBReader extends AsyncTask<Void, List<Quiz>> {
        // This method will run as a background process to read from db.
        // It returns a list of retrieved Quiz objects.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreate callback (the job leads review activity is started).
        @Override
        protected List<Quiz> doInBackground( Void... params ) {
            List<Quiz> allQuizzesList = quizData.retrieveAllQuizzes();

            Log.d( TAG, "QuizDBReader: Quizzes retrieved: " + allQuizzesList.size() );

            return allQuizzesList;
        }

        // This method will be automatically called by Android once the db reading
        // background process is finished.  It will then create and set an adapter to provide
        // values for the RecyclerView.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( List<Quiz> quizzesList ) {
            Log.d( TAG, "QuizDBReader: quizzesList.size(): " + quizzesList.size() );
            allQuizzesList.addAll( quizzesList );

            // create the RecyclerAdapter and set it for the RecyclerView
            Log.d( TAG, "QuizDBReader: allQuizzesList: " + allQuizzesList );
            recyclerAdapter = new PastQuizzesRecyclerAdapter( context, allQuizzesList );
            recyclerView.setAdapter( recyclerAdapter );
            recyclerAdapter.sync();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_quizzes);

        allQuizzesList = new ArrayList<Quiz>();

        // Create a JobLeadsData instance, since we will need to save a new JobLead to the dn.
        // Note that even though more activites may create their own instances of the JobLeadsData
        // class, we will be using a single instance of the JobLeadsDBHelper object, since
        // that class is a singleton class.
        quizData = new QuizData( this );

        // Open that database for reading of the full list of job leads.
        // Note that onResume() hasn't been called yet, so the db open in it
        // was not called yet!
        quizData.open();

        // Execute the retrieval of the job leads in an asynchronous way,
        // without blocking the main UI thread.
        recyclerView = findViewById( R.id.recyclerView );
        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        new QuizDBReader().execute();

    }

    @Override
    public void onResume() {
        super.onResume();
        // open the database in onResume
        if( quizData != null )
            quizData.open();
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