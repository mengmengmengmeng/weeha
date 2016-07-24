package ph.codebuddy.weeha.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ph.codebuddy.weeha.R;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    double lat = 14.598152, lng = 120.9446317;
    SharedPreferences sharedPreferences;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        sharedPreferences = getActivity().getSharedPreferences("WEEHA_PREFS", Context.MODE_PRIVATE);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);
                        if (mLastLocation != null) {
                            lat = mLastLocation.getLatitude();
                            lng = mLastLocation.getLongitude();

                            if(!sharedPreferences.getString("tracked_lat", "").equals("")){
                                lat = Double.parseDouble(sharedPreferences.getString("tracked_lat", ""));
                                lng = Double.parseDouble(sharedPreferences.getString("tracked_lng", ""));
                                String name_marker = sharedPreferences.getString("tracked_name", "");
                                LatLng loc = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions().position(loc).title(name_marker));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc ,15));
                            }else{
                                LatLng loc = new LatLng(lat, lng);
                                //mMap.addMarker(new MarkerOptions().position(loc).title("New Marker"));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc ,15));
                            }

                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng manila = new LatLng(14.598152, 120.9446317);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manila, 13));

        mMap.setMyLocationEnabled(true);
    }
}
