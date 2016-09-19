package com.karolis.agentStatsWidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

public class WidgetConfigure extends Activity {
    private static final String PREFS_NAME = "Configuration";
    private static final String PREF_PREFIX_KEY = "API_KEY";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private EditText mApiKey;
    private TextInputLayout mApiKeyLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        setContentView(R.layout.appwidget_configure);
        mApiKey = (EditText) findViewById(R.id.input_api_key);
        mApiKeyLayout = (TextInputLayout) findViewById(R.id.input_layout_api_key);
        findViewById(R.id.save).setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            mApiKeyLayout.setErrorEnabled(false);
            final Context context = WidgetConfigure.this;
            String apiKey = mApiKey.getText().toString();
            if(apiKey.length() < 20) {
                mApiKeyLayout.setErrorEnabled(true);
                mApiKeyLayout.setError(getString(R.string.api_key_too_short));
                return;
            }

            saveApiKey(context, mAppWidgetId, apiKey);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            MainAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, apiKey);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    static void saveApiKey(Context context, int appWidgetId, String apiKey) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, apiKey);
        prefs.apply();
    }

    static String loadApiKey(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String apiKey = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (apiKey != null) {
            return apiKey;
        } else {
            return "";
        }
    }
}
