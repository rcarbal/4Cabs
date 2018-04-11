package first_app.rcarb.a4cabs;

import android.graphics.Color;
import android.graphics.Typeface;
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

import first_app.rcarb.a4cabs.Objects.FlightSyncObject;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

    private Button thirtyButton;
    private Button oneHourButton;
    private Button twoHour;

    private TextView mFligtsLanding;
    private TextView mDaysFlight;
    private TextView mUpdated;

    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListsner;

    private int mTimeFrameSelected;
    private FlightSyncObject mSyncObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.ABMOB_ID));

        thirtyButton = findViewById(R.id.thirty_button);
        oneHourButton = findViewById(R.id.one_hour_button);
        twoHour = findViewById(R.id.two_hour_button);
        mFligtsLanding = findViewById(R.id.flights_landing);
        mDaysFlight = findViewById(R.id.days_flight);
        mUpdated = findViewById(R.id.updated);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest =  new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        mFirebaseReference = FirebaseDatabase.getInstance("")
                .getReference().child("flight");
        mChildEventListsner = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FlightSyncObject object = dataSnapshot.getValue(FlightSyncObject.class);
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

        setSelectedButton();
    }

    private void setSelectedButton(){
        thirtyButton.setTextColor(Color.parseColor("#01579B"));
        thirtyButton.setTypeface(Typeface.DEFAULT_BOLD);
        mTimeFrameSelected =0;

    }

    public void thirtyClicked(View view){
        thirtyButton.setTextColor(Color.parseColor("#01579B"));
        thirtyButton.setTypeface(Typeface.DEFAULT_BOLD);
        oneHourButton.setTextColor(Color.parseColor("#BDBDBD"));
        oneHourButton.setTypeface(null,Typeface.NORMAL);
        twoHour.setTextColor(Color.parseColor("#BDBDBD"));
        twoHour.setTypeface(null,Typeface.NORMAL);
        mTimeFrameSelected = 0;
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
        if (mSyncObject!=null){
            int sync = mSyncObject.getTwoHour();
            mFligtsLanding.setText(""+sync);
        }else {
            Toast.makeText(this, "Server not Synced", Toast.LENGTH_SHORT).show();
        }
    }

}
