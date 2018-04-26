package first_app.rcarb.a4cabs.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import first_app.rcarb.a4cabs.R;
import first_app.rcarb.a4cabs.objects.FlightObject;

public class FlightArrayAdaptor extends RecyclerView.Adapter<FlightArrayAdaptor.FlightListViewHolder> {

    private final ArrayList<FlightObject>mFlightArray;

    public FlightArrayAdaptor(ArrayList<FlightObject> flightArray) {
        mFlightArray = flightArray;
    }

    @NonNull
    @Override
    public FlightListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int viewToBeInflated = R.layout.flights_list_details_holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(viewToBeInflated, parent, false);
        return new FlightListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightListViewHolder holder, int position) {
        FlightObject object = mFlightArray.get(position);
        holder.mFlightName.setText(object.getFlightName());
        holder.mAirline.setText(String.valueOf(object.getAirline()));
        holder.mArrivalTime.setText(String.valueOf(object.getActualArrivalTime()));
    }

    @Override
    public int getItemCount() {
        if (mFlightArray != null) {
            return mFlightArray.size();
        }
        return 0;
    }

    class FlightListViewHolder extends RecyclerView.ViewHolder {

        final TextView mFlightName;
        final TextView mAirline;
        final TextView mArrivalTime;

        public FlightListViewHolder(View itemView) {
            super(itemView);

            mFlightName = itemView.findViewById(R.id.flight_name);
            mAirline = itemView.findViewById(R.id.terminal);
            mArrivalTime = itemView.findViewById(R.id.arrival_time);
        }
    }
}
