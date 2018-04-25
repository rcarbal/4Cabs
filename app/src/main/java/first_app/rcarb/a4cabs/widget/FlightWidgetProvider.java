package first_app.rcarb.a4cabs.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import first_app.rcarb.a4cabs.MainActivity;
import first_app.rcarb.a4cabs.R;

/**
 * Implementation of App Widget functionality.
 */
public class FlightWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String flightCount, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.flight_widget_provider);
        views.setTextViewText(R.id.widget_count_tv, flightCount);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
       // WidgetUpdateIntentService.updateWidgetFlightCount(context);
        String open = context.getString(R.string.please_open_app_ph);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,open, appWidgetId);
        }


    }

    public static void updateFlightWidgets(Context context, AppWidgetManager appWidgetManager,
                                           String flightCount, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager,flightCount, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

