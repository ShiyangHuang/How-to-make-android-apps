package huang.test.savingstate;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by shiyanghuang on 15/4/30.
 */
public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

    }
}
