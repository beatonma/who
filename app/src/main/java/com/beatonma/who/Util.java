package com.beatonma.who;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Created by Michael on 24/04/2015.
 */
public class Util {
    private static final String TAG = "";

    public static String getAccounts(Context context) {
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
}
