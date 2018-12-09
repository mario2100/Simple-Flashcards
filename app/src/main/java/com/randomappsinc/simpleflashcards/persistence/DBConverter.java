package com.randomappsinc.simpleflashcards.persistence;

import com.randomappsinc.simpleflashcards.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.persistence.models.FolderDO;

import java.util.ArrayList;
import java.util.List;

public class DBConverter {

    public static Folder getFolderFromDO(FolderDO folderDO) {
        Folder folder = new Folder();
        folder.setId(folderDO.getId());
        folder.setName(folderDO.getName());

        List<FlashcardSet> flashcardSets = new ArrayList<>(folderDO.getFlashcardSets());
        folder.setFlashcardSets(flashcardSets);
        return folder;
    }
}
