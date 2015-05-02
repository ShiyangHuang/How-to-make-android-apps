package huang.test.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity {

    Button showNotification, stopNotification, alartButton;

    NotificationManager notificationManager;

    boolean isNotificActive = false;

    int notifID = 33;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showNotification = (Button) findViewById(R.id.show_notification);
        stopNotification = (Button) findViewById(R.id.show_notification);
        alartButton = (Button) findViewById(R.id.alert_in_5_second);


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

    public void showNotification(View view) {

        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText("Alert new message")
                .setTicker("Alert New Message")
                .setSmallIcon(R.drawable.abc_ab_share_pack_mtrl_alpha);

        Intent moreInfoIntent = new Intent(this, MoreInfoNotification.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

        taskStackBuilder.addParentStack(MoreInfoNotification.class);

        taskStackBuilder.addNextIntent(moreInfoIntent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificBuilder.setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notifID, notificBuilder.build());

        isNotificActive = true;
    }

    public void stopNotification(View view) {

        if (isNotificActive) {

            notificationManager.cancel(notifID);
        }
    }

    public void onAlarm(View view) {

        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;

        Intent alertIntent = new Intent(this, AlertReceiver.class);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));

    }
}
