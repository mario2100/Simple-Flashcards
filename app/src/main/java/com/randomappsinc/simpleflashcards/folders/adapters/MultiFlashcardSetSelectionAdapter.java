package com.randomappsinc.simpleflashcards.folders.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Adapter for rendering a list of flashcard sets the user can add to a folder */
public class MultiFlashcardSetSelectionAdapter
        extends RecyclerView.Adapter<MultiFlashcardSetSelectionAdapter.FlashcardSetViewHolder> {

    public interface Listener {
        void onNumSelectedSetsUpdated(int numSelectedSets);
    }

    protected List<FlashcardSet> flashcardSets = new ArrayList<>();
    protected Set<FlashcardSet> selectedSets = new HashSet<>();
    protected Listener listener;

    public MultiFlashcardSetSelectionAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setFlashcardSets(List<FlashcardSet> newSets) {
        flashcardSets.clear();
        flashcardSets.addAll(newSets);
        selectedSets.clear();
        notifyDataSetChanged();
        listener.onNumSelectedSetsUpdated(selectedSets.size());
    }

    public List<FlashcardSet> getSelectedSets() {
        return new ArrayList<>(selectedSets);
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
        @BindView(R.id.set_selected_toggle) CheckBox setSelectedToggle;

        FlashcardSetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcardSet(int position) {
            FlashcardSet flashcardSet = flashcardSets.get(position);
            setName.setText(flashcardSet.getName());
            Context context = numFlashcards.getContext();
            numFlashcards.setText(flashcardSet.getFlashcards().size() == 1
                    ? context.getString(R.string.one_flashcard)
                    : context.getString(R.string.x_flashcards, flashcardSet.getFlashcards().size()));
            setSelectedToggle.setChecked(selectedSets.contains(flashcardSet));
        }

        @OnClick(R.id.set_for_folder_parent)
        public void onSetCellClick() {
            FlashcardSet flashcardSet = flashcardSets.get(getAdapterPosition());
            if (selectedSets.contains(flashcardSet)) {
                selectedSets.remove(flashcardSet);
                setSelectedToggle.setChecked(false);
            } else {
                selectedSets.add(flashcardSet);
                setSelectedToggle.setChecked(true);
            }
            listener.onNumSelectedSetsUpdated(selectedSets.size());
        }

        @OnClick(R.id.set_selected_toggle)
        public void onSetSelection() {
            FlashcardSet flashcardSet = flashcardSets.get(getAdapterPosition());
            if (selectedSets.contains(flashcardSet)) {
                selectedSets.remove(flashcardSet);
            } else {
                selectedSets.add(flashcardSet);
            }
            listener.onNumSelectedSetsUpdated(selectedSets.size());
        }
    }
}