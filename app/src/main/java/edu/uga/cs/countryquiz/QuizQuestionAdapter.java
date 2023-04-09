package edu.uga.cs.countryquiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuizQuestionAdapter extends FragmentStateAdapter {

    Question[] questions;

    public QuizQuestionAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, Question[] questions) {
        super(fragmentManager, lifecycle);
        this.questions = questions;
    }

    @Override
    public Fragment createFragment(int questionNum) {

        if (questionNum == 6) {
            Fragment fragment = QuizResultsFragment.newInstance();
            return fragment;
        }
        else {
            Fragment fragment = QuizQuestionFragment.newInstance(questionNum);
            Bundle args = new Bundle();
            args.putInt("questionNum", questionNum);
            args.putString("country", questions[questionNum].getCountry());
            args.putString("correctContinent", questions[questionNum].getCorrectContinent());
            args.putString("wrongContinent1", questions[questionNum].getWrongContinent1());
            args.putString("wrongContinent2", questions[questionNum].getWrongContinent2());
            fragment.setArguments(args);
            return fragment;
        }
    }

    @Override
    public int getItemCount() {
        return QuizQuestionFragment.getNumberOfQuestions();
    }

}
