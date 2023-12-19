package com.example.lab6;

import static android.content.Context.MODE_PRIVATE;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {


    final static String LOG_TAG = "myLogs";
    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_TEXT = "widget_text_";
    public final static String WIDGET_LVL = "widget_lvl_";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(LOG_TAG, "HELOOoooooo" );
        SharedPreferences sp = context.getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        int lvl = sp.getInt(SettingActivity.WIDGET_LVL
                + appWidgetId, 0);
        Log.d(LOG_TAG, "get lvl = " + lvl);
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        boolean canPlus = (lvl&1)==1;
        boolean canMinus = (lvl&2)==2;
        boolean canDiv = (lvl&4)==4;
        boolean canMulti = (lvl&8)==8;
        ArrayList<Boolean> deistv = new ArrayList<>();
        deistv.add(canPlus);
        deistv.add(canMinus);
        deistv.add(canDiv);
        deistv.add(canMulti);
        int count=0;
        for (boolean itt:deistv
             ) {
            if(itt){
                count++;
            }
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, 12);
        int vibor= randomNum/(12/count);
        int i=-1;
        int ind=0;
        while(vibor!=i){
            if(deistv.get(ind)){
                i++;
            }
            ind++;
        }
        --ind;
        int randomNum1 = ThreadLocalRandom.current().nextInt(10, 100);
        int randomNum2 = ThreadLocalRandom.current().nextInt(10, 100);
        int realAns=0;
        String ask=randomNum1+" ";
        if(ind==0){
            ask+="+ ";
            realAns=randomNum1+randomNum2;
        }
        if(ind==1){
            ask+="- ";
            realAns=randomNum1-randomNum2;
        }
        if(ind==2){
            ask+="/ ";
            realAns=randomNum1/randomNum2;
        }
        if(ind==3){
            ask+="* ";
            realAns=randomNum1*randomNum2;
        }
        String var1= (Integer.valueOf(realAns)).toString();
        int val1=ThreadLocalRandom.current().nextInt(realAns-5, realAns+6);
        while(val1==realAns){
            val1=ThreadLocalRandom.current().nextInt(realAns-5, realAns+6);
        }
        int val2=ThreadLocalRandom.current().nextInt(realAns-5, realAns+6);
        while(val2==realAns ||val2==val1){
            val2=ThreadLocalRandom.current().nextInt(realAns-5, realAns+6);
        }
        String var2= (Integer.valueOf(realAns+1)).toString();
        String var3= (Integer.valueOf(realAns-1)).toString();
        ArrayList<String> vars = new ArrayList<>();
        vars.add(var1);
        vars.add(var2);
        vars.add(var3);
        Collections.shuffle(vars);

        Intent configIntent = new Intent(context, SettingActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pIntent = PendingIntent.getActivity(context, appWidgetId,
                configIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.button4, pIntent);
        // Обновление виджета (вторая зона)
        Intent updateIntent = new Intent(context, NewAppWidget.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                new int[] { appWidgetId });
        pIntent = PendingIntent.getBroadcast(context, appWidgetId, updateIntent, PendingIntent.FLAG_IMMUTABLE);


        views.setOnClickPendingIntent(R.id.button, null);
        views.setOnClickPendingIntent(R.id.button2, null);
        views.setOnClickPendingIntent(R.id.button3, null);

        if(Objects.equals(vars.get(0), var1)){
            views.setOnClickPendingIntent(R.id.button, pIntent);

        }
        if(Objects.equals(vars.get(1), var1)){

            views.setOnClickPendingIntent(R.id.button2, pIntent);
        }
        if(Objects.equals(vars.get(2), var1)){

            views.setOnClickPendingIntent(R.id.button3, pIntent);
        }


        ask+=randomNum2+" = ?";

        views.setTextViewText(R.id.appwidget_text,ask );
        views.setTextViewText(R.id.button,vars.get(0) );
        views.setTextViewText(R.id.button2,vars.get(1) );
        views.setTextViewText(R.id.button3,vars.get(2) );
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            Log.d("myLogs", "hi" );
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Chain up to the super class so the onEnabled, etc callbacks get dispatched
        super.onReceive(context, intent);

        // Handle a different Intent
        Log.d("myLogs", "onReceive()" + intent.getAction());

    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // Удаляем Preferences
        SharedPreferences.Editor editor = context.getSharedPreferences(
                SettingActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(SettingActivity.WIDGET_LVL + widgetID);
        }
        editor.commit();
    }


}