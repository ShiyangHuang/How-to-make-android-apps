package huang.test.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    SQLiteDatabase contactsDB = null;

    Button createDBButton, addContactButton, deleteContactButton,
        getContactsButton, deleteDBButton;

    EditText nameEditText, emailEditText, contactListEditText,
        idEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDBButton = (Button) findViewById(R.id.createDBButton);
        addContactButton = (Button) findViewById(R.id.addContactButton);
        deleteContactButton = (Button) findViewById(R.id.deleteContactButton);
        getContactsButton = (Button) findViewById(R.id.getContactsButton);
        deleteDBButton = (Button) findViewById(R.id.deleteDBButton);
        contactListEditText = (EditText) findViewById(R.id.contactListEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        idEditText = (EditText) findViewById(R.id.idEditText);


    }

    public void createDatabase(View view) {

        contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);
        contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts " + "(id integer primary key, name VARCHAR, email VARCHAR);");
        try {
            File database = getApplicationContext().getDatabasePath("MyContacts.db");

            if (!database.exists()) {
                Toast.makeText(this, "Database created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Database missing", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("Contacts error", "Error creating database");
        }

        addContactButton.setClickable(true);
        deleteContactButton.setClickable(true);
        getContactsButton.setClickable(true);
        deleteDBButton.setClickable(true);
    }

    public void addContact(View view) {

        String contactName = nameEditText.getText().toString();
        String contactEmail = emailEditText.getText().toString();

        contactsDB.execSQL("INSERT INTO contacts (name, email) VALUES ('" + contactName + "','" + contactEmail + "');");
    }


    public void deleteContact(View view) {

        String id = idEditText.getText().toString();

        contactsDB.execSQL("DELETE FROM contacts WHERE id = " + id + ";");
    }

    public void getContacts(View view) {
        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);

        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int emailColumn = cursor.getColumnIndex("email");

        cursor.moveToFirst();

        String contactList = "";

        if (cursor != null && (cursor.getCount() > 0)) {
            do {

                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String email = cursor.getString(emailColumn);

                contactList = contactList + id + " : " + name + " : " + email + "\n";

            }while (cursor.moveToNext());

            contactListEditText.setText(contactList);
        } else {
            Toast.makeText(this, "No result to show", Toast.LENGTH_LONG).show();
            contactListEditText.setText("");
        }
    }

    public void deleteDatabase(View view) {

        this.deleteDatabase("MyContacts");

    }

    @Override
    protected void onDestroy() {
        contactsDB.close();
        super.onDestroy();
    }
}
