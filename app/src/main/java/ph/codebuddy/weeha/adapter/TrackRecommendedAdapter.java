package ph.codebuddy.weeha.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ph.codebuddy.weeha.R;
import ph.codebuddy.weeha.activity.MainActivity;
import ph.codebuddy.weeha.model.TrackRequest;
import ph.codebuddy.weeha.request.OnTaskCompleted;
import ph.codebuddy.weeha.request.PostRequestTrack;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class TrackRecommendedAdapter extends RecyclerView.Adapter<TrackRecommendedAdapter.ViewAllJoinersHolder> {
    ArrayList<TrackRequest> trackRequests;
    Context context;
    SharedPreferences preferences;

    public TrackRecommendedAdapter(ArrayList<TrackRequest> list, Context context, SharedPreferences preferences){
        this.trackRequests = list;
        this.context = context;
        this.preferences = preferences;
    }
    @Override
    public ViewAllJoinersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_track_recommended, parent, false);

        return new ViewAllJoinersHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAllJoinersHolder holder, int position) {
        final TrackRequest current = trackRequests.get(position);

        holder.tvJoinerName.setText(current.getFullName());
        Picasso.with(context).load(current.getAvatar()).placeholder(R.drawable.placeholder_user).into(holder.rivAvatar);

        holder.ivRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostRequestTrack postRequestTrack = new PostRequestTrack(context,
                        current.getId(),
                        preferences, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Boolean bool, String response) {
                        if(bool){
                            Toast.makeText(context, "Tracking is now requested, wait for him/her to confirm the tracking request", Toast.LENGTH_LONG).show();
                            ((Activity) context).finish();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    }
                });

                postRequestTrack.executePostRequestTrack();
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
        AppCompatImageView ivRequest;

        public ViewAllJoinersHolder(View itemView) {
            super(itemView);
            rivAvatar = (RoundedImageView) itemView.findViewById(R.id.rivAvatar);
            tvJoinerName = (AppCompatTextView) itemView.findViewById(R.id.tvJoinerName);
            ivRequest = (AppCompatImageView) itemView.findViewById(R.id.request);

        }
    }
}