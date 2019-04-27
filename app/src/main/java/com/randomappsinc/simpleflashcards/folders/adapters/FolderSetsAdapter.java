package com.randomappsinc.simpleflashcards.folders.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;
import com.randomappsinc.simpleflashcards.theme.ThemedTextView;

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
        void onFlashcardSetClicked(FlashcardSetDO flashcardSetDO);

        void removeFlashcardSet(FlashcardSetDO flashcardSetDO);
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
                R.layout.folder_set_cell,
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

        @BindView(R.id.flashcard_set_name) ThemedTextView setName;
        @BindView(R.id.num_flashcards) ThemedTextView numFlashcardsText;

        FlashcardSetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcardSet(int position) {
            FlashcardSetDO flashcardSet = flashcardSets.get(position);
            setName.setText(flashcardSet.getName());
            int numFlashcards = flashcardSet.getFlashcards().size();
            if (numFlashcards == 1) {
                numFlashcardsText.setText(R.string.one_flashcard);
            } else {
                numFlashcardsText.setText(setName.getContext().getString(R.string.x_flashcards, numFlashcards));
            }
        }

        @OnClick(R.id.set_cell_parent)
        public void onSetClicked() {
            listener.onFlashcardSetClicked(flashcardSets.get(getAdapterPosition()));
        }

        @OnClick(R.id.remove)
        public void onSetRemoved() {
            FlashcardSetDO flashcardSetDO = flashcardSets.get(getAdapterPosition());
            flashcardSets.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            listener.removeFlashcardSet(flashcardSetDO);
        }
    }
}
