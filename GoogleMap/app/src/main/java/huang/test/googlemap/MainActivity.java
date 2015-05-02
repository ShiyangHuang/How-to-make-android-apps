package huang.test.googlemap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {

    static final LatLng DerekPos = new LatLng(40,-79);

    private GoogleMap googleMap;

    EditText addressEditText, finalAddressEditText;

    LatLng addressPos, finalAddressPos;

    Marker addressMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressEditText = (EditText) findViewById(R.id.addressEditText);
        finalAddressEditText = (EditText) findViewById(R.id.finalAddressEditText);


        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }

            // Show a satellite map with roads
            /* MAP_TYPE_NORMAL: Basic map with roads.
            MAP_TYPE_SATELLITE: Satellite view with roads.
            MAP_TYPE_TERRAIN: Terrain view without roads.
            */

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            // Place dot on current location
            googleMap.setMyLocationEnabled(true);

            // Turns traffic layer on
            googleMap.setTrafficEnabled(true);

            // Enables indoor maps
            googleMap.setIndoorEnabled(true);

            // Turns on 3D buildings
            googleMap.setBuildingsEnabled(true);

            // Show Zoom buttons
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            // Create a marker in the map at a given position with a title
            Marker marker = googleMap.addMarker(new MarkerOptions().
                    position(DerekPos).title("Hello"));

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
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

    public void showAddressMarker(View view) {

        String newAddress = addressEditText.getText().toString();

        if (newAddress != null) {

            new PlaceAMarker().execute(newAddress);
        }
    }

    public void getDirections(View view) {

        String startingAddress = addressEditText.getText().toString();
        String finalAddress = finalAddressEditText.getText().toString();

        if((startingAddress.equals("")) || (finalAddress.equals(""))) {

            Toast.makeText(this, "Enter a starting & endign address", Toast.LENGTH_LONG).show();

        } else {

            new GetDirections().execute(startingAddress, finalAddress);

        }

    }

    class PlaceAMarker extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String startAddress = strings[0];

            startAddress = startAddress.replaceAll(" ", "%20");

            getLatLng(startAddress, false);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            addressMarker = googleMap.addMarker(new MarkerOptions().position(addressPos).title("Address"));


        }
    }

    class GetDirections extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String startAddress = strings[0];

            startAddress = startAddress.replaceAll(" ","%20");

            getLatLng(startAddress, false);

            String endingAddress = strings[1];

            endingAddress = endingAddress.replaceAll(" ", "%20");

            getLatLng(endingAddress, true);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String geoUriString = "http://maps.google.com/maps?addr=" + addressPos.latitude + ","
                    + addressPos.longitude + "&daddr=" + finalAddressPos.latitude + "," +
                    finalAddressPos.longitude;

            Intent mapCall = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUriString));

            startActivity(mapCall);
        }
    }

    protected void getLatLng(String address, boolean setDestination) {

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false";

        HttpGet httpGet = new HttpGet(uri);

        HttpClient client = new DefaultHttpClient();

        HttpResponse response;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();

            InputStream stream = entity.getContent();

            int byteData;

            while ((byteData = stream.read()) != -1) {
                stringBuilder.append((char)byteData);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        double lat = 0.0, lng = 0.0;

        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            if (setDestination) {

                finalAddressPos = new LatLng(lat, lng);

            } else {

                addressPos = new LatLng(lat, lng);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
