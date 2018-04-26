package first_app.rcarb.a4cabs.loaders;

import android.content.Context;

import java.util.ArrayList;

import first_app.rcarb.a4cabs.objects.FlightObject;

public class PrepareFlightArrayLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<FlightObject>> {

    private final ArrayList<FlightObject> mData;
    private int mTimeFrame;

    public PrepareFlightArrayLoader(Context context, ArrayList<FlightObject> flights, int timeFrame) {
        super(context);
        mData = flights;
        mTimeFrame = timeFrame;

    }

    @Override
    public ArrayList<FlightObject> loadInBackground() {
        if (mData == null && mTimeFrame < 0) return null;
        assert mData != null;
        ArrayList<FlightObject> returnedArray = new ArrayList<>();
        for (FlightObject flight : mData) {
            //noinspection IfCanBeSwitch
            if (mTimeFrame == 0) {
                if (flight.getTImeFrame() == 0) {
                    returnedArray.add(flight);
                }
            }else if (mTimeFrame == 1) {
                    if (flight.getTImeFrame() < 2) {
                        returnedArray.add(flight);
                    }
            } else if (mTimeFrame == 2) {
                    returnedArray.add(flight);
                }
        }
        return returnedArray;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
