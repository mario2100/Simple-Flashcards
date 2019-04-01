package com.randomappsinc.simpleflashcards.editflashcards.adapters;

import android.content.Context;
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

public class SingleFlashcardSetChooserAdapter
        extends RecyclerView.Adapter<SingleFlashcardSetChooserAdapter.FlashcardSetViewHolder>{

    public interface Listener {
        void onFlashcardSetSelected(FlashcardSetDO flashcardSet);
    }

    protected List<FlashcardSetDO> flashcardSets = new ArrayList<>();
    protected Listener listener;

    public SingleFlashcardSetChooserAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setFlashcardSets(List<FlashcardSetDO> newSets) {
        flashcardSets.clear();
        flashcardSets.addAll(newSets);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.simple_flashcard_set_cell,
                parent,
                false);
        return new FlashcardSetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SingleFlashcardSetChooserAdapter.FlashcardSetViewHolder holder, int position) {
        holder.loadFlashcardSet(position);
    }

    @Override
    public int getItemCount() {
        return flashcardSets.size();
    }

    public class FlashcardSetViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.set_name) TextView setName;
        @BindView(R.id.num_flashcards) TextView numFlashcards;

        FlashcardSetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcardSet(int position) {
            FlashcardSetDO flashcardSet = flashcardSets.get(position);
            setName.setText(flashcardSet.getName());
            Context context = numFlashcards.getContext();
            numFlashcards.setText(flashcardSet.getFlashcards().size() == 1
                    ? context.getString(R.string.one_flashcard)
                    : context.getString(R.string.x_flashcards, flashcardSet.getFlashcards().size()));
        }

        @OnClick(R.id.set_preview_parent)
        public void onSetCellClick() {
            FlashcardSetDO flashcardSet = flashcardSets.get(getAdapterPosition());
            listener.onFlashcardSetSelected(flashcardSet);
        }
    }
}
