package huang.test.translation;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.security.acl.AclNotFoundException;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    EditText translateEditText;

    private Locale locSpanish = new Locale("es", "MX");
    private Locale locRussian = new Locale("ru", "RU");
    private Locale locPortuguess = new Locale("pt", "BR");
    private Locale locDutch = new Locale("nl", "NL");

    private Locale currentSpokenLang = Locale.US;

    private Locale[] languages = {locDutch, Locale.FRANCE, Locale.GERMAN, Locale.ITALIAN, locPortuguess, locRussian, locSpanish};

    TextToSpeech textToSpeech;

    private Spinner languageSpinner;

    private int spinnerIndex = 0;

    private String[] arrayOfTranslations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        languageSpinner = (Spinner) findViewById(R.id.lang_spinner);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                currentSpokenLang = languages[index];

                spinnerIndex = index;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textToSpeech = new TextToSpeech(this, this);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
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

    public void onTranslateClick(View view) {

        EditText translateEditText = (EditText) findViewById(R.id.editText);

        if (!isEmpty(translateEditText)) {

            Toast.makeText(this, "Getting Translations", Toast.LENGTH_SHORT).show();

            //new SaveTheFeed().execute();

            new GetXMLData().execute();

        } else {

            Toast.makeText(this, "Enter Words to Translate", Toast.LENGTH_SHORT).show();

        }
    }

    protected boolean isEmpty(EditText editText) {

        return editText.getText().toString().trim().length() == 0;

    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(currentSpokenLang);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Ready to speech", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void readTheText(View view) {

        textToSpeech.setLanguage(currentSpokenLang);

        if (arrayOfTranslations.length >= 9) {

            textToSpeech.speak(arrayOfTranslations[spinnerIndex+4], TextToSpeech.QUEUE_FLUSH, null);

        } else {
            Toast.makeText(this, "Translate text first", Toast.LENGTH_LONG).show();
        }
    }



    class SaveTheFeed extends AsyncTask<Void, Void, Void> {

        String jsonString = "";

        String result = "";


        @Override
        protected Void doInBackground(Void... voids) {

            EditText translateEditText = (EditText) findViewById(R.id.editText);

            String wordsToTranslate = translateEditText.getText().toString();

            wordsToTranslate = wordsToTranslate.replace(" ", "+");

            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            HttpPost httpPost = new HttpPost("http://newjustin.com/translateit.php?action=translations&english_words=" + wordsToTranslate);

            httpPost.setHeader("Content-type", "application/json");

            InputStream inputStream = null;


            try {

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                jsonString = sb.toString();

                JSONObject jObject = new JSONObject(jsonString);

                JSONArray jArray = jObject.getJSONArray("translations");

                outputTranslations(jArray);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            TextView translationTextView = (TextView) findViewById(R.id.translationTextView);

            translationTextView.setText(result);
        }

        protected void outputTranslations(JSONArray jsonArray) {

            String[] languages = {"arabic", "chinese", "danish", "dutch","french","german","italian","portuguese","russian","spanish"};

            try {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject translationObject = jsonArray.getJSONObject(i);

                    result = result + languages[i] + " : " + translationObject.getString(languages[i]) + "\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetXMLData extends AsyncTask<Void, Void, Void> {

        String stringToPrint = "";

        @Override
        protected Void doInBackground(Void... voids) {

            String xmlString = "";

            String wordsToTranslate = "";

            EditText translateEditText = (EditText) findViewById(R.id.editText);

            wordsToTranslate = translateEditText.getText().toString();

            wordsToTranslate = wordsToTranslate.replace(" ","+");

            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            HttpPost httpPost = new HttpPost("http://newjustin.com/translateit.php?action=xmltranslations&english_words=" + wordsToTranslate);

            httpPost.setHeader("Content-type", "text/xml");

            InputStream inputStream = null;

            try {
                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line=reader.readLine()) != null) {
                    sb.append(line);
                }

                xmlString = sb.toString();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(true);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(xmlString));

                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT) {

                    if ((eventType == XmlPullParser.START_TAG) && (!xpp.getName().equals("translations"))) {

                        stringToPrint = stringToPrint + xpp.getName() + " : ";

                    } else if (eventType == XmlPullParser.TEXT) {

                        stringToPrint = stringToPrint + xpp.getText() + "\n";
                    }

                    eventType = xpp.next();

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            TextView translationTextView = (TextView) findViewById(R.id.translationTextView);

            translationTextView.setMovementMethod(new ScrollingMovementMethod());

            String stringOfTranslations = stringToPrint.replaceAll("\\w+\\s:", "#");

            arrayOfTranslations = stringOfTranslations.split("#");

            translationTextView.setText(stringToPrint);
        }
    }

    public void ExceptSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_input_phrase));

        try {

            startActivityForResult(intent, 100);

        } catch (ActivityNotFoundException e) {

            Toast.makeText(this, getString(R.string.stt_not_supported_message), Toast.LENGTH_SHORT).show();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == 100) && (data != null) && (resultCode == RESULT_OK)) {

            ArrayList<String> spokenText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            EditText wordsEntered = (EditText) findViewById(R.id.editText);

            wordsEntered.setText(spokenText.get(0));
        }
    }
}
