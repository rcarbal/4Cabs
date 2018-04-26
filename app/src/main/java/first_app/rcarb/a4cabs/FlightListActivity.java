package first_app.rcarb.a4cabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import first_app.rcarb.a4cabs.adaptors.FlightArrayAdaptor;
import first_app.rcarb.a4cabs.objects.FlightObject;
import first_app.rcarb.a4cabs.utilities.ActionStrings;

@SuppressWarnings("FieldCanBeLocal")
public class FlightListActivity extends AppCompatActivity {
    @BindView(R.id.adView2)
    AdView mAdView;
    @BindView(R.id.set_time_frame_tv)
    TextView mTimeFrameTextView;
    @BindView(R.id.flight_list_rv)
    RecyclerView mRecyclerView;

    private ArrayList<FlightObject>mFlightArray;
    private int mTimeFrameselected;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.list_detail_activity);

        ButterKnife.bind(this);
        postponeEnterTransition();

        AdRequest adRequest =  new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        getIntentExtras();

        //Setup recyclerview
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FlightArrayAdaptor(mFlightArray);
        mRecyclerView.setAdapter(mAdapter);
        startPostponedEnterTransition();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    @SuppressWarnings("IfCanBeSwitch")
    private void getIntentExtras(){
        Intent intent = getIntent();
        mFlightArray = intent.getParcelableArrayListExtra(ActionStrings.SEND_FLIGHTS_ARRAYLIST);
        mTimeFrameselected = intent.getIntExtra(ActionStrings.SEND_TIME_FRAME_SELECTED, 0);
        if (mTimeFrameselected >=0){
            if (mTimeFrameselected == 0)  {
                mTimeFrameTextView.setText(getString(R.string.detail_time_frame_30_min));
            }
            else if (mTimeFrameselected == 1){
                mTimeFrameTextView.setText(getString(R.string.detail_time_frame_1_hour));
            }
            else if (mTimeFrameselected == 2){
                mTimeFrameTextView.setText(R.string.detail_time_frame_2_hours);
            }
        }
    }

}
