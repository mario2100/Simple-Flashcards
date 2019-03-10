package com.randomappsinc.simpleflashcards.editflashcards.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.Constants;
import com.randomappsinc.simpleflashcards.common.activities.PictureFullViewActivity;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.editflashcards.adapters.EditFlashcardsAdapter;
import com.randomappsinc.simpleflashcards.editflashcards.dialogs.CreateFlashcardDialog;
import com.randomappsinc.simpleflashcards.editflashcards.dialogs.DeleteFlashcardDialog;
import com.randomappsinc.simpleflashcards.editflashcards.dialogs.EditFlashcardDefinitionDialog;
import com.randomappsinc.simpleflashcards.editflashcards.dialogs.EditFlashcardSetNameDialog;
import com.randomappsinc.simpleflashcards.editflashcards.dialogs.EditFlashcardTermDialog;
import com.randomappsinc.simpleflashcards.editflashcards.dialogs.FlashcardImageOptionsDialog;
import com.randomappsinc.simpleflashcards.editflashcards.helpers.EditFlashcardsTutorialHelper;
import com.randomappsinc.simpleflashcards.editflashcards.managers.ImportFlashcardsManager;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.utils.PermissionUtils;
import com.randomappsinc.simpleflashcards.utils.StringUtils;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EditFlashcardSetActivity extends StandardActivity
        implements EditFlashcardsAdapter.Listener, FlashcardImageOptionsDialog.Listener,
        ImportFlashcardsManager.Listener {

    // Intent codes
    private static final int IMAGE_FILE_REQUEST_CODE = 1;
    private static final int SEARCH_SPEECH_REQUEST_CODE = 2;
    private static final int TERM_ENTRY_SPEECH_REQUEST_CODE = 3;
    private static final int DEFINITION_ENTRY_SPEECH_REQUEST_CODE = 4;
    private static final int IMPORT_CODE = 5;

    // Permission codes
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    @BindView(R.id.flashcards_info) View focusSink;
    @BindView(R.id.search_input) EditText searchInput;
    @BindView(R.id.voice_search) View voiceSearch;
    @BindView(R.id.clear_search) View clearSearch;
    @BindView(R.id.num_flashcards) TextView numFlashcards;
    @BindView(R.id.no_flashcards) TextView noFlashcards;
    @BindView(R.id.flashcards) RecyclerView flashcardsList;
    @BindView(R.id.add_flashcard) FloatingActionButton addFlashcard;

    protected EditFlashcardsAdapter adapter;
    protected int setId;
    private CreateFlashcardDialog createFlashcardDialog;
    protected DeleteFlashcardDialog deleteFlashcardDialog;
    protected EditFlashcardTermDialog editFlashcardTermDialog;
    protected EditFlashcardDefinitionDialog editFlashcardDefinitionDialog;
    protected FlashcardImageOptionsDialog flashcardImageOptionsDialog;
    private EditFlashcardSetNameDialog editFlashcardSetNameDialog;
    protected int currentlySelectedFlashcardId;
    private ImportFlashcardsManager importFlashcardsManager;

    // Janky state boolean to figure out if we're working with term images or definition images
    protected boolean forTerm;

    protected DatabaseManager databaseManager = DatabaseManager.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_flashcard_set);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        setId = getIntent().getIntExtra(Constants.FLASHCARD_SET_ID_KEY, 0);
        String currentSetName = databaseManager.getSetName(setId);
        setTitle(currentSetName);
        addFlashcard.setImageDrawable(
                new IconDrawable(this, IoniconsIcons.ion_android_add)
                        .colorRes(R.color.white));
        createFlashcardDialog = new CreateFlashcardDialog(this, flashcardCreatedListener, setId);
        deleteFlashcardDialog = new DeleteFlashcardDialog(this, flashcardDeleteListener);
        editFlashcardTermDialog = new EditFlashcardTermDialog(this, flashcardTermEditListener);
        editFlashcardDefinitionDialog = new EditFlashcardDefinitionDialog(
                this, flashcardDefinitionEditListener);
        flashcardImageOptionsDialog = new FlashcardImageOptionsDialog(this, this);
        editFlashcardSetNameDialog = new EditFlashcardSetNameDialog(
                this, currentSetName, editSetNameListener);
        adapter = new EditFlashcardsAdapter(this, setId, noFlashcards, numFlashcards);
        flashcardsList.setAdapter(adapter);
        importFlashcardsManager = new ImportFlashcardsManager(this, this, setId);

        // When the user is scrolling to browse flashcards, close the soft keyboard
        flashcardsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    UIUtils.closeKeyboard(EditFlashcardSetActivity.this);
                    takeAwayFocusFromSearch();
                }
            }
        });

        EditFlashcardsTutorialHelper.teach(this);
    }

    // Stop the EditText cursor from blinking
    protected void takeAwayFocusFromSearch() {
        focusSink.requestFocus();
    }

    @OnTextChanged(value = R.id.search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        adapter.setCurrentQuery(input.toString());
        voiceSearch.setVisibility(input.length() == 0 ? View.VISIBLE : View.GONE);
        clearSearch.setVisibility(input.length() == 0 ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        searchInput.setText("");
    }

    @OnClick(R.id.voice_search)
    public void searchWithVoice() {
        showGoogleSpeechDialog(SEARCH_SPEECH_REQUEST_CODE);
    }

    protected void showGoogleSpeechDialog(int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        switch (requestCode) {
            case SEARCH_SPEECH_REQUEST_CODE:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_message));
                break;
            case TERM_ENTRY_SPEECH_REQUEST_CODE:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.term_speech_prompt));
                break;
            case DEFINITION_ENTRY_SPEECH_REQUEST_CODE:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.definition_speech_prompt));
                break;
        }
        try {
            startActivityForResult(intent, requestCode);
            overridePendingTransition(R.anim.stay, R.anim.slide_in_bottom);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(
                    this,
                    R.string.speech_not_supported,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.add_flashcard)
    public void addFlashcard() {
        createFlashcardDialog.show();
    }

    protected void verifyReadExternalStoragePermission() {
        if (PermissionUtils.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, this)) {
            searchForImageFile();
        } else {
            PermissionUtils.requestPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    protected void searchForImageFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_FILE_REQUEST_CODE);
        }
    }

    @Override
    public void onSetChosen(FlashcardSet flashcardSet, int importMode) {
        Intent intent = new Intent(this, PickAndImportFlashcardsActivity.class)
                .putExtra(Constants.RECEIVING_SET_ID, setId)
                .putExtra(Constants.SENDING_SET_ID, flashcardSet.getId())
                .putExtra(Constants.IMPORT_MODE_KEY, importMode);
        startActivityForResult(intent, IMPORT_CODE);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode) {
            case SEARCH_SPEECH_REQUEST_CODE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String searchQuery = extractTranscription(resultData);
                if (searchQuery != null) {
                    searchInput.setText(searchQuery);
                }
                break;
            case IMAGE_FILE_REQUEST_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && resultCode == Activity.RESULT_OK
                        && resultData != null && resultData.getData() != null) {
                    Uri uri = resultData.getData();

                    // Persist ability to read from this file
                    int takeFlags = resultData.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(uri, takeFlags);

                    String uriString = uri.toString();
                    if (forTerm) {
                        databaseManager.updateFlashcardTermImageUrl(currentlySelectedFlashcardId, uriString);
                        adapter.onTermImageUpdated(uriString);
                    } else {
                        databaseManager.updateFlashcardDefinitionImageUrl(currentlySelectedFlashcardId, uriString);
                        adapter.onDefinitionImageUpdated(uriString);
                    }
                }
                break;
            case TERM_ENTRY_SPEECH_REQUEST_CODE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String term = extractTranscription(resultData);
                createFlashcardDialog.onVoiceTermSpoken(term);
                break;
            case DEFINITION_ENTRY_SPEECH_REQUEST_CODE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String definition = extractTranscription(resultData);
                createFlashcardDialog.onVoiceDefinitionSpoken(definition);
                break;
            case IMPORT_CODE:
                if (resultCode == RESULT_OK) {
                    adapter.refreshSet();
                }
                break;
        }
    }

    @Nullable
    private String extractTranscription(Intent resultData) {
        if (resultData == null) {
            return null;
        }
        List<String> result = resultData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (result == null || result.isEmpty()) {
            UIUtils.showLongToast(R.string.speech_unrecognized, this);
            return null;
        }
        return StringUtils.capitalizeFirstWord(result.get(0));
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            searchForImageFile();
        }
    }

    private final CreateFlashcardDialog.Listener flashcardCreatedListener =
            new CreateFlashcardDialog.Listener() {
                @Override
                public void onFlashcardCreated(String term, String definition) {
                    databaseManager.addFlashcard(setId, term, definition);
                    adapter.refreshSet();
                    flashcardsList.scrollToPosition(adapter.getItemCount() - 1);
                }

                @Override
                public void onVoiceTermEntryRequested() {
                    showGoogleSpeechDialog(TERM_ENTRY_SPEECH_REQUEST_CODE);
                }

                @Override
                public void onVoiceDefinitionEntryRequested() {
                    showGoogleSpeechDialog(DEFINITION_ENTRY_SPEECH_REQUEST_CODE);
                }
            };

    @Override
    public void onEditTerm(Flashcard flashcard) {
        currentlySelectedFlashcardId = flashcard.getId();
        editFlashcardTermDialog.show(flashcard);
    }

    @Override
    public void onEditDefinition(Flashcard flashcard) {
        currentlySelectedFlashcardId = flashcard.getId();
        editFlashcardDefinitionDialog.show(flashcard);
    }

    @Override
    public void onDeleteFlashcard(Flashcard flashcard) {
        currentlySelectedFlashcardId = flashcard.getId();
        deleteFlashcardDialog.show();
    }

    @Override
    public void onImageClicked(Flashcard flashcard, boolean forTerm) {
        this.forTerm = forTerm;
        currentlySelectedFlashcardId = flashcard.getId();
        flashcardImageOptionsDialog.show();
    }

    @Override
    public void onAddImageClicked(Flashcard flashcard, boolean forTerm) {
        this.forTerm = forTerm;
        currentlySelectedFlashcardId = flashcard.getId();
        verifyReadExternalStoragePermission();
    }

    private final DeleteFlashcardDialog.Listener flashcardDeleteListener =
            new DeleteFlashcardDialog.Listener() {
                @Override
                public void onFlashcardDeleted() {
                    databaseManager.deleteFlashcard(currentlySelectedFlashcardId);
                    adapter.onFlashcardDeleted();
                }
            };

    private final EditFlashcardTermDialog.Listener flashcardTermEditListener =
            new EditFlashcardTermDialog.Listener() {
                @Override
                public void onFlashcardTermEdited(String newTerm) {
                    databaseManager.updateFlashcardTerm(currentlySelectedFlashcardId, newTerm);
                    adapter.onFlashcardTermEdited(newTerm);
                }
            };

    private final EditFlashcardDefinitionDialog.Listener flashcardDefinitionEditListener =
            new EditFlashcardDefinitionDialog.Listener() {
                @Override
                public void onFlashcardDefinitionEdited(String newDefinition) {
                    databaseManager.updateFlashcardDefinition(currentlySelectedFlashcardId, newDefinition);
                    adapter.onFlashcardDefinitionEdited(newDefinition);
                }
            };

    @Override
    public void onFullViewRequested() {
        Flashcard flashcard = adapter.getCurrentlyChosenFlashcard();
        Intent intent = new Intent(
                EditFlashcardSetActivity.this,
                PictureFullViewActivity.class)
                .putExtra(Constants.IMAGE_URL_KEY, forTerm
                        ? flashcard.getTermImageUrl()
                        : flashcard.getDefinitionImageUrl())
                .putExtra(Constants.CAPTION_KEY, forTerm
                        ? flashcard.getTerm()
                        : flashcard.getDefinition());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, 0);
    }

    @Override
    public void onFlashcardImageChangeRequested() {
        verifyReadExternalStoragePermission();
    }

    @Override
    public void onFlashcardImageDeleted() {
        if (forTerm) {
            databaseManager.updateFlashcardTermImageUrl(currentlySelectedFlashcardId, null);
            adapter.onTermImageUpdated(null);
        } else {
            databaseManager.updateFlashcardDefinitionImageUrl(currentlySelectedFlashcardId, null);
            adapter.onDefinitionImageUpdated(null);
        }
    }

    private final EditFlashcardSetNameDialog.Listener editSetNameListener =
            new EditFlashcardSetNameDialog.Listener() {
                @Override
                public void onFlashcardSetRename(String newSetName) {
                    databaseManager.renameSet(setId, newSetName);
                    setTitle(newSetName);
                    UIUtils.showShortToast(
                            R.string.flashcard_set_renamed,
                            EditFlashcardSetActivity.this);
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        createFlashcardDialog.cleanUp();
        deleteFlashcardDialog.cleanUp();
        editFlashcardTermDialog.cleanUp();
        editFlashcardDefinitionDialog.cleanUp();
        flashcardImageOptionsDialog.cleanUp();
        editFlashcardSetNameDialog.cleanUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_set, menu);
        UIUtils.loadMenuIcon(menu, R.id.import_flashcards, IoniconsIcons.ion_android_upload, this);
        UIUtils.loadMenuIcon(menu, R.id.rename_flashcard_set, IoniconsIcons.ion_edit, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_flashcards:
                importFlashcardsManager.startImport();
                return true;
            case R.id.rename_flashcard_set:
                editFlashcardSetNameDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
