package com.beatonma.who;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Michael on 24/04/2015.
 */
public class Invisible extends Activity {
    String fromEmail;
    String fromPassword;
    String[] toEmail;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        Log.d("", "Invisible activity started");
        load();
        new MailSender(Invisible.this, fromEmail, fromPassword, toEmail, null).execute();
        finish();
    }

    private void load() {
        SharedPreferences sp = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);
        fromEmail = sp.getString("from_email", "");
        fromPassword = sp.getString("from_password", "");
        toEmail = parseEmails(sp.getString("to_email", ""));
    }

    private String[] parseEmails(String input) {
        String[] output = input.split(",");
        return output;
    }
}
