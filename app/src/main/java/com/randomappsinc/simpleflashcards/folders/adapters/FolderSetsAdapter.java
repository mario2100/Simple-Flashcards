package com.randomappsinc.simpleflashcards.folders.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Shows the flashcard sets within a folder */
public class FolderSetsAdapter extends RecyclerView.Adapter<FolderSetsAdapter.FlashcardSetViewHolder> {

    public interface Listener {
        void browseFlashcardSet(FlashcardSetDO flashcardSet);

        void takeQuiz(FlashcardSetDO flashcardSet);

        void editFlashcardSet(FlashcardSetDO flashcardSet);

        void removeFlashcardSet(FlashcardSetDO flashcardSet);
    }

    @NonNull protected Listener listener;
    protected List<FlashcardSetDO> flashcardSets = new ArrayList<>();

    public FolderSetsAdapter(@NonNull Listener listener) {
        this.listener = listener;
    }

    public void setFlashcardSets(List<FlashcardSetDO> newSets) {
        flashcardSets.clear();
        flashcardSets.addAll(newSets);
        notifyDataSetChanged();
    }

    public void refreshContent(List<FlashcardSetDO> freshSets) {
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
            FlashcardSetDO flashcardSet = flashcardSets.get(position);
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
            int indexToRemove = getAdapterPosition();
            FlashcardSetDO removedSet = flashcardSets.get(indexToRemove);
            flashcardSets.remove(indexToRemove);
            notifyItemRemoved(indexToRemove);
            listener.removeFlashcardSet(removedSet);
        }
    }
}
