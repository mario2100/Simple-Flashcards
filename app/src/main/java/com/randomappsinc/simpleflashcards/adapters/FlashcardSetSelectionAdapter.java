package com.randomappsinc.simpleflashcards.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/** Adapter for rendering a list of flashcard sets the user can add to a folder */
public class FlashcardSetSelectionAdapter
        extends RecyclerView.Adapter<FlashcardSetSelectionAdapter.FlashcardSetViewHolder> {

    protected List<FlashcardSet> flashcardSets = new ArrayList<>();

    public void setFlashcardSets(List<FlashcardSet> newSets) {
        flashcardSets.clear();
        flashcardSets.addAll(newSets);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.flashcard_set_for_folder_cell,
                parent,
                false);
        return new FlashcardSetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardSetViewHolder holder, int position) {
        holder.loadFlashcardSet(position);
    }

    @Override
    public int getItemCount() {
        return flashcardSets.size();
    }

    public class FlashcardSetViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.set_name) TextView setName;
        @BindView(R.id.num_flashcards) TextView numFlashcards;

        FlashcardSetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcardSet(int position) {
            FlashcardSet flashcardSet = flashcardSets.get(position);
            setName.setText(flashcardSet.getName());
            Context context = numFlashcards.getContext();
            numFlashcards.setText(flashcardSet.getFlashcards().size() == 1
                    ? context.getString(R.string.one_flashcard_set)
                    : context.getString(R.string.x_flashcard_sets, flashcardSet.getFlashcards().size()));
        }
    }
}
