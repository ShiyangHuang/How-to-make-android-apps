package huang.test.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by shiyanghuang on 15/5/1.
 */
public class AlertReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context, "Times up", "5 Seconds has passed", "Alert");

    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert) {

        PendingIntent notificIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.abc_btn_check_to_on_mtrl_015)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);

        mBuilder.setContentIntent(notificIntent);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
