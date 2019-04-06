package com.randomappsinc.simpleflashcards.browse.managers;

import android.content.Context;

import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class BrowseFlashcardsSettingsManager {

    public interface FlashcardListener {
        void onDefaultSideChanged(boolean showTermsByDefault);

        void onTextSizeChanged(int textSize);

        void onTextColorChanged(int textColor);

        void refresh();
    }

    public interface BrowserListener {
        void onLearnFilterChanged(boolean doNotShowLearned);

        void onEnableShakeChanged(boolean enableShake);
    }

    private static BrowseFlashcardsSettingsManager instance;

    public static BrowseFlashcardsSettingsManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized BrowseFlashcardsSettingsManager getSync() {
        if (instance == null) {
            instance = new BrowseFlashcardsSettingsManager();
        }
        return instance;
    }

    private PreferencesManager preferencesManager;
    private int textSize;
    private int textColor;
    private boolean doNotShowLearned;
    private boolean showTermsByDefault;
    private boolean enableShake;
    private List<FlashcardListener> flashcardListeners = new ArrayList<>();
    private BrowserListener browserListener;

    private BrowseFlashcardsSettingsManager() {}

    public boolean getDoNotShowLearned() {
        return doNotShowLearned;
    }

    public boolean getShowTermsByDefault() {
        return showTermsByDefault;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void changeTextSize(int newTextSize) {
        if (textSize != newTextSize) {
            textSize = newTextSize;
            for (FlashcardListener flashcardListener : flashcardListeners) {
                flashcardListener.onTextSizeChanged(textSize);
            }
        }
    }

    public void changeTextColor(int newTextColor) {
        if (textColor != newTextColor) {
            textColor = newTextColor;
            for (FlashcardListener flashcardListener : flashcardListeners) {
                flashcardListener.onTextColorChanged(textColor);
            }
        }
    }

    public void applySettings(boolean showTermsByDefault, boolean enableShake, boolean doNotShowLearned) {
        // If this is true, we shouldn't alert the terms/definitions listeners because they will redraw
        // themselves when the entire flashcards list is refreshed
        boolean setRefreshed = false;

        if (this.doNotShowLearned != doNotShowLearned) {
            this.doNotShowLearned = doNotShowLearned;
            browserListener.onLearnFilterChanged(doNotShowLearned);
            preferencesManager.setBrowseDoNotShowLearned(doNotShowLearned);
            setRefreshed = true;
        }

        if (this.showTermsByDefault != showTermsByDefault) {
            this.showTermsByDefault = showTermsByDefault;
            if (!setRefreshed) {
                for (FlashcardListener flashcardListener : flashcardListeners) {
                    flashcardListener.onDefaultSideChanged(showTermsByDefault);
                }
            } else {
                for (FlashcardListener flashcardListener : flashcardListeners) {
                    flashcardListener.refresh();
                }
            }
        }

        if (this.enableShake != enableShake) {
            this.enableShake = enableShake;
            browserListener.onEnableShakeChanged(enableShake);
            preferencesManager.setShakeEnabled(enableShake);
        }
    }

    public void addDefaultSideListener(FlashcardListener flashcardListener) {
        flashcardListeners.add(flashcardListener);
    }

    public void removeDefaultSideListener(FlashcardListener flashcardListener) {
        flashcardListeners.remove(flashcardListener);
    }

    public void setBrowserListener(BrowserListener browserListener) {
        this.browserListener = browserListener;
    }

    public void start(Context context) {
        preferencesManager = new PreferencesManager(context);
        textSize = preferencesManager.getBrowseTextSize();
        textColor = preferencesManager.getBrowseTextColor();
        doNotShowLearned = preferencesManager.getBrowseDoNotShowLearned();
        showTermsByDefault = true;
        enableShake = preferencesManager.isShakeEnabled();
    }

    public void shutdown() {
        preferencesManager = null;
        flashcardListeners.clear();
        browserListener = null;
    }
}
