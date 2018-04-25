package first_app.rcarb.a4cabs.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import first_app.rcarb.a4cabs.R;
import first_app.rcarb.a4cabs.objects.FlightObject;
import first_app.rcarb.a4cabs.objects.FlightSyncObject;

public class WidgetUpdateIntentService extends IntentService {

    public final static String UPDATE_FLIGHTS_WIDGET = "update-flights-widget";
    private Context mContext = this;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public WidgetUpdateIntentService() {
        super("WidgetUpdateIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_FLIGHTS_WIDGET.equals(action)) {
                handleActionUpdateWidgetCount();
            }
        }
    }

    public static void updateWidgetFlightCount(Context context){
        Intent intent = new Intent(context, WidgetUpdateIntentService.class);
        intent.setAction(UPDATE_FLIGHTS_WIDGET);
        context.startService(intent);
    }

    private void handleActionUpdateWidgetCount() {
        final ArrayList<FlightObject>flightArray = new ArrayList<>();
        final FlightSyncObject[] object = new FlightSyncObject[1];

        DatabaseReference firebaseReference = FirebaseDatabase.getInstance("https://flight-server.firebaseio.com")
                .getReference().child("flight");
        ChildEventListener childEventListsner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot objectArray = dataSnapshot.child("entireFlightsArray");
                flightArray.clear();
                for (DataSnapshot postSnapshot : objectArray.getChildren()) {
                    FlightObject flight = postSnapshot.getValue(FlightObject.class);
                    flightArray.add(flight);
                }
                object[0] = dataSnapshot.getValue(FlightSyncObject.class);

                String daysFlight = null;
                try {
                    daysFlight = object[0].getTotalFlights();
                } catch (Exception e) {
                    e.printStackTrace();
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext,
                            FlightWidgetProvider.class));
                    //Now update all the widgets
                    FlightWidgetProvider.updateFlightWidgets(mContext, appWidgetManager,getString(R.string.no_flights),appWidgetIds);

                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext,FlightWidgetProvider.class));
                //Now update all the widgets
                FlightWidgetProvider.updateFlightWidgets(mContext, appWidgetManager,daysFlight,appWidgetIds);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseReference.addChildEventListener(childEventListsner);

    }
}
