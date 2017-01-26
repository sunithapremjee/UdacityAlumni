package com.google.developer.udacityalumni.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.activity.MainActivity;



public class UdacityAlumniAppWidgetProvider extends AppWidgetProvider {


    public static final String TAG = "AlumniAppWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

         Log.d(TAG, "onUpdate");


        for (int appWidgetId: appWidgetIds ) {

            updateAppWidget(context, appWidgetManager, appWidgetId );

        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.widget_text);

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.udacityalumni_appwidget);
        remoteViews.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent = new Intent( context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent( R.id.widget_container, pendingIntent );


        Intent remoteViewsServiceIntent = new Intent( context, UdacityAlumniWidgetService.class );
        remoteViewsServiceIntent.putExtra( AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId );
        remoteViewsServiceIntent.setData( Uri.parse( remoteViewsServiceIntent.toUri( Intent.URI_INTENT_SCHEME ) ) );
        remoteViews.setRemoteAdapter( R.id.widget_list, remoteViewsServiceIntent );

        appWidgetManager.updateAppWidget( appWidgetId, remoteViews );
    }
}
