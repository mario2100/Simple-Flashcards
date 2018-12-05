package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.simpleflashcards.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class CreateFlashcardDialog implements MaterialDialog.SingleButtonCallback {

    public interface Listener {
        void onFlashcardCreated(String term, String definition);

        void onVoiceTermEntryRequested();
    }

    @BindView(R.id.term_input) EditText termInput;
    @BindView(R.id.voice_term_entry) View voiceTermEntry;
    @BindView(R.id.definition_input) EditText definitionInput;

    protected MaterialDialog dialog;
    @NonNull protected Listener listener;

    public CreateFlashcardDialog(Context context, @NonNull final Listener listener) {
        this.listener = listener;
        dialog = new MaterialDialog.Builder(context)
                .title(R.string.create_flashcard)
                .customView(R.layout.create_flashcard, true)
                .positiveText(R.string.save)
                .onPositive(this)
                .negativeText(R.string.cancel)
                .build();
        ButterKnife.bind(this, dialog.getCustomView());
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (which == DialogAction.POSITIVE) {
            String term = termInput.getText().toString().trim();
            String definition = definitionInput.getText().toString().trim();
            listener.onFlashcardCreated(term, definition);
        }
    }

    @OnTextChanged(value = R.id.term_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        voiceTermEntry.setVisibility(input.length() == 0 ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.voice_term_entry)
    public void onVoiceTermEntry() {
        listener.onVoiceTermEntryRequested();
    }

    public void onVoiceTermSpoken(String term) {
        termInput.setText(term);
        termInput.setSelection(term.length());
    }

    public void show() {
        termInput.setText("");
        definitionInput.setText("");
        dialog.show();
        if (termInput.requestFocus()) {
            termInput.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.showSoftInput(termInput, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            });
        }
    }
}
