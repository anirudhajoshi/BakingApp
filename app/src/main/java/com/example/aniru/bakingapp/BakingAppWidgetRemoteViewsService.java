package com.example.aniru.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.aniru.bakingapp.data.Recipe;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by aniru on 8/20/2017.
 */

public class BakingAppWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Timber.d("BakingAppWidgetRemoteViewsService: onGetViewFactory()");

        return new BakingAppWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
