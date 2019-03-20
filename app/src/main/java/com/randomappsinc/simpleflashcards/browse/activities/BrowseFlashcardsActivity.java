package com.randomappsinc.simpleflashcards.browse.activities;

import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SeekBar;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.browse.adapters.FlashcardsBrowsingAdapter;
import com.randomappsinc.simpleflashcards.browse.dialogs.BrowseSettingsDialogsManager;
import com.randomappsinc.simpleflashcards.browse.managers.BrowseFlashcardsSettingsManager;
import com.randomappsinc.simpleflashcards.common.Constants;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.common.managers.TextToSpeechManager;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.utils.UIUtils;
import com.squareup.seismic.ShakeDetector;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class BrowseFlashcardsActivity extends StandardActivity
        implements ShakeDetector.Listener,
        BrowseFlashcardsSettingsManager.ShakeListener,
        BrowseSettingsDialogsManager.Listener {

    @BindView(R.id.browse_parent) View parent;
    @BindView(R.id.flashcards_pager) ViewPager flashcardsPager;
    @BindView(R.id.flashcards_slider) SeekBar flashcardsSlider;

    private FlashcardsBrowsingAdapter flashcardsBrowsingAdapter;
    private TextToSpeechManager textToSpeechManager;
    private BrowseSettingsDialogsManager browseSettingsDialogsManager;
    private BrowseFlashcardsSettingsManager settingsManager = BrowseFlashcardsSettingsManager.get();
    private PreferencesManager preferencesManager;
    private Random random;
    private ShakeDetector shakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_flashcards);
        ButterKnife.bind(this);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        textToSpeechManager = new TextToSpeechManager(this, textToSpeechListener);

        preferencesManager = new PreferencesManager(this);
        parent.setBackgroundColor(preferencesManager.getDarkModeEnabled()
                ? ContextCompat.getColor(this, R.color.dark_mode_browse)
                : ContextCompat.getColor(this, R.color.theater_black));

        int setId = getIntent().getIntExtra(Constants.FLASHCARD_SET_ID_KEY, 0);
        FlashcardSet flashcardSet = DatabaseManager.get().getFlashcardSet(setId);
        setTitle(flashcardSet.getName());

        if (flashcardSet.getFlashcards().size() < 2) {
            flashcardsSlider.setVisibility(View.GONE);
        }

        flashcardsBrowsingAdapter = new FlashcardsBrowsingAdapter(getSupportFragmentManager(), setId);
        flashcardsPager.setAdapter(flashcardsBrowsingAdapter);

        flashcardsSlider.setMax(flashcardSet.getFlashcards().size() - 1);
        flashcardsSlider.setOnSeekBarChangeListener(flashcardsSliderListener);

        browseSettingsDialogsManager = new BrowseSettingsDialogsManager(this, this);
        settingsManager.start(this);
        settingsManager.setShakeListener(this);

        random = new Random();
        shakeDetector = new ShakeDetector(this);
    }

    @Override
    protected void setActionBarColors() {
        // We don't have a toolbar/status bar, so we make this no-op
    }

    private final SeekBar.OnSeekBarChangeListener flashcardsSliderListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        flashcardsPager.setCurrentItem(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            };

    private final TextToSpeechManager.Listener textToSpeechListener = new TextToSpeechManager.Listener() {
        @Override
        public void onTextToSpeechFailure() {
            UIUtils.showLongToast(
                    R.string.text_to_speech_fail,
                    BrowseFlashcardsActivity.this);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (preferencesManager.isShakeEnabled()) {
            shakeDetector.stop();
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferencesManager.isShakeEnabled()) {
            shakeDetector.start((SensorManager) getSystemService(SENSOR_SERVICE));
        }
    }

    @Override
    public void onShuffleRequested() {
        flashcardsBrowsingAdapter.shuffle();
        flashcardsPager.setAdapter(flashcardsBrowsingAdapter);
        flashcardsPager.setCurrentItem(0);
        flashcardsSlider.setProgress(0);
        UIUtils.showShortToast(R.string.flashcards_shuffled, this);
    }

    @Override
    public void onRestoreRequested() {
        if (flashcardsBrowsingAdapter.isShuffled()) {
            flashcardsBrowsingAdapter.restoreOrder();
            flashcardsPager.setAdapter(flashcardsBrowsingAdapter);
            flashcardsPager.setCurrentItem(0);
            flashcardsSlider.setProgress(0);
            UIUtils.showShortToast(R.string.flashcards_order_restored, this);
        } else {
            UIUtils.showLongToast(R.string.flashcards_original_order_already, this);
        }
    }

    @Override
    public void onEnableShakeChanged(boolean enableShake) {
        if (enableShake) {
            shakeDetector.start((SensorManager) getSystemService(SENSOR_SERVICE));
            preferencesManager.setShakeEnabled(true);
        } else {
            shakeDetector.stop();
            preferencesManager.setShakeEnabled(false);
        }
    }

    @Override
    public void hearShake() {
        int index = random.nextInt(flashcardsBrowsingAdapter.getCount());
        flashcardsPager.setCurrentItem(index);
    }

    @OnPageChange(R.id.flashcards_pager)
    public void onFlashcardChanged(int position) {
        textToSpeechManager.stopSpeaking();
        flashcardsSlider.setProgress(position);
    }

    public void speak(String text) {
        textToSpeechManager.speak(text);
    }

    public void stopSpeaking() {
        textToSpeechManager.stopSpeaking();
    }

    @OnClick(R.id.settings)
    public void onSettingsClick() {
        browseSettingsDialogsManager.showOptions();
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        textToSpeechManager.stopSpeaking();
        if (preferencesManager.isShakeEnabled()) {
            shakeDetector.stop();
        }
    }

    @Override
    public void onDestroy() {
        textToSpeechManager.shutdown();
        settingsManager.shutdown();
        browseSettingsDialogsManager.shutdown();
        super.onDestroy();
    }
}
