package com.beatonma.who;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

/**
 * Created by Michael on 24/04/2015.
 */
public class MainFragment extends Fragment {
    private final static String TAG = "Who";

    Context context;

    private EditText fromEmail;
    private EditText fromPassword;
    private EditText toEmail;
    private ProgressBar sendProgress;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        this.context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
        Configuration configuration = context.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;
        int layout = screenWidthDp > 600 ? R.layout.fragment_main_wide : R.layout.fragment_main;

        View v = inflater.inflate(layout, parent, false);

        sendProgress = (ProgressBar) v.findViewById(R.id.send_progress);

        fromEmail = (EditText) v.findViewById(R.id.from_email_address);
        fromPassword = (EditText) v.findViewById(R.id.from_password);
        toEmail = (EditText) v.findViewById(R.id.to_address);
        Button buttonTest = (Button) v.findViewById(R.id.button_test);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button pressed: sending test email.");
                String sender = fromEmail.getText().toString();
                String password = fromPassword.getText().toString();
                String[] receivers = parseEmails(toEmail.getText().toString());

                new MailSender(context, sender, password, receivers, sendProgress).execute();
            }
        });

        Button buttonPreview = (Button) v.findViewById(R.id.button_preview);
        buttonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title("Preview")
                        .customView(R.layout.preview, true)
                        .theme(Theme.DARK)
                        .positiveText("OK")
                        .build();

                dialog.show();

                ((TextView) dialog.getCustomView().findViewById(R.id.content)).setText(getAccounts());
            }
        });

        loadSettings();
        return v;
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }

    public String getAccounts() {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccounts();
        String output = "";
        for (int i = 0; i < accounts.length; i++) {
            Account account = accounts[i];
            output = output
                    + "Type: " + account.type
                    + "\n"
                    + "Name: " + account.name
                    + "\n\n";
        }

        return output;
    }

    public void loadSettings() {
        SharedPreferences sp = context.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
        fromEmail.setText(sp.getString("from_email", ""));
        fromPassword.setText(sp.getString("from_password", ""));
        toEmail.setText(sp.getString("to_email", ""));
    }

    public void saveSettings() {
        SharedPreferences sp = context.getSharedPreferences(MainActivity.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("from_email", fromEmail.getText().toString());
        editor.putString("from_password", fromPassword.getText().toString());
        editor.putString("to_email", toEmail.getText().toString());
        editor.commit();
    }

    private String[] parseEmails(String input) {
        String[] output = input.split(",");
        return output;
    }
}
