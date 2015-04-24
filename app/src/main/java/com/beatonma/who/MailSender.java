package com.beatonma.who;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Michael on 24/04/2015.
 */
public class MailSender extends AsyncTask<Void, Void, Void> {
    private final static String TAG = "MailSender";
    Context context;
    Mail mail;
    boolean success = false;
    String exception = "";
    ProgressBar progress;

    public MailSender(Context context, String fromEmail, String password, String[] toEmail, ProgressBar progress) {
        this.context = context;
        this.progress = progress;
        mail = new Mail(fromEmail, password);
        mail.setToAddress(toEmail);
        mail.setSubject(context.getResources().getString(R.string.email_subject));
    }

    @Override
    protected void onPreExecute() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
            progress.setIndeterminate(true);
        }
    }

    @Override
    protected Void doInBackground(Void... file) {
        Log.d(TAG, "Attempting to send email.");
        try {
            mail.setBody(getAccounts());
            success = mail.send();
        }
        catch (Exception e) {
            Log.e(TAG, "Exception when attempting to send email: " + e.toString());
            exception = e.toString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void file) {
        if (progress != null) { // Else everything should be done invisibly
            progress.setVisibility(View.GONE);
            if (success) {
                Log.d(TAG, "Email sent!");
                Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d(TAG, "Email failed!");
                Toast.makeText(context, (exception.contains("AuthenticationFailedException") ? "Authentication error: Please check your details." : "Error: " + exception), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getAccounts() {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccounts();
        String output = "Active accounts on this device:\n\n\n";
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
}
