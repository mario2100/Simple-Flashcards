package com.randomappsinc.simpleflashcards.nearbysharing.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.views.SimpleDividerItemDecoration;
import com.randomappsinc.simpleflashcards.nearbysharing.adapters.SendFlashcardsAdapter;
import com.randomappsinc.simpleflashcards.nearbysharing.managers.NearbyConnectionsManager;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SendFlashcardsFragment extends Fragment {

    @BindView(R.id.no_flashcards_to_send) View noFlashcards;
    @BindView(R.id.flashcard_sets_to_send) RecyclerView flashcardSetList;

    protected NearbyConnectionsManager nearbyConnectionsManager = NearbyConnectionsManager.get();
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.send_flashcards,
                container,
                false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<FlashcardSetDO> flashcardSets = DatabaseManager.get().getFlashcardSets("");
        if (flashcardSets.isEmpty()) {
            flashcardSetList.setVisibility(View.GONE);
        } else {
            flashcardSetList.setAdapter(new SendFlashcardsAdapter(flashcardSets, sendFlashcardsListener));
            flashcardSetList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            noFlashcards.setVisibility(View.GONE);
        }
    }

    private final SendFlashcardsAdapter.Listener sendFlashcardsListener = new SendFlashcardsAdapter.Listener() {
        @Override
        public void onSendFlashcardSet(FlashcardSetDO flashcardSet) {
            nearbyConnectionsManager.sendFlashcardSet(flashcardSet, getContext());
        }

        @Override
        public void onFlashcardSetTransferFailure(FlashcardSetDO flashcardSet) {
            UIUtils.showLongToast(getString(
                    R.string.failed_to_send_set,
                    flashcardSet.getName()), getContext());
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
