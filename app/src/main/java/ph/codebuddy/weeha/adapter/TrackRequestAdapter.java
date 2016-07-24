package ph.codebuddy.weeha.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
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
import ph.codebuddy.weeha.request.AcceptTrackingRequest;
import ph.codebuddy.weeha.request.OnTaskCompleted;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class TrackRequestAdapter extends RecyclerView.Adapter<TrackRequestAdapter.ViewAllJoinersHolder> {
    ArrayList<TrackRequest> trackRequests;
    Context context;
    SharedPreferences preferences;

    public TrackRequestAdapter(ArrayList<TrackRequest> list, Context context, SharedPreferences preferences){
        this.trackRequests = list;
        this.context = context;
        this.preferences = preferences;
    }
    @Override
    public ViewAllJoinersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_track_request, parent, false);

        return new ViewAllJoinersHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewAllJoinersHolder holder, int position) {
        final TrackRequest current = trackRequests.get(position);

        holder.tvJoinerName.setText(current.getFullName());
        Picasso.with(context).load(current.getAvatar()).placeholder(R.drawable.placeholder_user).into(holder.rivAvatar);

        holder.ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptTrackingRequest acceptTrackingRequest = new AcceptTrackingRequest(context,
                        current.getId(),
                        preferences, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Boolean bool, String response) {
                        if(bool){
                            Toast.makeText(context, "Request accepted!", Toast.LENGTH_LONG).show();
                            ((Activity) context).finish();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    }
                });

                acceptTrackingRequest.executeAcceptTrackingRequest();
            }
        });

        holder.ivDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        AppCompatImageView ivAccept, ivDecline;

        public ViewAllJoinersHolder(View itemView) {
            super(itemView);
            rivAvatar = (RoundedImageView) itemView.findViewById(R.id.rivAvatar);
            tvJoinerName = (AppCompatTextView) itemView.findViewById(R.id.tvJoinerName);
            ivAccept = (AppCompatImageView) itemView.findViewById(R.id.accept);
            ivDecline = (AppCompatImageView) itemView.findViewById(R.id.decline);

        }
    }
}
