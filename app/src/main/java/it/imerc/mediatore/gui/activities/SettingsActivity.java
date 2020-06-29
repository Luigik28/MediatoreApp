package it.imerc.mediatore.gui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
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
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import it.imerc.mediatore.R;
import it.imerc.mediatore.util.Utility;
import it.imerc.mediatore.wsClient.MediatoreOperation;

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
                getSharedPreferences(getString(R.string.preferencesKey), Context.MODE_PRIVATE).edit().clear().apply();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SharedPreferences sharedPreferences;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            sharedPreferences = requireContext().getSharedPreferences(getString(R.string.preferencesKey), Context.MODE_PRIVATE);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            final SwitchPreferenceCompat debug = ((SwitchPreferenceCompat) Objects.requireNonNull(findPreference(Utility.debugKey)));
            final EditTextPreference ipAddresPreference = (EditTextPreference) Objects.requireNonNull(findPreference(Utility.ipAddressKey));
            final EditTextPreference port = (EditTextPreference) Objects.requireNonNull(findPreference(Utility.portAddressKey));
            final EditTextPreference timeout = (EditTextPreference) Objects.requireNonNull(findPreference(Utility.timeoutKey));
            //set default value
            debug.setChecked(sharedPreferences.getBoolean(Utility.debugKey, false));
            ipAddresPreference.setText(sharedPreferences.getString(Utility.ipAddressKey, getString(R.string.defaultIp)));
            port.setText(sharedPreferences.getString(Utility.portAddressKey, getString(R.string.defaultPort)));
            timeout.setText(String.valueOf(sharedPreferences.getInt(Utility.timeoutKey, MediatoreOperation.timeout)));

            debug.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String localServer = null;
                    boolean value = Boolean.parseBoolean(newValue.toString());
                    if (value) {
                        localServer = "http://";
                        localServer += sharedPreferences.getString(Utility.ipAddressKey, getString(R.string.defaultIp));
                        localServer += ":" + sharedPreferences.getString(Utility.portAddressKey, getString(R.string.defaultPort));
                    }
                    sharedPreferences.edit().putBoolean(Utility.debugKey, value).apply();
                    MediatoreOperation.setLocalServer(localServer);
                    return true;
                }
            });
            ipAddresPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue == null)
                        return false;
                    String newIp = newValue.toString();
                    boolean ret = newIp.matches("(\\d){1,3}\\.(\\d){1,3}.(\\d){1,3}\\.(\\d){1,3}");
                    String localServer;
                    if (ret) {
                        sharedPreferences.edit().putString(preference.getKey(), newIp).apply();
                        localServer = "http://";
                        localServer += sharedPreferences.getString(Utility.ipAddressKey, getString(R.string.defaultIp));
                        localServer += ":" + sharedPreferences.getString(Utility.portAddressKey, getString(R.string.defaultPort));
                        MediatoreOperation.setLocalServer(localServer);
                    } else
                        Toast.makeText(requireContext(), "Indirizzo IP non valido", Toast.LENGTH_LONG).show();
                    return ret;
                }
            });
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
                    int portInt = Integer.parseInt(newValue.toString());
                    boolean ret = false;
                    String localServer;
                    if (portInt > 0) {
                        sharedPreferences.edit().putInt(preference.getKey(), portInt).apply();
                        localServer = "http://";
                        localServer += sharedPreferences.getString(Utility.ipAddressKey, getString(R.string.defaultIp));
                        localServer += ":" + sharedPreferences.getString(Utility.portAddressKey, getString(R.string.defaultPort));
                        MediatoreOperation.setLocalServer(localServer);
                        ret = true;
                    }
                    return ret;
                }
            });
            timeout.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean ret = false;
                    try {
                        MediatoreOperation.timeout = Integer.parseInt(newValue.toString());
                        sharedPreferences.edit().putInt(Utility.timeoutKey, MediatoreOperation.timeout).apply();
                        ret = true;
                    } catch (NumberFormatException e) {
                        Toast.makeText(requireContext(), "Valore timeout non valido", Toast.LENGTH_LONG).show();
                    }
                    return ret;
                }
            });

            timeout.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            });
        }
    }
}