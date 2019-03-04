package com.randomappsinc.simpleflashcards.editflashcards.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;
import com.randomappsinc.simpleflashcards.theme.ThemedTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        @BindView(R.id.term_text) ThemedTextView termText;
        @BindView(R.id.term_image) ImageView termImage;
        @BindView(R.id.definition_text) ThemedTextView definitionText;
        @BindView(R.id.definition_image) ImageView definitionImage;

        FlashcardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcard(int position) {
            Flashcard flashcard = flashcards.get(position);

            String term = flashcard.getTerm();
            if (TextUtils.isEmpty(term)) {
                termText.setTextAsHint(R.string.no_term_hint);
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
                definitionText.setTextAsHint(R.string.no_definition_hint);
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
        }
    }
}
