package com.example.aniru.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.aniru.bakingapp.data.Recipe;

import timber.log.Timber;

/**
 * Created by aniru on 8/20/2017.
 */

public class BakingAppRecipeCollectionWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Timber.d("BakingAppRecipeCollectionWidgetProvider: onUpdate()");

        // Update each of the app widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {

            // Set up the intent that starts the BakingAppRemoteViewsService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, BakingAppWidgetRemoteViewsService.class);
            // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bakingapp_widget);

            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects to a RemoteViewsService through the specified intent to populate the data
            views.setRemoteAdapter(R.id.recipeListView, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews
            // object above.
            views.setEmptyView(R.id.recipeListView, R.id.empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }
}
