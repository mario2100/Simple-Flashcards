package com.randomappsinc.simpleflashcards.nearbysharing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.nearbysharing.constants.FlashcardSetTransferState;
import com.randomappsinc.simpleflashcards.nearbysharing.managers.NearbyConnectionsManager;
import com.randomappsinc.simpleflashcards.nearbysharing.models.FlashcardSetForTransfer;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendFlashcardsAdapter extends RecyclerView.Adapter<SendFlashcardsAdapter.FlashcardSetViewHolder> {

    public interface Listener {
        void onSendFlashcardSet(FlashcardSetDO flashcardSet);

        void onFlashcardSetTransferFailure(FlashcardSetDO flashcardSet);
    }

    protected Listener listener;
    protected List<FlashcardSetForTransfer> flashcardSets;

    public SendFlashcardsAdapter(List<FlashcardSetDO> flashcardSetList, Listener listener) {
        flashcardSets = new ArrayList<>();
        for (FlashcardSetDO original : flashcardSetList) {
            flashcardSets.add(new FlashcardSetForTransfer(original));
        }
        this.listener = listener;
        NearbyConnectionsManager.get().setFlashcardSetTransferStatusListener(flashcardSetTransferStatusListener);
    }

    private final NearbyConnectionsManager.FlashcardSetTransferStatusListener flashcardSetTransferStatusListener =
            new NearbyConnectionsManager.FlashcardSetTransferStatusListener() {
                @Override
                public void onFlashcardSetTransferFailure(int flashcardSetId) {
                    for (int i = 0; i < flashcardSets.size(); i++) {
                        FlashcardSetForTransfer set = flashcardSets.get(i);
                        if (set.getFlashcardSet().getId() == flashcardSetId) {
                            set.setTransferState(FlashcardSetTransferState.NOT_YET_SENT);
                            notifyItemChanged(i);
                            listener.onFlashcardSetTransferFailure(set.getFlashcardSet());
                            break;
                        }
                    }
                }
            };

    protected void setItemToSentState(int position) {
        flashcardSets.get(position).setTransferState(FlashcardSetTransferState.SENT);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public FlashcardSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.send_flashcard_set_cell,
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
        @BindView(R.id.send) View send;
        @BindView(R.id.sent) View sent;

        @BindString(R.string.one_flashcard) String oneFlashcard;
        @BindString(R.string.x_flashcards) String xFlashcards;

        FlashcardSetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFlashcardSet(int position) {
            FlashcardSetForTransfer flashcardSetForTransfer = flashcardSets.get(position);
            FlashcardSetDO flashcardSet = flashcardSetForTransfer.getFlashcardSet();
            setName.setText(flashcardSet.getName());
            int numFlashcards = flashcardSet.getFlashcards().size();
            numFlashcardsText.setText(numFlashcards == 1
                    ? oneFlashcard
                    : String.format(xFlashcards, numFlashcards));

            switch (flashcardSetForTransfer.getTransferState()) {
                case FlashcardSetTransferState.NOT_YET_SENT:
                    sent.setVisibility(View.GONE);
                    send.setVisibility(View.VISIBLE);
                    break;
                case FlashcardSetTransferState.SENT:
                    send.setVisibility(View.GONE);
                    sent.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @OnClick(R.id.send)
        public void sendFlashcardSet() {
            setItemToSentState(getAdapterPosition());
            listener.onSendFlashcardSet(flashcardSets.get(getAdapterPosition()).getFlashcardSet());
        }
    }
}
