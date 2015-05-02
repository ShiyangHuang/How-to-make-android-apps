package huang.test.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    EditText downloadEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadEditText = (EditText) findViewById(R.id.downloadEditText);

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(FileService.TRANSACTION_DONE);

        registerReceiver(downloadReceiver, intentFilter);


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

    public void startFileService(View view) {

        Intent intent = new Intent(this, FileService.class);

        intent.putExtra("url", "https://www.newthinktank.com/wordpress/lotr.txt");

        this.startService(intent);

    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("FileService", "Service Received");

            showFileContents();
        }
    };

    public void showFileContents() {

        StringBuilder sb;

        try {
            FileInputStream fis = this.openFileInput("myFile");

            InputStreamReader isr = new InputStreamReader(fis, "UTF8");

            BufferedReader bufferedReader = new BufferedReader(isr);

            sb = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            downloadEditText.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
