package com.randomappsinc.simpleflashcards.editflashcards.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

public class MultiFlashcardsSelectorAdapter
        extends RecyclerView.Adapter<MultiFlashcardsSelectorAdapter.FlashcardViewHolder>{

    public interface Listener {
        void onNumSelectedSetsUpdated(int numSelectedFlashcards);
    }

    protected List<Flashcard> flashcards = new ArrayList<>();
    protected Set<Integer> selectedFlashcardIds = new HashSet<>();
    protected Listener listener;

    public MultiFlashcardsSelectorAdapter(Listener listener, List<Flashcard> flashcards) {
        this.listener = listener;
        this.flashcards = flashcards;
    }

    public List<Integer> getSelectedFlashcardIds() {
        return new ArrayList<>(selectedFlashcardIds);
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.flashcard_for_choosing,
                parent,
                false);
        return new FlashcardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        holder.loadFlashcard(position);
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public class FlashcardViewHolder extends RecyclerView.ViewHolder {

        FlashcardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcard(int position) {
        }
    }
}
