package com.randomappsinc.simpleflashcards.activities;

import android.os.Bundle;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.fragments.HomepageFragmentController;
import com.randomappsinc.simpleflashcards.managers.BackupDataManager;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;
import com.randomappsinc.simpleflashcards.utils.DialogUtil;
import com.randomappsinc.simpleflashcards.utils.UIUtils;
import com.randomappsinc.simpleflashcards.views.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends StandardActivity implements BottomNavigationView.Listener {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigation;

    private HomepageFragmentController navigationController;
    protected BackupDataManager backupDataManager = BackupDataManager.get();
    private DatabaseManager databaseManager = DatabaseManager.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigation.setListener(this);
        navigationController = new HomepageFragmentController(getSupportFragmentManager(), R.id.container);
        navigationController.loadHomeInitially();

        PreferencesManager preferencesManager = new PreferencesManager(this);
        preferencesManager.logAppOpen();
        DialogUtil.showHomepageDialog(this);

        databaseManager.setListener(databaseListener);
    }

    private final DatabaseManager.Listener databaseListener = new DatabaseManager.Listener() {
        @Override
        public void onDatabaseUpdated() {
            backupDataManager.backupData(getApplicationContext(), false);
        }
    };

    @Override
    public void onNavItemSelected(int viewId) {
        UIUtils.closeKeyboard(this);
        navigationController.onNavItemSelected(viewId);
    }

    public void loadQuizletSetSearch() {
        bottomNavigation.onSearchClicked();
    }
}
