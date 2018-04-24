package first_app.rcarb.a4cabs.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rcarb on 3/16/2018.
 */

public class FlightSyncObject implements Parcelable {

    private int thirty = -1;
    private int hour = -1;
    private int twoHour = -1;
    private String stamp = "No flights available";
    private String totalFlight = "Total flights not available";
    private ArrayList<FlightObject> mFlights = new ArrayList<>();

    public FlightSyncObject() {
    }

    public FlightSyncObject(int thirty,
                            int hour,
                            int twohour,
                            String stamp,
                            String totalFlights,
                            ArrayList<FlightObject> flights){
        this.thirty = thirty;
        this.hour = hour;
        this.twoHour = twohour;
        this.stamp = stamp;
        this.totalFlight = totalFlights;
        mFlights = flights;
    }

    protected FlightSyncObject(Parcel in) {
        thirty = in.readInt();
        hour = in.readInt();
        twoHour = in.readInt();
        stamp = in.readString();
        totalFlight = in.readString();
        mFlights = in.createTypedArrayList(FlightObject.CREATOR);
        String a ="";
    }

    public static final Creator<FlightSyncObject> CREATOR = new Creator<FlightSyncObject>() {
        @Override
        public FlightSyncObject createFromParcel(Parcel in) {
            return new FlightSyncObject(in);
        }

        @Override
        public FlightSyncObject[] newArray(int size) {
            return new FlightSyncObject[size];
        }
    };

    public void setThirty(int thirty){
        this.thirty = thirty;
    }
    public int getThirty(){
        return thirty;
    }
    public void setHour(int hour){
        this.hour = hour;
    }
    public int getHour(){
        return hour;
    }
    public void setTwoHour(int twoHour){
        this.twoHour = twoHour;
    }
    public int getTwoHour(){
        return twoHour;
    }
    public void setStamp(String stamp){
        this.stamp = stamp;
    }
    public String getStamp(){
        return stamp;
    }
    public void setTotalFlights(String totalFlights){
        this.totalFlight = totalFlights;
    }
    public String getTotalFlights(){
        return totalFlight;
    }
    public void setEntireArrayList(ArrayList<FlightObject> flights){
        mFlights.addAll(flights);
    }
    public ArrayList<FlightObject> getEntireFlightsArray(){
        return mFlights;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(thirty);
        dest.writeInt(hour);
        dest.writeInt(twoHour);
        dest.writeString(stamp);
        dest.writeString(totalFlight);
        dest.writeTypedList(mFlights);
        String a ="";
    }
}
