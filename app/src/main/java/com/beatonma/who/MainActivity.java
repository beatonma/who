package com.beatonma.who;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "Who";
    static final String PREFS = "settings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit();

        if (isFirstRun()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showHelp();
                }
            }, 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            showHelp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showHelp() {
        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .title(getString(R.string.instructions_title))
                .customView(R.layout.instructions, true)
                .theme(Theme.DARK)
                .positiveText("OK")
                .build();

        dialog.show();

        TextView content = (TextView) dialog.getCustomView().findViewById(R.id.content);
        content.setLinkTextColor(getResources().getColor(R.color.Accent));
        content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public boolean isFirstRun() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean result = sp.getBoolean("first_run", true);
        sp.edit().putBoolean("first_run", false).commit(); // Overwrite so this should always be false after first run

        return result;
    }
}
