package ph.codebuddy.weeha.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ph.codebuddy.weeha.R;
import ph.codebuddy.weeha.adapter.TrackRecommendedAdapter;
import ph.codebuddy.weeha.adapter.TrackRequestAdapter;
import ph.codebuddy.weeha.adapter.TrackedContactsAdapter;
import ph.codebuddy.weeha.model.TrackRequest;
import ph.codebuddy.weeha.request.GetTrackRequest;
import ph.codebuddy.weeha.request.OnTaskCompleted;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class TrackFragment extends Fragment {

    public TrackFragment() {
    }


    RecyclerView recyclerView, recyclerView2, recyclerView3;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2, linearLayoutManager3;
    TrackRequestAdapter trackRequestAdapter;
    TrackRecommendedAdapter trackRecommendedAdapter;
    TrackedContactsAdapter trackedContactsAdapter;
    LinearLayout layoutRequest, layoutRecommended, layoutTrackedContacts;
    SharedPreferences sharedPreferences;
    ArrayList<TrackRequest> trackList = new ArrayList<>();
    ArrayList<TrackRequest> trackRecommended = new ArrayList<>();
    ArrayList<TrackRequest> trackedContacts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);

        sharedPreferences = getActivity().getSharedPreferences("WEEHA_PREFS", Context.MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutRequest = (LinearLayout) view.findViewById(R.id.layoutRequests);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        trackRequestAdapter = new TrackRequestAdapter(trackList, getActivity(), sharedPreferences);

        recyclerView.setAdapter(trackRequestAdapter);

        linearLayoutManager2 = new LinearLayoutManager(getActivity());
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerViewRec);
        layoutRecommended = (LinearLayout) view.findViewById(R.id.layoutRecommended);

        recyclerView2.setLayoutManager(linearLayoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        trackRecommendedAdapter = new TrackRecommendedAdapter(trackRecommended, getActivity(), sharedPreferences);

        recyclerView2.setAdapter(trackRecommendedAdapter);

        linearLayoutManager3 = new LinearLayoutManager(getActivity());
        recyclerView3 = (RecyclerView) view.findViewById(R.id.recyclerViewTracked);
        layoutTrackedContacts = (LinearLayout) view.findViewById(R.id.layoutTrackedContacts);

        recyclerView3.setLayoutManager(linearLayoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());

        trackedContactsAdapter = new TrackedContactsAdapter(trackedContacts, getActivity());

        recyclerView3.setAdapter(trackedContactsAdapter);

        GetTrackRequest getTrackRequest = new GetTrackRequest(getActivity(), "relationships/pending_requests", sharedPreferences, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Boolean bool, String response) {
                Log.v(String.valueOf(bool) + "pending ", response);

                try {
                    TrackRequest trackRequest = new TrackRequest();
                    JSONArray users = new JSONArray(response);
                    if(users.length() == 0){
                        layoutRequest.setVisibility(View.GONE);
                    }
                    for(int i = 0; i<users.length(); i++){
                        JSONObject user = users.getJSONObject(i);
                        JSONObject follower = user.getJSONObject("follower");
                        trackRequest.setTrackRequest(user.getString("id"),
                                follower.getString("first_name"),
                                follower.getString("last_name"),
                                "http://www.anglia.ac.uk/~/media/Images/Staff%20Profiles/placeholder-profile.jpg");

                        trackList.add(trackRequest);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                trackRequestAdapter.notifyDataSetChanged();

            }
        });

        GetTrackRequest getTrackRecommended = new GetTrackRequest(getActivity(), "users", sharedPreferences, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Boolean bool, String response) {
                Log.v(String.valueOf(bool), response);

                try {
                    TrackRequest trackRequest = new TrackRequest();
                    JSONArray users = new JSONArray(response);
                    if(users.length() == 0){
                        layoutRecommended.setVisibility(View.GONE);
                    }
                    for(int i = 0; i<users.length(); i++){
                        JSONObject user = users.getJSONObject(i);
                        trackRequest.setTrackRequest(user.getString("id"),
                                user.getString("first_name"),
                                user.getString("last_name"),
                                "http://www.anglia.ac.uk/~/media/Images/Staff%20Profiles/placeholder-profile.jpg");

                        trackRecommended.add(trackRequest);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                trackRecommendedAdapter.notifyDataSetChanged();

            }
        });

        GetTrackRequest getTrackedContacts = new GetTrackRequest(getActivity(), "following", sharedPreferences, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Boolean bool, String response) {
                Log.v(String.valueOf(bool), response);

                try {
                    TrackRequest trackRequest = new TrackRequest();
                    JSONArray users = new JSONArray(response);
                    if(users.length() == 0){
                        layoutTrackedContacts.setVisibility(View.GONE);
                    }
                    for(int i = 0; i<users.length(); i++){
                        JSONObject user = users.getJSONObject(i);
                        JSONObject followed = user.getJSONObject("followed");
                        trackRequest.setTrackRequest(user.getString("id"),
                                followed.getString("first_name"),
                                followed.getString("last_name"),
                                "http://www.anglia.ac.uk/~/media/Images/Staff%20Profiles/placeholder-profile.jpg");

                        trackedContacts.add(trackRequest);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                trackedContactsAdapter.notifyDataSetChanged();

            }
        });


        getTrackRequest.executeGetTrackRequest();
        getTrackRecommended.executeGetTrackRequest();
        getTrackedContacts.executeGetTrackRequest();

        return view;
    }
}
