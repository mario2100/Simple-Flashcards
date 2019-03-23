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
    }

    public interface ShakeListener {
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

    private int textSize;
    private int textColor;
    private boolean showTermsByDefault;
    private boolean enableShake;
    private List<FlashcardListener> flashcardListeners = new ArrayList<>();
    private ShakeListener shakeListener;

    private BrowseFlashcardsSettingsManager() {}

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

    public void applySettings(boolean showTermsByDefault, boolean enableShake) {
        if (this.showTermsByDefault != showTermsByDefault) {
            this.showTermsByDefault = showTermsByDefault;
            for (FlashcardListener flashcardListener : flashcardListeners) {
                flashcardListener.onDefaultSideChanged(showTermsByDefault);
            }
        }

        if (this.enableShake != enableShake) {
            this.enableShake = enableShake;
            shakeListener.onEnableShakeChanged(enableShake);
        }
    }

    public void addDefaultSideListener(FlashcardListener flashcardListener) {
        flashcardListeners.add(flashcardListener);
    }

    public void removeDefaultSideListener(FlashcardListener flashcardListener) {
        flashcardListeners.remove(flashcardListener);
    }

    public void setShakeListener(ShakeListener shakeListener) {
        this.shakeListener = shakeListener;
    }

    public void start(Context context) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        textSize = preferencesManager.getBrowseTextSize();
        textColor = preferencesManager.getBrowseTextColor();
        showTermsByDefault = true;
        enableShake = preferencesManager.isShakeEnabled();
    }

    public void shutdown() {
        flashcardListeners.clear();
        shakeListener = null;
    }
}
