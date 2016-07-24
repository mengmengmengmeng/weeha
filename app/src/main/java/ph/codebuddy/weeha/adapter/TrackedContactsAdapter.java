package ph.codebuddy.weeha.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ph.codebuddy.weeha.R;
import ph.codebuddy.weeha.activity.MainActivity;
import ph.codebuddy.weeha.model.TrackRequest;
import ph.codebuddy.weeha.model.TrackedContacts;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class TrackedContactsAdapter extends RecyclerView.Adapter<TrackedContactsAdapter.ViewAllJoinersHolder> {
    ArrayList<TrackedContacts> trackRequests;
    Context context;

    public TrackedContactsAdapter(ArrayList<TrackedContacts> list, Context context){
        this.trackRequests = list;
        this.context = context;
    }
    @Override
    public ViewAllJoinersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tracked_contacts, parent, false);

        return new ViewAllJoinersHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAllJoinersHolder holder, int position) {
        final TrackedContacts current = trackRequests.get(position);

        holder.tvJoinerName.setText(current.getFullName());
        Picasso.with(context).load(current.getAvatar()).placeholder(R.drawable.placeholder_user).into(holder.rivAvatar);

        holder.ivTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAGGGG", current.getLocations());
                try {
                    JSONArray locations = new JSONArray(current.getLocations());
                    JSONObject lastLocation = new JSONObject(locations.get(locations.length()-1).toString());
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("tracked_lat", lastLocation.getString("lat"));
                    intent.putExtra("tracked_lng", lastLocation.getString("lng"));
                    intent.putExtra("tracked_name", current.getFullName());
                    ((Activity)context).finish();
                    context.startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return trackRequests.size();
    }

    public class ViewAllJoinersHolder extends RecyclerView.ViewHolder {
        RoundedImageView rivAvatar;
        AppCompatTextView tvJoinerName;
        AppCompatImageView ivTrack;

        public ViewAllJoinersHolder(View itemView) {
            super(itemView);
            rivAvatar = (RoundedImageView) itemView.findViewById(R.id.rivAvatar);
            tvJoinerName = (AppCompatTextView) itemView.findViewById(R.id.tvJoinerName);
            ivTrack = (AppCompatImageView) itemView.findViewById(R.id.track);

        }
    }
}
