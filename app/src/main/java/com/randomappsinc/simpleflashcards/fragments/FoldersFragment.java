package com.randomappsinc.simpleflashcards.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.activities.FolderActivity;
import com.randomappsinc.simpleflashcards.adapters.FoldersAdapter;
import com.randomappsinc.simpleflashcards.constants.Constants;
import com.randomappsinc.simpleflashcards.dialogs.CreateFolderDialog;
import com.randomappsinc.simpleflashcards.dialogs.DeleteFolderDialog;
import com.randomappsinc.simpleflashcards.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.views.SimpleDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FoldersFragment extends Fragment
        implements CreateFolderDialog.Listener, FoldersAdapter.Listener, DeleteFolderDialog.Listener {

    public static FoldersFragment newInstance() {
        return new FoldersFragment();
    }

    @BindView(R.id.add_folder) FloatingActionButton addFolder;
    @BindView(R.id.no_folders) View noFolders;
    @BindView(R.id.folders) RecyclerView folders;

    private CreateFolderDialog createFolderDialog;
    private DeleteFolderDialog deleteFolderDialog;
    private DatabaseManager databaseManager = DatabaseManager.get();
    private FoldersAdapter adapter;
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
        deleteFolderDialog = new DeleteFolderDialog(getActivity(), this);
        adapter = new FoldersAdapter(this);
        folders.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        folders.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refreshContent();
    }

    @Override
    public void onContentUpdated(int numSets) {
        noFolders.setVisibility(numSets == 0 ? View.VISIBLE : View.GONE);
        folders.setVisibility(numSets == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onFolderClicked(int folderId) {
        Intent intent = new Intent(getActivity(), FolderActivity.class)
                .putExtra(Constants.FOLDER_ID_KEY, folderId);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    @Override
    public void onFolderDeleteRequested(Folder folder) {
        deleteFolderDialog.show(folder);
    }

    @Override
    public void onFolderDeleted(Folder folder) {
        databaseManager.deleteFolder(folder);
        adapter.onFolderDeleted();
    }

    @OnClick(R.id.add_folder)
    public void addFolder() {
        createFolderDialog.show();
    }

    @Override
    public void onNewFolderSubmitted(String folderName) {
        databaseManager.createFolder(folderName);
        adapter.refreshContent();
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
