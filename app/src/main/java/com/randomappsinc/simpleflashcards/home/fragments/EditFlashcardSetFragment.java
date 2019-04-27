package com.randomappsinc.simpleflashcards.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.constants.Constants;
import com.randomappsinc.simpleflashcards.common.views.SimpleDividerItemDecoration;
import com.randomappsinc.simpleflashcards.home.adapters.FlashcardSetOptionsAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EditFlashcardSetFragment extends Fragment implements FlashcardSetOptionsAdapter.ItemSelectionListener {

    public static EditFlashcardSetFragment getInstance(int setId) {
        EditFlashcardSetFragment fragment = new EditFlashcardSetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.FLASHCARD_SET_ID_KEY, setId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.edit_set_options) RecyclerView editSetOptions;

    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.edit_flashcard_set_fragment,
                container,
                false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editSetOptions.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        editSetOptions.setAdapter(new FlashcardSetOptionsAdapter(
                getActivity(),
                this,
                R.array.flashcard_set_edit_options,
                R.array.flashcard_set_edit_icons));
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
