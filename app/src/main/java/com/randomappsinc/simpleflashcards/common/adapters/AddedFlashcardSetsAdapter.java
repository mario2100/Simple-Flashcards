package com.randomappsinc.simpleflashcards.common.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.models.FlashcardSetPreview;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddedFlashcardSetsAdapter
        extends RecyclerView.Adapter<AddedFlashcardSetsAdapter.FlashcardSetViewHolder>{

    public interface Listener {
        void onCellClicked(FlashcardSetPreview setPreview);
    }

    protected Listener listener;
    protected List<FlashcardSetPreview> setPreviews;

    public AddedFlashcardSetsAdapter(Listener listener, List<FlashcardSetPreview> setPreviews) {
        this.listener = listener;
        this.setPreviews = setPreviews;
    }

    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.simple_flashcard_set_cell,
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
        return setPreviews.size();
    }

    class FlashcardSetViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.set_name) TextView setName;
        @BindView(R.id.num_flashcards) TextView numFlashcardsText;

        @BindString(R.string.one_flashcard) String oneFlashcard;
        @BindString(R.string.x_flashcards) String manyFlashcardsTemplate;

        FlashcardSetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcardSet(int position) {
            FlashcardSetPreview setPreview = setPreviews.get(position);
            setName.setText(setPreview.getSetName());
            int numFlashcards = setPreview.getNumCards();
            numFlashcardsText.setText(numFlashcards == 1
                    ? oneFlashcard
                    : String.format(manyFlashcardsTemplate, numFlashcards));
        }

        @OnClick(R.id.set_preview_parent)
        public void onCellClicked() {
            FlashcardSetPreview preview = setPreviews.get(getAdapterPosition());
            listener.onCellClicked(preview);
        }
    }
}
