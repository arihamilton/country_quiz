package edu.uga.cs.countryquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Main Activity for the Quiz app.
 * Splash screen. Allows user to view the results of past quizzes or
 * start a new one.
 *
 * @author Arianna Hamilton
 */
public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private Button buttonPastQuizzes;

    /**
     * Initializes the current activity.
     *
     * @param savedInstanceState data storage
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = findViewById(R.id.buttonStart);
        // set the start Button's listener as a Lambda
        buttonStart.setOnClickListener((view) -> {

                    Intent intent = new
                            Intent(view.getContext(),
                            PlayQuizActivity.class);
                    startActivity(intent);

                }
        );

        buttonPastQuizzes = findViewById(R.id.buttonPastQuizzes);
        // set the past quizzes Button's listener as a Lambda
        buttonPastQuizzes.setOnClickListener((view) -> {

                    Intent intent = new
                            Intent(view.getContext(),
                            PastQuizzesActivity.class);
                    startActivity(intent);

                }
        );

    }
}