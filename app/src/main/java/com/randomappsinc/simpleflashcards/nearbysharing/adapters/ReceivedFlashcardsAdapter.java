package com.randomappsinc.simpleflashcards.nearbysharing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.models.FlashcardSetPreview;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceivedFlashcardsAdapter
        extends RecyclerView.Adapter<ReceivedFlashcardsAdapter.FlashcardSetViewHolder>{

    public interface Listener {
        void onCellClicked(FlashcardSetPreview setPreview);
    }

    protected Listener listener;
    protected List<FlashcardSetDO> flashcardSets = new ArrayList<>();

    public ReceivedFlashcardsAdapter(Listener listener) {
        this.listener = listener;
    }

    public void addFlashcardSet(FlashcardSetDO flashcardSet) {
        flashcardSets.add(flashcardSet);
        notifyItemInserted(getItemCount() - 1);
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
        return flashcardSets.size();
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
            FlashcardSetDO flashcardSet = flashcardSets.get(position);
            setName.setText(flashcardSet.getName());
            int numFlashcards = flashcardSet.getFlashcards().size();
            numFlashcardsText.setText(numFlashcards == 1
                    ? oneFlashcard
                    : String.format(manyFlashcardsTemplate, numFlashcards));
        }

        @OnClick(R.id.set_preview_parent)
        public void onCellClicked() {
            FlashcardSetDO flashcardSet = flashcardSets.get(getAdapterPosition());
            listener.onCellClicked(new FlashcardSetPreview(flashcardSet));
        }
    }
}
