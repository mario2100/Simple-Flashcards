package com.randomappsinc.simpleflashcards.browse.managers;

import android.content.Context;

import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class BrowseFlashcardsSettingsManager {

    public interface DefaultSideListener {
        void onDefaultSideChanged(boolean showTermsByDefault);
    }

    public interface ShakeAndShuffleListener {
        void onShuffleChanged(boolean shuffle);

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

    private boolean showTermsByDefault;
    private boolean shuffle;
    private boolean enableShake;
    private List<DefaultSideListener> defaultSideListeners = new ArrayList<>();
    private ShakeAndShuffleListener shakeAndShuffleListener;

    private BrowseFlashcardsSettingsManager() {}

    public boolean getShowTermsByDefault() {
        return showTermsByDefault;
    }

    public void applySettings(boolean showTermsByDefault, boolean shuffle, boolean enableShake) {
        if (this.showTermsByDefault != showTermsByDefault) {
            this.showTermsByDefault = showTermsByDefault;
            for (DefaultSideListener defaultSideListener : defaultSideListeners) {
                defaultSideListener.onDefaultSideChanged(showTermsByDefault);
            }
        }

        if (this.shuffle != shuffle) {
            this.shuffle = shuffle;
            shakeAndShuffleListener.onShuffleChanged(shuffle);
        }

        if (this.enableShake != enableShake) {
            this.enableShake = enableShake;
            shakeAndShuffleListener.onEnableShakeChanged(enableShake);
        }
    }

    public void addDefaultSideListener(DefaultSideListener defaultSideListener) {
        defaultSideListeners.add(defaultSideListener);
    }

    public void removeDefaultSideListener(DefaultSideListener defaultSideListener) {
        defaultSideListeners.remove(defaultSideListener);
    }

    public void setShakeAndShuffleListener(ShakeAndShuffleListener shakeAndShuffleListener) {
        this.shakeAndShuffleListener = shakeAndShuffleListener;
    }

    public void start(Context context) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        showTermsByDefault = true;
        shuffle = false;
        enableShake = preferencesManager.isShakeEnabled();
    }

    public void shutdown() {
        defaultSideListeners.clear();
        shakeAndShuffleListener = null;
    }
}
