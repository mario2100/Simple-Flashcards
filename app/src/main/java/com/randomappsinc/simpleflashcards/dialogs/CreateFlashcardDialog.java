package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.simpleflashcards.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateFlashcardDialog {

    public interface Listener {
        void onFlashcardCreated(String term, String definition);
    }

    @BindView(R.id.term_input) EditText termInput;
    @BindView(R.id.definition_input) EditText definitionInput;

    protected MaterialDialog dialog;

    public CreateFlashcardDialog(Context context, @NonNull final Listener listener) {
        dialog = new MaterialDialog.Builder(context)
                .title(R.string.create_flashcard)
                .customView(R.layout.create_flashcard, true)
                .positiveText(R.string.save)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String term = termInput.getText().toString().trim();
                        String definition = definitionInput.getText().toString().trim();
                        listener.onFlashcardCreated(term, definition);
                    }
                })
                .negativeText(R.string.cancel)
                .build();
        ButterKnife.bind(this, dialog.getCustomView());
    }

    public void show() {
        termInput.setText("");
        definitionInput.setText("");
        dialog.show();
    }
}
