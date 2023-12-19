package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingActivity extends Activity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    final String LOG_TAG = "myLogs";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_TEXT = "widget_text_";
    public final static String WIDGET_LVL = "widget_lvl_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate config");

        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_setting);
    }


    public void onClick(View v) {
        int selLvl = ((RadioGroup) findViewById(R.id.rgLvl))
                .getCheckedRadioButtonId();
        CheckBox checkBox1 = findViewById(R.id.checkBox);
        CheckBox checkBox2 = findViewById(R.id.checkBox2);
        CheckBox checkBox3 = findViewById(R.id.checkBox3);
        CheckBox checkBox4 = findViewById(R.id.checkBox4);
        int lvl=0;
        if(checkBox1.isChecked()){
            lvl=lvl|1;
        }
        if(checkBox2.isChecked()){
            lvl=lvl|2;
        }
        if(checkBox3.isChecked()){
            lvl=lvl|4;
        }
        if(checkBox4.isChecked()){
            lvl=lvl|8;
        }
        if(lvl!=0) {
            // Записываем значения с экрана в Preferences
            SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(WIDGET_LVL + widgetID, lvl);
            editor.commit();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            NewAppWidget.updateAppWidget(this, appWidgetManager, widgetID);
            // положительный ответ
            setResult(RESULT_OK, resultValue);

            Log.d(LOG_TAG, "finish config lvl = " + lvl);
            Log.d(LOG_TAG, "finish config " + widgetID);
            finish();
        }
    }




}