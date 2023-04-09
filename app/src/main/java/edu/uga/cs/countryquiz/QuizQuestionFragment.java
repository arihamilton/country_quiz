package edu.uga.cs.countryquiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizQuestionFragment extends Fragment {

    private static final String TAG = "QuizQuestionFragment";

    // which question to display in the fragment
    private int questionNum;
    private String country;
    private String correctContinent;
    private String wrongContinent1;
    private String wrongContinent2;

    private int[] answerOrder = {0, 1, 2};
    private String[] answers = new String[3];

    // Access main quiz
    public Quiz mainQuiz;

    private void shuffleArray(int[] arr) {

        Random ran = new Random();

        // Returns number between 0-3(exclusive)
        arr[0] = (ran.nextInt(3));

        arr[1] = (ran.nextInt(3));
        // Ensure no duplicate values
        while (arr[1] == arr[0]) {
            arr[1] = (ran.nextInt(3));
        }

        arr[2] = (ran.nextInt(3));
        // Ensure no duplicate values
        while ((arr[2] == arr[0]) || (arr[2] == arr[1])) {
            arr[2] = (ran.nextInt(3));
        }

    }

    public QuizQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param questionNum Parameter 1.
     * @return A new instance of fragment QuizQuestionFragment.
     */
    public static QuizQuestionFragment newInstance(int questionNum) {
        QuizQuestionFragment fragment = new QuizQuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionNum = getArguments().getInt("questionNum");
            country = getArguments().getString("country");
            correctContinent = getArguments().getString("correctContinent");
            wrongContinent1 = getArguments().getString("wrongContinent1");
            wrongContinent2 = getArguments().getString("wrongContinent2");
            answers[0] = correctContinent;
            answers[1] = wrongContinent1;
            answers[2] = wrongContinent2;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainQuiz = ((PlayQuizActivity) getActivity()).mainQuiz;

        // Set lastAnswered even if the user did not select a value
        // for the last question
        mainQuiz.setLastAnswered(questionNum-1);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_question, container, false);
    }

    private int sum(int[] arr) {
        int total = 0;
        for (int i = 0; i < arr.length; i++) {
            total = total + arr[i];
        }
        return total;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //public void onActivityCreated(Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewQuestion = view.findViewById(R.id.textViewQuestion);

        TextView answerButton1 = view.findViewById(R.id.answerButton1);
        TextView answerButton2 = view.findViewById(R.id.answerButton2);
        TextView answerButton3 = view.findViewById(R.id.answerButton3);

        shuffleArray(answerOrder);

        textViewQuestion.setText("Name the continent on which " + country + " is located");

        answerButton1.setText("A) " + answers[answerOrder[0]]);
        answerButton2.setText("B) " + answers[answerOrder[1]]);
        answerButton3.setText("C) " + answers[answerOrder[2]]);

        // Set radio button listeners
        answerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((answers[answerOrder[0]]).equals(correctContinent)) {
                    mainQuiz.setCorrectSoFar(questionNum, true);
                    int results = sum(mainQuiz.getCorrectSoFar());
                    mainQuiz.setResult(results);
                    Log.d( TAG, "CORRECT ANSWER SELECTED: " + correctContinent );
                }
                else {
                    mainQuiz.setCorrectSoFar(questionNum, false);
                    int results = sum(mainQuiz.getCorrectSoFar());
                    mainQuiz.setResult(results);
                }
                mainQuiz.setLastAnswered(questionNum);
            }

        });

        answerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((answers[answerOrder[1]]).equals(correctContinent)) {
                    mainQuiz.setCorrectSoFar(questionNum, true);
                    int results = sum(mainQuiz.getCorrectSoFar());
                    mainQuiz.setResult(results);
                    Log.d( TAG, "CORRECT ANSWER SELECTED: " + correctContinent );
                }
                else {
                    mainQuiz.setCorrectSoFar(questionNum, false);
                    int results = sum(mainQuiz.getCorrectSoFar());
                    mainQuiz.setResult(results);
                }
                mainQuiz.setLastAnswered(questionNum);
            }

        });

        answerButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((answers[answerOrder[2]]).equals(correctContinent)) {
                    mainQuiz.setCorrectSoFar(questionNum, true);
                    int results = sum(mainQuiz.getCorrectSoFar());
                    mainQuiz.setResult(results);
                    Log.d( TAG, "CORRECT ANSWER SELECTED: " + correctContinent );
                }
                else {
                    mainQuiz.setCorrectSoFar(questionNum, false);
                    int results = sum(mainQuiz.getCorrectSoFar());
                    mainQuiz.setResult(results);
                }
                mainQuiz.setLastAnswered(questionNum);
            }

        });

    }

    public static int getNumberOfQuestions() {
        return 7;
    }

}