package edu.uga.cs.countryquiz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all past quizzes.
 */
public class PastQuizzesRecyclerAdapter
        extends RecyclerView.Adapter<PastQuizzesRecyclerAdapter.QuizHolder> {

    public static final String DEBUG_TAG = "PastQuizzesAdapter";

    private final Context context;

    private List<Quiz> values;
    private List<Quiz> originalValues;

    public PastQuizzesRecyclerAdapter(Context context, List<Quiz> pastQuizList) {
        this.context = context;
        this.values = pastQuizList;
        this.originalValues = new ArrayList<Quiz>(pastQuizList);
    }

    // reset the originalValues to the current contents of values
    public void sync() {
        originalValues = new ArrayList<Quiz>(values);
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    public static class QuizHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView result;

        public QuizHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            result = itemView.findViewById(R.id.result);
        }
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // We need to make sure that all CardViews have the same, full width, allowed by the parent view.
        // This is a bit tricky, and we must provide the parent reference (the second param of inflate)
        // and false as the third parameter (don't attach to root).
        // Consequently, the parent view's (the RecyclerView) width will be used (match_parent).
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz, parent, false);
        return new QuizHolder(view);
    }

    // This method fills in the values of a holder to show a Quiz.
    // The position parameter indicates the position on the list of quizzes.
    @Override
    public void onBindViewHolder(QuizHolder holder, int position) {

        Log.d( DEBUG_TAG, "values: " + values );

        Quiz quiz = values.get(position);

        holder.date.setText(quiz.getDate());
        Log.d( DEBUG_TAG, "result: " + quiz.getResult() );
        holder.result.setText("" + quiz.getResult() + "/6");
    }

    @Override
    public int getItemCount() {
        if (values != null)
            return values.size();
        else
            return 0;
    }

}
