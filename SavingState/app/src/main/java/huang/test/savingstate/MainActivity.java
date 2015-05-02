package huang.test.savingstate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    EditText notesEditText;
    Button btnSettings;
    private static final int SETTINGS_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesEditText = (EditText) findViewById(R.id.editText);

        if (savedInstanceState != null) {

            String notes = savedInstanceState.getString("NOTES");

            notesEditText.setText(notes);
        }

        String sPNotes = getPreferences(Context.MODE_PRIVATE).getString("NOTES", "EMPTY");

        if (!sPNotes.equals("EMPTY")) {

            notesEditText.setText(sPNotes);

        }

        btnSettings = (Button) findViewById(R.id.btnSettings);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentPreferences = new Intent(getApplicationContext(), SettingsActivity.class);

                startActivityForResult(intentPreferences, SETTINGS_INFO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_INFO) {

            updateNoteText();
        }
    }

    private void updateNoteText() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("pref_text_bold", false)) {

            notesEditText.setTypeface(null, Typeface.NORMAL);

        }

        String textSizeStr = sharedPreferences.getString("pref_text_size", "16");

        float textSizeFloat = Float.parseFloat(textSizeStr);

        notesEditText.setTextSize(textSizeFloat);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("NOTES", notesEditText.getText().toString());

        super.onSaveInstanceState(outState);
    }

    private void saveSettings() {

        SharedPreferences.Editor sPEditor = getPreferences(Context.MODE_PRIVATE).edit();

        sPEditor.putString("NOTES", notesEditText.getText().toString());

        sPEditor.commit();
    }

    @Override
    protected void onStop() {
        saveSettings();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
