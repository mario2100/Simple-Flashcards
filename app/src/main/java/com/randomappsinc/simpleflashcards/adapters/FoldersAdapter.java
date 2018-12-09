package com.randomappsinc.simpleflashcards.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FolderViewHolder> {

    public interface Listener {
        void onContentUpdated(int numSets);

        void onFolderClicked(int folderId);
    }

    @NonNull protected Listener listener;
    private Context context;
    protected List<Folder> folders;
    private DatabaseManager databaseManager;

    public FoldersAdapter(@NonNull Listener listener, Context context) {
        this.listener = listener;
        this.context = context;
        this.folders = new ArrayList<>();
        this.databaseManager = DatabaseManager.get();
    }

    public void refreshContent() {
        folders.clear();
        folders.addAll(databaseManager.getFolders());
        notifyDataSetChanged();
        listener.onContentUpdated(getItemCount());
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(
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
        @BindView(R.id.folder_name) TextView folderName;

        FolderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadFolder(int position) {
            Folder folder = folders.get(position);
            folderName.setText(folder.getName());
        }

        @OnClick(R.id.folder_parent)
        public void onCellClicked() {
            listener.onFolderClicked(folders.get(getAdapterPosition()).getId());
        }
    }
}
