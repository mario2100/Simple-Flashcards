package com.randomappsinc.simpleflashcards.adapters;

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
import butterknife.OnClick;

/** Shows the flashcard sets within a folder */
public class FolderSetsAdapter extends RecyclerView.Adapter<FolderSetsAdapter.FlashcardSetViewHolder> {

    public interface Listener {
        void browseFlashcardSet(FlashcardSet flashcardSet);

        void takeQuiz(FlashcardSet flashcardSet);

        void editFlashcardSet(FlashcardSet flashcardSet);

        void removeFlashcardSet(FlashcardSet flashcardSet);
    }

    @NonNull protected Listener listener;
    protected List<FlashcardSet> flashcardSets = new ArrayList<>();
    protected int selectedItemIndex = -1;

    public FolderSetsAdapter(@NonNull Listener listener) {
        this.listener = listener;
    }

    public void setFlashcardSets(List<FlashcardSet> newSets) {
        flashcardSets.clear();
        flashcardSets.addAll(newSets);
        notifyDataSetChanged();
    }

    public void refreshContent(List<FlashcardSet> freshSets) {
        flashcardSets.clear();
        flashcardSets.addAll(freshSets);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.flashcard_set_cell,
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
        @BindView(R.id.delete_button_text) TextView deleteButtonText;

        FlashcardSetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            deleteButtonText.setText(R.string.remove);
        }

        void loadFlashcardSet(int position) {
            FlashcardSet flashcardSet = flashcardSets.get(position);
            setName.setText(flashcardSet.getName());
            numFlashcards.setText(String.valueOf(flashcardSet.getFlashcards().size()));
        }

        @OnClick(R.id.browse_button)
        public void browseFlashcards() {
            listener.browseFlashcardSet(flashcardSets.get(getAdapterPosition()));
        }

        @OnClick(R.id.quiz_button)
        public void takeQuiz() {
            listener.takeQuiz(flashcardSets.get(getAdapterPosition()));
        }

        @OnClick(R.id.edit_button)
        public void editFlashcardSet() {
            listener.editFlashcardSet(flashcardSets.get(getAdapterPosition()));
        }

        @OnClick(R.id.delete_button)
        public void deleteFlashcardSet() {
            selectedItemIndex = getAdapterPosition();
            listener.removeFlashcardSet(flashcardSets.get(getAdapterPosition()));
        }
    }
}
