package com.pehlaj.chairlift.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.pehlaj.chairlift.R;
import com.pehlaj.chairlift.activities.BusDetailsActivity;
import com.pehlaj.chairlift.activities.BusListActivity;
import com.pehlaj.chairlift.constants.Constants;
import com.pehlaj.chairlift.entities.Bus;

/**
 * @author Pehlaj Rai
 * @since 04-Nov-17.
 */
public class NotificationUtils {

    public static void showNotification(Context context, Bus bus) {
        Notification.Builder builder =
                new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setTicker(bus.getRegistrationNumber())
                        .setContentText(bus.toString());

        Intent intent = new Intent(context, BusDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_BUS, bus);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack for the Intent (but not the Intent itself)
        taskStackBuilder.addParentStack(BusListActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        taskStackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.


        Notification notification = builder.build();

        //notification.defaults |= Notification.DEFAULT_ALL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }
}
