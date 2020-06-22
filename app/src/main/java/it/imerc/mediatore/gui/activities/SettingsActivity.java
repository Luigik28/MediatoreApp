package it.imerc.mediatore.gui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import it.imerc.mediatore.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pref, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_reset:
                getSharedPreferences(getString(R.string.preferencesKey),Context.MODE_PRIVATE).edit().clear().apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SharedPreferences sharedPreferences;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            sharedPreferences = requireContext().getSharedPreferences(getString(R.string.preferencesKey),Context.MODE_PRIVATE);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Objects.requireNonNull(findPreference("ipAddress")).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(newValue == null)
                        return false;
                    String newIp = newValue.toString();
                    boolean ret = newIp.matches("(\\d){1,3}\\.(\\d){1,3}.(\\d){1,3}\\.(\\d){1,3}");
                    if(ret) {
                        sharedPreferences.edit().putString(preference.getKey(),newIp).apply();
                    } else
                        Toast.makeText(requireContext(), "Indirizzo IP non valido", Toast.LENGTH_LONG).show();
                    return ret;
                }
            });
            EditTextPreference port = (EditTextPreference) Objects.requireNonNull(findPreference("portAddress"));
            port.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setFilters(new InputFilter[]{
                            new InputFilter.LengthFilter(5)
                    });
                }
            });

            port.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    int port = Integer.parseInt(newValue.toString());
                    if(port > 0) {
                        sharedPreferences.edit().putInt(preference.getKey(), port).apply();
                        return true;
                    }
                    else
                        return false;
                }
            });
        }
    }
}