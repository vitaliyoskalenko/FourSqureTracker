package com.voskalenko.foursquretracker.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.callback.VenueCallback;
import com.voskalenko.foursquretracker.model.Venue;

import java.util.List;
@EBean
public class VenuesAdapter extends BaseAdapter {

    @RootContext
    Context ctx;

    @SystemService
    LayoutInflater inflater;

    private List<Venue> venues;
    private VenueCallback callback;

    public void setCallback(VenueCallback callback) {
        this.callback = callback;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    @Override
	public int getCount() {
		return venues == null ? 0 : venues.size();
	}

	@Override
	public Object getItem(int position) {
		return venues == null ? null : venues.get(position);
	}

	@Override
	public long getItemId(int position) {
        return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        UIHolder holder;
        if(convertView == null) {
            holder = new UIHolder();
            convertView = inflater.inflate(R.layout.venues_list_item, null);
            holder.txtVenuName = (TextView) convertView.findViewById(R.id.txt_venue_name);
            holder.txtCity =  (TextView) convertView.findViewById(R.id.txt_city);
            holder.txtDistance = (TextView) convertView.findViewById(R.id.txt_distance);
            holder.btnCheckIn = (ImageButton) convertView.findViewById(R.id.btn_checkin);
            holder.btnCheckIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String venueId = ((Venue)view.getTag()).getId();
                    callback.onSuccess(venueId);
                }
            });
            convertView.setTag(holder);
        } else
            holder = (UIHolder) convertView.getTag();

        Venue venue = venues.get(position);
        holder.txtVenuName.setText(venue.getName());
        holder.txtCity.setText(venue.getLocation().getCity());
        holder.txtDistance.setText(ctx.getString(R.id.txt_distance, venue.getLocation().getDistance()));
        holder.btnCheckIn.setTag(getItem(position));

        return convertView;
	}

    static class UIHolder {
        TextView txtVenuName;
        TextView txtCity;
        TextView txtDistance;
        ImageButton btnCheckIn;
    }

}
