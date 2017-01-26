package com.google.developer.udacityalumni.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.developer.udacityalumni.R;
import com.google.developer.udacityalumni.data.AlumContract;
import com.google.developer.udacityalumni.fragment.ArticleFragment;

/**
 * Created by Sunitha Premjee on 1/25/2017.
 */


public class UdacityAlumniWidgetService extends RemoteViewsService {


    public static final String TAG = "UAlumniWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.i(TAG, "UdacityAlumniWidgetService");
        return new UdacityAlumniRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class UdacityAlumniRemoteViewsFactory implements
            RemoteViewsService.RemoteViewsFactory {


        private Cursor mCursor = null;

        private Context mContext;
        private int mAppWidgetId;

        public UdacityAlumniRemoteViewsFactory(Context context, Intent intent) {

            Log.i(TAG, "UdacityAlumniRemoteViewsFactory");
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Initialize the data set.
        public void onCreate() {

            mCursor = getContentResolver().query(AlumContract.ArticleEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDataSetChanged() {
            mCursor = getContentResolver().query(AlumContract.ArticleEntry.CONTENT_URI, null, null, null, AlumContract.ArticleEntry.COL_ARTICLE_ID + " desc");

        }

        @Override
        public void onDestroy() {

            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }

        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }


        // Given the position (index) of a WidgetItem in the array, use the item's text value in
        // combination with the app widget item XML file to construct a RemoteViews object.
        public RemoteViews getViewAt(int position) {

            Log.i(TAG, "getViewAt");

            if( mCursor == null || mCursor.getCount() == 0)
                return null;
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.udacityalumni_appwidget_item );
            if( mCursor.moveToPosition(position) ) {
                Log.i(TAG, mCursor.getString(ArticleFragment.IND_TITLE));
                remoteViews.setTextViewText(R.id.Alumni_Title,mCursor.getString(ArticleFragment.IND_USER_NAME ));


                remoteViews.setTextViewText(R.id.Alumni_Article_Title,mCursor.getString(ArticleFragment.IND_TITLE));
            }
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if( mCursor.moveToPosition(position) ){

                return mCursor.getLong( ArticleFragment.IND_ARTICLE_ID );
            }
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

    }
}