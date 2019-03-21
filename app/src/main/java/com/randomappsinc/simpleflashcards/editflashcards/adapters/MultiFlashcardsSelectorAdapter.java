package com.randomappsinc.simpleflashcards.editflashcards.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;
import com.randomappsinc.simpleflashcards.theme.ThemedTextView;
import com.randomappsinc.simpleflashcards.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultiFlashcardsSelectorAdapter
        extends RecyclerView.Adapter<MultiFlashcardsSelectorAdapter.FlashcardViewHolder>{

    public interface Listener {
        void onNumSelectedSetsUpdated(int numSelectedFlashcards);
    }

    protected List<Flashcard> flashcards;
    protected Set<Integer> selectedFlashcardIds = new HashSet<>();
    protected Listener listener;

    public MultiFlashcardsSelectorAdapter(Listener listener, List<Flashcard> flashcards) {
        this.listener = listener;
        this.flashcards = flashcards;
    }

    public Set<Integer> getSelectedFlashcardIds() {
        return selectedFlashcardIds;
    }

    public void selectAll() {
        selectedFlashcardIds.clear();
        for (Flashcard flashcard : flashcards) {
            selectedFlashcardIds.add(flashcard.getId());
        }
        notifyDataSetChanged();
        listener.onNumSelectedSetsUpdated(selectedFlashcardIds.size());
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

        @BindView(R.id.term_text) ThemedTextView termText;
        @BindView(R.id.term_image) ImageView termImage;
        @BindView(R.id.definition_text) ThemedTextView definitionText;
        @BindView(R.id.definition_image) ImageView definitionImage;
        @BindView(R.id.flashcard_selected_toggle) CheckBox flashcardToggle;

        FlashcardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcard(int position) {
            Flashcard flashcard = flashcards.get(position);

            String term = flashcard.getTerm();
            if (TextUtils.isEmpty(term)) {
                termText.setTextAsHint(R.string.no_term);
            } else {
                termText.setTextNormally(term);
            }

            String termImageUrl = flashcard.getTermImageUrl();
            if (TextUtils.isEmpty(termImageUrl)) {
                termImage.setVisibility(View.GONE);
            } else {
                termImage.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(termImageUrl)
                        .fit()
                        .centerCrop()
                        .into(termImage);
            }

            String definition = flashcard.getDefinition();
            if (TextUtils.isEmpty(definition)) {
                definitionText.setTextAsHint(R.string.no_description);
            } else {
                definitionText.setTextNormally(definition);
            }

            String definitionImageUrl = flashcard.getDefinitionImageUrl();
            if (TextUtils.isEmpty(definitionImageUrl)) {
                definitionImage.setVisibility(View.GONE);
            } else {
                definitionImage.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(definitionImageUrl)
                        .fit()
                        .centerCrop()
                        .into(definitionImage);
            }

            UIUtils.setCheckedImmediately(flashcardToggle, selectedFlashcardIds.contains(flashcard.getId()));
        }

        @OnClick(R.id.flashcard_for_choosing_parent)
        public void onSetCellClick() {
            int setId = flashcards.get(getAdapterPosition()).getId();
            if (selectedFlashcardIds.contains(setId)) {
                selectedFlashcardIds.remove(setId);
                flashcardToggle.setChecked(false);
            } else {
                selectedFlashcardIds.add(setId);
                flashcardToggle.setChecked(true);
            }
            listener.onNumSelectedSetsUpdated(selectedFlashcardIds.size());
        }

        @OnClick(R.id.flashcard_selected_toggle)
        public void onSetSelection() {
            int setId = flashcards.get(getAdapterPosition()).getId();
            if (selectedFlashcardIds.contains(setId)) {
                selectedFlashcardIds.remove(setId);
            } else {
                selectedFlashcardIds.add(setId);
            }
            listener.onNumSelectedSetsUpdated(selectedFlashcardIds.size());
        }
    }
}
