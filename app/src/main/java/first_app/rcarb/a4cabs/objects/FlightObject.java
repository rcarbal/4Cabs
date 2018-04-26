package first_app.rcarb.a4cabs.objects;


import android.os.Parcel;
import android.os.Parcelable;

public class FlightObject implements Parcelable {

    private long mIdColumn = -2;
    private int mDate = -2;
    private int mGate = -2;
    private String mFlightId = "null";
    private int mNextFlightIdIndex = -2;

    private int mFlightArrivalScheduled = -2;
    private int mFlightArrivalScheduledIndex= -2;

    private int mActualArrival =-2;
    private String mPreStatus = "null";

    private String mAirport= "null";
    private  String mAirline = "null";

    private boolean mIsLastFLight = false;
    private String mParsedString = "null";
    private int mAlarmSet = -2;
    private String mPostStatus = null;
    private String mDay = "null";
    private int mTimeFrame =-2;

    public FlightObject() {
    }

    protected FlightObject(Parcel in) {

        mIdColumn = in.readLong();
        mDate = in.readInt();
        mGate = in.readInt();
        mFlightId = in.readString();
        mNextFlightIdIndex = in.readInt();
        mFlightArrivalScheduled = in.readInt();
        mFlightArrivalScheduledIndex = in.readInt();
        mActualArrival = in.readInt();
        mPreStatus = in.readString();
        mAirport = in.readString();
        mAirline = in.readString();
        mIsLastFLight = in.readByte() != 0;
        mParsedString = in.readString();
        mAlarmSet = in.readInt();
        mPostStatus = in.readString();
        mDay = in.readString();
        mTimeFrame = in.readInt();
    }

    public static final Creator<FlightObject> CREATOR = new Creator<FlightObject>() {
        @Override
        public FlightObject createFromParcel(Parcel in) {
            return new FlightObject(in);
        }

        @Override
        public FlightObject[] newArray(int size) {
            return new FlightObject[size];
        }
    };

    public String getFlightName(){
        return mFlightId;
    }

    public int getActualArrivalTime(){
        return mActualArrival;
    }

    public String getAirline(){
        return mAirline;
    }

    public int getTImeFrame(){
        return mTimeFrame;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mIdColumn);
        dest.writeInt(mDate);
        dest.writeInt(mGate);
        dest.writeString(mFlightId);
        dest.writeInt(mNextFlightIdIndex);
        dest.writeInt(mFlightArrivalScheduled);
        dest.writeInt(mFlightArrivalScheduledIndex);
        dest.writeInt(mActualArrival);
        dest.writeString(mPreStatus);
        dest.writeString(mAirport);
        dest.writeString(mAirline);
        dest.writeByte((byte) (mIsLastFLight ? 1 : 0));
        if (!mParsedString.equals("null")){
            mParsedString = "null";
        }
        dest.writeString(mParsedString);
        dest.writeInt(mAlarmSet);
        dest.writeString(mPostStatus);
        dest.writeString(mDay);
        dest.writeInt(mTimeFrame);
    }
}
