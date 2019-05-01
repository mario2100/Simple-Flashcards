package com.randomappsinc.simpleflashcards.nearbysharing.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.nearbysharing.adapters.NearbyFlashcardsTabsAdapter;
import com.randomappsinc.simpleflashcards.nearbysharing.managers.NearbyConnectionsManager;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NearbyFlashcardsTransferActivity extends StandardActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nearby_flashcards_tabs) TabLayout tabs;
    @BindView(R.id.nearby_flashcards_pager) ViewPager viewPager;
    @BindArray(R.array.nearby_flashcards_tabs) String[] nearbyFlashcardsTabs;

    private NearbyConnectionsManager nearbyConnectionsManager = NearbyConnectionsManager.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_flashcards_transfer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(new IconDrawable(this, IoniconsIcons.ion_android_close)
                        .colorRes(R.color.white)
                        .actionBarSize());
        setActionBarColors();
        setTitle(getString(R.string.connected_to, nearbyConnectionsManager.getOtherSideName()));

        nearbyConnectionsManager.setPostConnectionListener(postConnectionListener);
        viewPager.setAdapter(new NearbyFlashcardsTabsAdapter(getSupportFragmentManager(), nearbyFlashcardsTabs));
        tabs.setupWithViewPager(viewPager);
    }

    private final NearbyConnectionsManager.PostConnectionListener postConnectionListener =
            new NearbyConnectionsManager.PostConnectionListener() {
                @Override
                public void onDisconnect(String otherSideName) {
                    UIUtils.showLongToast(
                            getString(R.string.disconnected_from, otherSideName),
                            NearbyFlashcardsTransferActivity.this);
                    finish();
                }
            };

    @Override
    public void finish() {
        super.finish();
        nearbyConnectionsManager.disconnect();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
