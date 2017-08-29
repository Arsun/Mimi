package org.scau.mimi.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.scau.mimi.R;
import org.scau.mimi.gson.MessagesInfo;

import java.util.List;

/**
 * Created by 10313 on 2017/8/24.
 */

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "LocationAdapter";

    private List<MessagesInfo.Content.Message.Location> mLocations;

    public LocationAdapter(List<MessagesInfo.Content.Message.Location> locationList) {
        mLocations = locationList;
    }

    static class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView tvLocation;
        int locationId;

        public NormalViewHolder(View itemView) {
            super(itemView);

            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
        }

        public void bind(MessagesInfo.Content.Message.Location location) {

            tvLocation.setText(location.locale);
            locationId = location.lid;
            tvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);

        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((NormalViewHolder) holder).bind(mLocations.get(position));
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }
}
