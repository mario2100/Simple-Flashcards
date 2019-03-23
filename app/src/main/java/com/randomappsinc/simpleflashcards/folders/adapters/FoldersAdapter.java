package com.randomappsinc.simpleflashcards.folders.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.folders.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;
import com.randomappsinc.simpleflashcards.theme.ThemedIconTextView;
import com.randomappsinc.simpleflashcards.theme.ThemedSubtitleTextView;
import com.randomappsinc.simpleflashcards.theme.ThemedTitleTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FolderViewHolder>
        implements ThemeManager.Listener {

    public interface Listener {
        void onFolderClicked(int folderId);

        void onFolderDeleteRequested(Folder folder);
    }

    @NonNull protected Listener listener;
    protected List<Folder> folders;
    private DatabaseManager databaseManager;
    protected int lastInteractedWithPosition;
    private ThemeManager themeManager = ThemeManager.get();

    public FoldersAdapter(@NonNull Listener listener) {
        this.listener = listener;
        this.folders = new ArrayList<>();
        this.databaseManager = DatabaseManager.get();
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
        folders.clear();
        folders.addAll(databaseManager.getFolders(searchTerm));
        notifyDataSetChanged();
    }

    public void onFolderDeleted() {
        folders.remove(lastInteractedWithPosition);
        notifyItemRemoved(lastInteractedWithPosition);
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.folder_cell,
                parent,
                false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        holder.loadFolder(position);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.delete_folder) ThemedIconTextView deleteFolder;
        @BindView(R.id.folder_name) ThemedTitleTextView folderName;
        @BindView(R.id.num_sets) ThemedSubtitleTextView numSetsText;

        FolderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFolder(int position) {
            Folder folder = folders.get(position);
            folderName.setText(folder.getName());
            int numSets = folder.getFlashcardSets().size();
            if (numSets == 1) {
                numSetsText.setText(R.string.one_flashcard_set);
            } else {
                Context context = numSetsText.getContext();
                numSetsText.setText(context.getString(R.string.x_flashcard_sets, numSets));
            }
            adjustForDarkMode();
        }

        void adjustForDarkMode() {
            deleteFolder.setProperColors();
            folderName.setProperColors();
            numSetsText.setProperColors();
        }

        @OnClick(R.id.folder_parent)
        public void onCellClicked() {
            listener.onFolderClicked(folders.get(getAdapterPosition()).getId());
        }

        @OnClick(R.id.delete_folder)
        public void onDeleteClicked() {
            lastInteractedWithPosition = getAdapterPosition();
            listener.onFolderDeleteRequested(folders.get(getAdapterPosition()));
        }
    }
}
