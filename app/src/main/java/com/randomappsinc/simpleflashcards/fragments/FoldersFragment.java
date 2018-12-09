package com.randomappsinc.simpleflashcards.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.dialogs.CreateFolderDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FoldersFragment extends Fragment implements CreateFolderDialog.Listener {

    public static FoldersFragment newInstance() {
        return new FoldersFragment();
    }

    @BindView(R.id.add_folder) FloatingActionButton addFolder;

    private CreateFolderDialog createFolderDialog;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.folders,
                container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        addFolder.setImageDrawable(new IconDrawable(getContext(), IoniconsIcons.ion_android_add)
                .colorRes(R.color.white)
                .actionBarSize());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createFolderDialog = new CreateFolderDialog(getActivity(), this);
    }

    @OnClick(R.id.add_folder)
    public void addFolder() {
        createFolderDialog.show();
    }

    @Override
    public void onNewFolderSubmitted(String folderName) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.filter).setVisible(false);
    }
}
