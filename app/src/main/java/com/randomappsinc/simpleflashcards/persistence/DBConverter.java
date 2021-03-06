package com.randomappsinc.simpleflashcards.persistence;

import com.randomappsinc.simpleflashcards.folders.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;
import com.randomappsinc.simpleflashcards.persistence.models.FolderDO;

import java.util.ArrayList;
import java.util.List;

public class DBConverter {

    public static Folder getFolderFromDO(FolderDO folderDO) {
        Folder folder = new Folder();
        folder.setId(folderDO.getId());
        folder.setName(folderDO.getName());

        List<FlashcardSetDO> flashcardSets = new ArrayList<>(folderDO.getFlashcardSets());
        folder.setFlashcardSets(flashcardSets);
        return folder;
    }
}
