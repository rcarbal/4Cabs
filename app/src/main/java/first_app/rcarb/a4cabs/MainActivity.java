package first_app.rcarb.a4cabs;

import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import first_app.rcarb.a4cabs.loaders.PrepareFlightArrayLoader;
import first_app.rcarb.a4cabs.objects.FlightObject;
import first_app.rcarb.a4cabs.objects.FlightSyncObject;
import first_app.rcarb.a4cabs.utilities.ActionStrings;

public class MainActivity extends AppCompatActivity {
    private final static int GET_FLIGHT_TIME_FRAME_LOADER = 1;
    private AdView mAdView;

    private Button thirtyButton;
    private Button oneHourButton;
    private Button twoHour;
    private Button mListActivityButton;

    private TextView mFligtsLanding;
    private TextView mDaysFlight;
    private TextView mUpdated;

    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListsner;

    private int mTimeFrameSelected;
    private FlightSyncObject mSyncObject;
    private ArrayList<FlightObject>mFlightArray;
    private ArrayList<FlightObject>mSendFlightsToActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.ABMOB_ID));
        mFlightArray = new ArrayList<>();

        thirtyButton = findViewById(R.id.thirty_button);
        oneHourButton = findViewById(R.id.one_hour_button);
        twoHour = findViewById(R.id.two_hour_button);
        mListActivityButton = findViewById(R.id.list_activity_button);
        mListActivityButton.setClickable(false);
        mFligtsLanding = findViewById(R.id.flights_landing);
        mDaysFlight = findViewById(R.id.days_flight);
        mUpdated = findViewById(R.id.updated);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest =  new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        connectToDatabase();
    }

    private void connectToDatabase(){

        mFirebaseReference = FirebaseDatabase.getInstance("https://flight-server.firebaseio.com")
                .getReference().child("flight");
        mChildEventListsner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot objectArray = dataSnapshot.child("entireFlightsArray");
                mFlightArray.clear();
                for (DataSnapshot postSnapshot: objectArray.getChildren()){
                    FlightObject flight = postSnapshot.getValue(FlightObject.class);
                    mFlightArray.add(flight);
                }
                FlightSyncObject object = dataSnapshot.getValue(FlightSyncObject.class);
                if (mFlightArray.size()>0){
                    setSelectedButton();
                }

                mSyncObject = object;

                assert object != null;
                switch (mTimeFrameSelected){
                    case 0:
                        int minutes = object.getThirty();
                        mFligtsLanding.setText(""+minutes);
                        break;
                    case 1:
                        int hour = object.getHour();
                        mFligtsLanding.setText(""+hour);
                        break;
                    case 2:
                        int twoHour = object.getTwoHour();
                        mFligtsLanding.setText(""+twoHour);
                }
                String days = object.getTotalFlights();
                String updated = object.getStamp();
                mDaysFlight.setText(days+ " "+" Flights Today");
                mUpdated.setText("Updated: "+ updated);
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

    private void setSelectedButton(){
        thirtyButton.setTextColor(Color.parseColor("#01579B"));
        thirtyButton.setTypeface(Typeface.DEFAULT_BOLD);
        mTimeFrameSelected =0;
        if (mFlightArray != null && mFlightArray.size()>0){
            setFlightDataToCorrectTimeFrame();
        }
    }

    public void thirtyClicked(View view){

        thirtyButton.setTextColor(Color.parseColor("#01579B"));
        thirtyButton.setTypeface(Typeface.DEFAULT_BOLD);
        oneHourButton.setTextColor(Color.parseColor("#BDBDBD"));
        oneHourButton.setTypeface(null,Typeface.NORMAL);
        twoHour.setTextColor(Color.parseColor("#BDBDBD"));
        twoHour.setTypeface(null,Typeface.NORMAL);
        mTimeFrameSelected = 0;
        if (mFlightArray != null && mFlightArray.size()>0){
            setFlightDataToCorrectTimeFrame();
        }
        if (mSyncObject!=null){
            int sync = mSyncObject.getThirty();
            mFligtsLanding.setText(""+sync);
        }else {
            Toast.makeText(this, "Server not Synced", Toast.LENGTH_SHORT).show();
        }
    }

    public void oneHourClicked(View view){
        thirtyButton.setTextColor(Color.parseColor("#BDBDBD"));
        thirtyButton.setTypeface(null,Typeface.NORMAL);
        oneHourButton.setTextColor(Color.parseColor("#01579B"));
        oneHourButton.setTypeface(Typeface.DEFAULT_BOLD);
        twoHour.setTextColor(Color.parseColor("#BDBDBD"));
        twoHour.setTypeface(null,Typeface.NORMAL);
        mTimeFrameSelected = 1;
        if (mFlightArray != null && mFlightArray.size()>0){
            setFlightDataToCorrectTimeFrame();
        }
        if (mSyncObject!=null){
            int sync = mSyncObject.getHour();
            mFligtsLanding.setText(""+sync);
        }else {
            Toast.makeText(this, "Server not Synced", Toast.LENGTH_SHORT).show();
        }
    }

    public void twoHourClicked(View view){
        thirtyButton.setTextColor(Color.parseColor("#BDBDBD"));
        thirtyButton.setTypeface(null,Typeface.NORMAL);
        oneHourButton.setTextColor(Color.parseColor("#BDBDBD"));
        oneHourButton.setTypeface(null,Typeface.NORMAL);
        twoHour.setTextColor(Color.parseColor("#01579B"));
        twoHour.setTypeface(Typeface.DEFAULT_BOLD);
        mTimeFrameSelected = 2;
        if (mFlightArray != null && mFlightArray.size()>0){
            setFlightDataToCorrectTimeFrame();
        }
        if (mSyncObject!=null){
            int sync = mSyncObject.getTwoHour();
            mFligtsLanding.setText(""+sync);
        }else {
            Toast.makeText(this, "Server not Synced", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFlightDataToCorrectTimeFrame(){
        android.support.v4.app.LoaderManager manager = getSupportLoaderManager();
        android.support.v4.content.Loader<Object> getFLightTimeFrame = manager.getLoader(
                GET_FLIGHT_TIME_FRAME_LOADER);
        if (getFLightTimeFrame == null){
            getSupportLoaderManager().initLoader(GET_FLIGHT_TIME_FRAME_LOADER,
                    null,
                    prepareTimeframe);
        }else {
            getSupportLoaderManager().restartLoader(GET_FLIGHT_TIME_FRAME_LOADER,
                    null,
                    prepareTimeframe);
        }

    }
    public void startFlightListActivity(View view){
        if (mListActivityButton.isClickable()){
            Intent intent = new Intent(this, FlightListActivity.class);
            intent.putExtra(ActionStrings.SEND_FLIGHTS_ARRAYLIST, mSendFlightsToActivity);
            intent.putExtra(ActionStrings.SEND_TIME_FRAME_SELECTED, mTimeFrameSelected);
            startActivity(intent);
        }else {
            Toast.makeText(this, "No flights to show", Toast.LENGTH_SHORT).show();
        }

    }
    private final android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<FlightObject>> prepareTimeframe =
            new LoaderManager.LoaderCallbacks<ArrayList<FlightObject>>() {
                @NonNull
                @Override
                public android.support.v4.content.Loader<ArrayList<FlightObject>> onCreateLoader(int id, @Nullable Bundle args) {
                    return new PrepareFlightArrayLoader(MainActivity.this, mFlightArray,mTimeFrameSelected);
                }

                @Override
                public void onLoadFinished(@NonNull android.support.v4.content.Loader<ArrayList<FlightObject>> loader,
                                           ArrayList<FlightObject> data) {
                    mSendFlightsToActivity = new ArrayList<>();
                    mSendFlightsToActivity.addAll(data);
                    if (mSendFlightsToActivity!= null && mSendFlightsToActivity.size() > 0){
                        mListActivityButton.setClickable(true);
                    }

                }

                @Override
                public void onLoaderReset(@NonNull android.support.v4.content.Loader<ArrayList<FlightObject>> loader) {

                }
            };

}
