package huang.test.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

/**
 * Created by shiyanghuang on 15/4/30.
 */
public class FileService extends IntentService {

    public FileService(String name) {
        super(name);
    }

    public FileService() {
        super(FileService.class.getName());
    }

    public static final String TRANSACTION_DONE = "huang.test.service.TRANSACTION_DONE";

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("FileService", "Service Started");

        String passedURL = intent.getStringExtra("url");

        downloadFile(passedURL);

        Log.e("FileService", "Service Stopped");

        Intent i = new Intent(TRANSACTION_DONE);

        FileService.this.sendBroadcast(i);
    }

    protected void downloadFile(String theURL) {

        String fileName = "myFile";

        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

            URL fileURL = new URL(theURL);

            HttpURLConnection urlConnection = (HttpURLConnection) fileURL.openConnection();

            urlConnection.setRequestMethod("GET");

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,bufferLength);
            }
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
