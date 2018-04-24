package first_app.rcarb.a4cabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import first_app.rcarb.a4cabs.adaptors.FlightArrayAdaptor;
import first_app.rcarb.a4cabs.objects.FlightObject;
import first_app.rcarb.a4cabs.utilities.ActionStrings;

public class FlightListActivity extends AppCompatActivity {

    private AdView mAdView;

    private ArrayList<FlightObject>mFlightArray;
    private int mTimeFrameselected;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail_activity);
        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest =  new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        getIntentExtras();

        //Setup recyclerview
        mRecyclerView = findViewById(R.id.flight_list_rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FlightArrayAdaptor(mFlightArray);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getIntentExtras(){
        Intent intent = getIntent();
        mFlightArray = intent.getParcelableArrayListExtra(ActionStrings.SEND_FLIGHTS_ARRAYLIST);
        mTimeFrameselected = intent.getIntExtra(ActionStrings.SEND_TIME_FRAME_SELECTED, 0);
    }

}
