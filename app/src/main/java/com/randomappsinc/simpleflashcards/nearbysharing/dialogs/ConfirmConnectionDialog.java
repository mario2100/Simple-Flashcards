package com.randomappsinc.simpleflashcards.nearbysharing.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;
import com.randomappsinc.simpleflashcards.utils.StringUtils;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmConnectionDialog implements ThemeManager.Listener {

    public interface Listener {
        void onConnectionAccepted();

        void onConnectionRejected();
    }

    @BindView(R.id.connection_prompt) TextView title;
    @BindView(R.id.authentication_token) TextView authToken;

    protected MaterialDialog dialog;
    private View contentView;
    private Context context;
    protected Listener listener;
    private ThemeManager themeManager = ThemeManager.get();

    public ConfirmConnectionDialog(Context context, @NonNull Listener listener) {
        this.context = context;
        this.listener = listener;
        contentView = LayoutInflater.from(context).inflate(R.layout.confirm_connection, null);
        ButterKnife.bind(this, contentView);
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        dialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .customView(contentView, true)
                .positiveText(R.string.accept)
                .negativeText(R.string.reject)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onConnectionAccepted();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onConnectionRejected();
                    }
                })
                .cancelable(false)
                .build();
    }

    public void show(ConnectionInfo connectionInfo, Context context) {
        String deviceText = StringUtils.getSaneDeviceString(connectionInfo.getEndpointName());
        title.setText(connectionInfo.isIncomingConnection()
                ? context.getString(R.string.x_would_like_to_connect, deviceText)
                : context.getString(R.string.connecting_to_x, deviceText));
        authToken.setText(connectionInfo.getAuthenticationToken());
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
