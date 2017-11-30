package com.susankya.isuparking;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CategoryFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    String lat,lon;
    Bundle locationBundle;
    boolean update=false;
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mLastLocation!=null)
        {
            if (!update)
                Toast.makeText(getContext(),"Location updated",Toast.LENGTH_SHORT).show();
            update=true;
            lat=Double.toString(mLastLocation.getLatitude());
            lon=Double.toString(mLastLocation.getLongitude());
            locationBundle.putString("lat",lat);
            locationBundle.putString("lon",lon);
            Log.d("location", mLastLocation.getLatitude() + ":" + mLastLocation.getLongitude());

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == Activity.RESULT_OK) && (requestCode == 1000)) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,CategoryFragment.this );

        }
    }

    LocationRequest locationRequest;
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);

            final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);
            final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

            if (result != null) {
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult locationSettingsResult) {
                        final Status status = locationSettingsResult.getStatus();

                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,CategoryFragment.this );
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the user
                                // a optionsDialog.
                                try {
                                    // Show the optionsDialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    if (status.hasResolution()) {
                                        status.startResolutionForResult(getActivity(), 1000);
                                    }
                                } catch (Exception e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the optionsDialog.
                                break;
                        }
                    }
                });
            }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */


    public CategoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CategoryFragment newInstance(int columnCount) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationBundle=new Bundle();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        startPermissionCheck();

    }


    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

        }
        loadCat();
        return view;
    }


    public void startPermissionCheck()
    {
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION};
        if (ActivityCompat.checkSelfPermission((Activity)getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            getLocation();
        else requestPermissions(permissions,10);
    }

    private GoogleApiClient googleApiClient;
    private Location mLastLocation;



    public void getLocation()
    {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage(getActivity(), 0, this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        googleApiClient.connect();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==10)
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getLocation();
            }
            else
            {
                startPermissionCheck();
            }
        }

    }

    public void loadCat()
    {
        CallMethods caller=((MyApp)getActivity().getApplicationContext()).retrofit.create(CallMethods.class);
        caller.getCategories().enqueue(
                new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                        ArrayList<Category> list=new ArrayList<>(response.body());

                        try {
                            Log.d("TAG",list.get(0).category_name);
                        }
                        catch (Exception e)
                        {

                        }

                        recyclerView.setAdapter(new CategoryRecyclerViewAdapter(list));
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {

                    }
                }
        );
    }


    public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<Category> mValues;
        public CategoryRecyclerViewAdapter(ArrayList<Category> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_category, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).category_name);
            // holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLastLocation!=null)
                    {
                        ParkingLotFragment p=new ParkingLotFragment();
                        p.setArguments(locationBundle);
                        locationBundle.putInt("cat",holder.mItem.sn);
                        getFragmentManager().beginTransaction().replace(R.id.container,p).addToBackStack(null).commit();
                    }
                    else Toast.makeText(getContext(),"Please wait until your location is detected.",Toast.LENGTH_LONG).show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Category mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

}
