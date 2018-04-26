package first_app.rcarb.a4cabs;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import first_app.rcarb.a4cabs.loaders.CheckConnectionLoader;
import first_app.rcarb.a4cabs.loaders.PrepareFlightArrayLoader;
import first_app.rcarb.a4cabs.objects.FlightObject;
import first_app.rcarb.a4cabs.objects.FlightSyncObject;
import first_app.rcarb.a4cabs.utilities.ActionStrings;
import first_app.rcarb.a4cabs.widget.WidgetUpdateIntentService;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;

    private final static int GET_FLIGHT_TIME_FRAME_LOADER = 1;
    private final static int CHECK_NETWORK_CONNECTION = 2;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.thirty_button)
    Button thirtyButton;
    @BindView(R.id.one_hour_button)
    Button oneHourButton;
    @BindView(R.id.two_hour_button)
    Button twoHour;
    @BindView(R.id.list_activity_button)
    Button mListActivityButton;
    @BindView(R.id.flights_landing)
    TextView mFligtsLanding;
    @BindView(R.id.days_flight)
    TextView mDaysFlight;
    @BindView(R.id.updated)
    TextView mUpdated;

    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListsner;

    private int mTimeFrameSelected;
    private FlightSyncObject mSyncObject;
    private ArrayList<FlightObject> mFlightArray;
    private ArrayList<FlightObject> mSendFlightsToActivity;
    private int mSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            mTimeFrameSelected = savedInstanceState.getInt(ActionStrings.SAVE_TIME_SELECTED);
        } else {
            mTimeFrameSelected = -1;
            mSync = -1;
        }

        MobileAds.initialize(this, getString(R.string.ABMOB_ID));
        mFlightArray = new ArrayList<>();
        mListActivityButton.setClickable(false);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        checkInternetConnection();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ActionStrings.SAVE_TIME_SELECTED, mTimeFrameSelected);
    }
    private void addRequest(){
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }

    //If no Connection was found
    private void noConnection() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content),
                        R.string.retry_text, BaseTransientBottomBar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.hit_retry, new SnackListener());
        snackbar.show();
    }

    private void connectToDatabase() {
        addRequest();
        mFirebaseReference = FirebaseDatabase.getInstance("https://flight-server.firebaseio.com")
                .getReference().child("flight");
        mChildEventListsner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot objectArray = dataSnapshot.child("entireFlightsArray");
                mFlightArray.clear();
                for (DataSnapshot postSnapshot : objectArray.getChildren()) {
                    FlightObject flight = postSnapshot.getValue(FlightObject.class);
                    mFlightArray.add(flight);
                }
                FlightSyncObject object = dataSnapshot.getValue(FlightSyncObject.class);
                if (mFlightArray.size() > 0) {
                    setSelectedButton();
                }

                mSyncObject = object;

                assert object != null;
                switch (mTimeFrameSelected) {
                    case 0:
                        int minutes = object.getThirty();
                        mFligtsLanding.setText(String.valueOf(minutes));
                        break;
                    case 1:
                        int hour = object.getHour();
                        mFligtsLanding.setText(String.valueOf(hour));
                        break;
                    case 2:
                        int twoHour = object.getTwoHour();
                        mFligtsLanding.setText(String.valueOf(twoHour));
                }
                String days = object.getTotalFlights();
                WidgetUpdateIntentService.updateWidgetFlightCount(MainActivity.this);
                String updated = object.getStamp();
                String daysFlight = days + " " + getString(R.string.flights_today_string);
                mDaysFlight.setText(daysFlight);
                String updatedString = getString(R.string.updated_string)+ ": "+updated;
                mUpdated.setText(updatedString);
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
        mFirebaseReference.addChildEventListener(mChildEventListsner);
    }

    private void setSelectedButton() {
        if (mTimeFrameSelected == -1 || mTimeFrameSelected == 0) {
            thirtyButton.setTextColor(Color.parseColor("#01579B"));
            thirtyButton.setTypeface(Typeface.DEFAULT_BOLD);
            mTimeFrameSelected = 0;
            if (mFlightArray != null && mFlightArray.size() > 0) {
                setFlightDataToCorrectTimeFrame();
            }
        } else if (mTimeFrameSelected == 1) {
            oneHourSelected();
        } else if (mTimeFrameSelected == 2) {
            twohoursSelected();
        }
    }

    public void thirtyClicked(View view) {

        thirtyButton.setTextColor(Color.parseColor("#01579B"));
        thirtyButton.setTypeface(Typeface.DEFAULT_BOLD);
        oneHourButton.setTextColor(Color.parseColor("#BDBDBD"));
        oneHourButton.setTypeface(null, Typeface.NORMAL);
        twoHour.setTextColor(Color.parseColor("#BDBDBD"));
        twoHour.setTypeface(null, Typeface.NORMAL);
        mTimeFrameSelected = 0;
        if (mFlightArray != null && mFlightArray.size() > 0) {
            setFlightDataToCorrectTimeFrame();
        }
        if (mSyncObject != null) {
            if (mSync == -1) {
                mSync = mSyncObject.getThirty();
            }
            String sync = String.valueOf(mSync);
            mFligtsLanding.setText(sync);
        }
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.thirty_minutes));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    public void oneHourClicked(View view) {
        oneHourSelected();
    }

    private void oneHourSelected() {
        thirtyButton.setTextColor(Color.parseColor("#BDBDBD"));
        thirtyButton.setTypeface(null, Typeface.NORMAL);
        oneHourButton.setTextColor(Color.parseColor("#01579B"));
        oneHourButton.setTypeface(Typeface.DEFAULT_BOLD);
        twoHour.setTextColor(Color.parseColor("#BDBDBD"));
        twoHour.setTypeface(null, Typeface.NORMAL);
        mTimeFrameSelected = 1;
        if (mFlightArray != null && mFlightArray.size() > 0) {
            setFlightDataToCorrectTimeFrame();
        }
        if (mSyncObject != null) {
            mSync = mSyncObject.getHour();
            String sync = String.valueOf(mSync);
            mFligtsLanding.setText(sync);
        }
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.hour));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void twoHourClicked(View view) {
        twohoursSelected();
    }

    private void twohoursSelected() {
        thirtyButton.setTextColor(Color.parseColor("#BDBDBD"));
        thirtyButton.setTypeface(null, Typeface.NORMAL);
        oneHourButton.setTextColor(Color.parseColor("#BDBDBD"));
        oneHourButton.setTypeface(null, Typeface.NORMAL);
        twoHour.setTextColor(Color.parseColor("#01579B"));
        twoHour.setTypeface(Typeface.DEFAULT_BOLD);
        mTimeFrameSelected = 2;
        if (mFlightArray != null && mFlightArray.size() > 0) {
            setFlightDataToCorrectTimeFrame();
        }
        if (mSyncObject != null) {
            mSync = mSyncObject.getTwoHour();
            String sync = String.valueOf(mSync);
            mFligtsLanding.setText(sync);
        }
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getString(R.string.two_hours));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    public void startFlightListActivity(View view) {
        if (mListActivityButton.isClickable()) {
            Intent intent = new Intent(this, FlightListActivity.class);
            intent.putExtra(ActionStrings.SEND_FLIGHTS_ARRAYLIST, mSendFlightsToActivity);
            intent.putExtra(ActionStrings.SEND_TIME_FRAME_SELECTED, mTimeFrameSelected);
            Bundle bundle = ActivityOptions
                    .makeSceneTransitionAnimation(this)
                    .toBundle();
            startActivity(intent, bundle);
        } else {
            Toast.makeText(this, "No flights to show", Toast.LENGTH_SHORT).show();
        }
    }

    //LOADER check connection
    private void checkInternetConnection() {
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        android.support.v4.content.Loader<Boolean> checkConnection = loaderManager.getLoader(CHECK_NETWORK_CONNECTION);
        if (checkConnection == null) {
            getSupportLoaderManager().initLoader(CHECK_NETWORK_CONNECTION,
                    null,
                    checkNetworkConnection);
        } else {
            loaderManager.restartLoader(CHECK_NETWORK_CONNECTION, null, checkNetworkConnection);
        }
    }

    //LOADER get flights
    private void setFlightDataToCorrectTimeFrame() {
        android.support.v4.app.LoaderManager manager = getSupportLoaderManager();
        android.support.v4.content.Loader<Object> getFLightTimeFrame = manager.getLoader(
                GET_FLIGHT_TIME_FRAME_LOADER);
        if (getFLightTimeFrame == null) {
            getSupportLoaderManager().initLoader(GET_FLIGHT_TIME_FRAME_LOADER,
                    null,
                    prepareTimeframe);
        } else {
            getSupportLoaderManager().restartLoader(GET_FLIGHT_TIME_FRAME_LOADER,
                    null,
                    prepareTimeframe);
        }

    }

    //<----------------------------------------LOADERS---------------------------------------------->
    private final LoaderManager.LoaderCallbacks<Boolean> checkNetworkConnection =
            new LoaderManager.LoaderCallbacks<Boolean>() {
                @NonNull
                @Override
                public android.support.v4.content.Loader<Boolean> onCreateLoader(int id, @Nullable Bundle args) {
                    return new CheckConnectionLoader(MainActivity.this);
                }

                @Override
                public void onLoadFinished(@NonNull android.support.v4.content.Loader<Boolean> loader, Boolean data) {
                    if (data) {
                        connectToDatabase();
                    } else {
                        noConnection();
                    }
                }

                @Override
                public void onLoaderReset(@NonNull android.support.v4.content.Loader<Boolean> loader) {

                }
            };

    private final android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<FlightObject>> prepareTimeframe =
            new LoaderManager.LoaderCallbacks<ArrayList<FlightObject>>() {
                @NonNull
                @Override
                public android.support.v4.content.Loader<ArrayList<FlightObject>> onCreateLoader(int id, @Nullable Bundle args) {
                    return new PrepareFlightArrayLoader(MainActivity.this, mFlightArray, mTimeFrameSelected);
                }

                @Override
                public void onLoadFinished(@NonNull android.support.v4.content.Loader<ArrayList<FlightObject>> loader,
                                           ArrayList<FlightObject> data) {
                    mSendFlightsToActivity = new ArrayList<>();
                    mSendFlightsToActivity.addAll(data);
                    if (mSendFlightsToActivity != null && mSendFlightsToActivity.size() > 0) {
                        mListActivityButton.setClickable(true);
                        String flightSize = String.valueOf(mSendFlightsToActivity.size());
                        mFligtsLanding.setText(flightSize);
                    }

                }

                @Override
                public void onLoaderReset(@NonNull android.support.v4.content.Loader<ArrayList<FlightObject>> loader) {

                }
            };

    class SnackListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            checkInternetConnection();
        }
    }

}
