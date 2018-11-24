package com.randomappsinc.simpleflashcards.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.activities.BackupAndRestoreActivity;
import com.randomappsinc.simpleflashcards.activities.NearbySharingActivity;
import com.randomappsinc.simpleflashcards.adapters.SettingsAdapter;
import com.randomappsinc.simpleflashcards.managers.NearbyNameManager;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public static final String SUPPORT_EMAIL = "RandomAppsInc61@gmail.com";
    public static final String OTHER_APPS_URL = "https://play.google.com/store/apps/dev?id=9093438553713389916";
    public static final String REPO_URL = "https://github.com/Gear61/Simple-Flashcards";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.settings_options) ListView settingsOptions;
    @BindString(R.string.feedback_subject) String feedbackSubject;
    @BindString(R.string.send_email) String sendEmail;

    private NearbyNameManager nearbyNameManager;
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.settings,
                container,
                false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar.setTitle(R.string.settings);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        nearbyNameManager = new NearbyNameManager(getActivity(), null);
        settingsOptions.setAdapter(new SettingsAdapter(getActivity()));
    }

    @OnItemClick(R.id.settings_options)
    public void onItemClick(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(getActivity(), NearbySharingActivity.class);
                break;
            case 1:
                intent = new Intent(getActivity(), BackupAndRestoreActivity.class);
                break;
            case 2:
                nearbyNameManager.showNameSetter();
                return;
            case 3:
                String uriText = "mailto:" + SUPPORT_EMAIL + "?subject=" + Uri.encode(feedbackSubject);
                Uri mailUri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, mailUri);
                startActivity(Intent.createChooser(sendIntent, sendEmail));
                return;
            case 4:
                Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getString(R.string.share_app_message))
                        .getIntent();
                if (shareIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(shareIntent);
                }
                return;
            case 5:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(OTHER_APPS_URL));
                break;
            case 6:
                Uri uri =  Uri.parse("market://details?id=" + getContext().getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                if (!(getContext().getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                    UIUtils.showLongToast(R.string.play_store_error, getContext());
                    return;
                }
                break;
            case 7:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(REPO_URL));
                break;
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
