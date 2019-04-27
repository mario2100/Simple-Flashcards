package com.randomappsinc.simpleflashcards.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;
import com.randomappsinc.simpleflashcards.theme.ThemedTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomepageFlashcardSetsAdapter
        extends RecyclerView.Adapter<HomepageFlashcardSetsAdapter.FlashcardSetViewHolder>
        implements ThemeManager.Listener {

    public interface Listener {
        void onContentUpdated(int numSets);

        void onFlashcardSetClicked(FlashcardSetDO flashcardSetDO);
    }

    @NonNull protected Listener listener;
    private Context context;
    protected List<FlashcardSetDO> flashcardSets;
    private ThemeManager themeManager = ThemeManager.get();

    public HomepageFlashcardSetsAdapter(@NonNull Listener listener, Context context) {
        this.listener = listener;
        this.context = context;
        this.flashcardSets = new ArrayList<>();
        this.themeManager.registerListener(this);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        notifyDataSetChanged();
    }

    public void cleanup() {
        themeManager.unregisterListener(this);
    }

    public void refreshContent(String searchTerm) {
        flashcardSets.clear();
        flashcardSets.addAll(DatabaseManager.get().getFlashcardSets(searchTerm));
        notifyDataSetChanged();
        listener.onContentUpdated(getItemCount());
    }

    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(
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
            adjustForDarkMode();
        }

        void adjustForDarkMode() {
            setName.setProperTextColor();
            numFlashcardsText.setProperTextColor();
        }

        @OnClick(R.id.set_cell_parent)
        public void onSetClicked() {
            listener.onFlashcardSetClicked(flashcardSets.get(getAdapterPosition()));
        }
    }
}
