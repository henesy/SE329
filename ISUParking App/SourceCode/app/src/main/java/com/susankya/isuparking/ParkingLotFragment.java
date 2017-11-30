package com.susankya.isuparking;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingLotFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public ParkingLotFragment() {
    }

    // TODO: Customize parameter initialization

    String lat,lon;
    int cat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          lat=getArguments().getString("lat");
          lon=getArguments().getString("lon");
           cat=getArguments().getInt("cat");
        }
    }

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parkinglot_list, container, false);

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
        loadNearest();
        return view;
    }

    ArrayList<ParkingLot> lots=new ArrayList<>();
    public void loadNearest()
    {
        CallMethods caller=((MyApp)getActivity().getApplication()).retrofit.create(CallMethods.class);
        caller.getNearestLots(lat,lon,cat).enqueue(
                new Callback<List<ParkingLot>>() {
                    @Override
                    public void onResponse(Call<List<ParkingLot>> call, Response<List<ParkingLot>> response) {
                       lots=new ArrayList<>(response.body());
                        recyclerView.setAdapter(new ParkingLotRecyclerViewAdapter(lots));
                    }

                    @Override
                    public void onFailure(Call<List<ParkingLot>> call, Throwable t) {

                    }
                }
        );
    }



    public class ParkingLotRecyclerViewAdapter extends RecyclerView.Adapter<ParkingLotRecyclerViewAdapter.ViewHolder> {

        private final ArrayList<ParkingLot> mValues;

        public ParkingLotRecyclerViewAdapter(ArrayList<ParkingLot> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_parkinglot, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            if (position==0)
            {
                holder.mNearest.setVisibility(View.VISIBLE);
                holder.mHint.setVisibility(View.VISIBLE);
                holder.mIdView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
           else {
                holder.mNearest.setVisibility(View.GONE);
                holder.mHint.setVisibility(View.GONE);
                holder.mIdView.setTextColor(getResources().getColor(android.R.color.black));

            }
            holder.mIdView.setText(mValues.get(position).spot_name);
            double distanceMiles=Double.parseDouble(holder.mItem.distance_in_km)*0.621371;
            double rounded=Math.round(distanceMiles);
            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.CEILING);
            String v=df.format(distanceMiles);
            holder.mContentView.setText("Distance: "+v+" mi");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cat==2)
                        showParkDialog(lat,lon,holder.mItem.latitude,holder.mItem.longitude);
                    else showTimeDialog(lat,lon,holder.mItem.latitude,holder.mItem.longitude,position);

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
            public final TextView mHint;
            public final TextView mNearest;
            public ParkingLot mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mHint=view.findViewById(R.id.hint);
                mNearest=view.findViewById(R.id.best_id);
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }

        double totCharge;
        String rate_per_hour;
        public void showParkDialog(final String latitude1, final String longitude1, final String latitude2, final String longitude2)
        {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_park);
            dialog.setTitle("ISU PARKING");

            // set the custom dialog components - text, image and button
            final TextView charge = (TextView) dialog.findViewById(R.id.charge);

            if (cat==2)
                charge.setText("FREE");
            else charge.setText("$"+rate_per_hour);
            dialog.findViewById(R.id.viewMaps).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            openMaps(latitude1,longitude1,latitude2,longitude2);
                        }
                    }
            );
            Button dialogButton = (Button) dialog.findViewById(R.id.proceed);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        public void openMaps(String latitude1, String longitude1,String latitude2, String longitude2)
        {
            String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+latitude1+","+longitude1+"&daddr="+latitude2+","+longitude2;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(Intent.createChooser(intent, "Select an application"));
        }
        public void showTimeDialog(final String latitude1, final String longitude1, final String latitude2, final String longitude2,int position)
        {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog);
            dialog.setTitle("ISU PARKING");

            // set the custom dialog components - text, image and button
            final TextView charge = (TextView) dialog.findViewById(R.id.charge);
            final TextView note=dialog.findViewById(R.id.notes);
            String noteString=lots.get(position).note;
            String special="SPECIAL: ";
            if (lots.get(position).special_rate==1)
            {
                note.setText(special+noteString);
                note.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            else
            {
                note.setText(noteString);
                note.setTextColor(getResources().getColor(R.color.green));
            }


           /* Spinner sp=dialog.findViewById(R.id.spinner);
            final List<String> list = new ArrayList<String>();
            for (int i=1;i<17;i++)
                list.add(Integer.toString(i));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item,list );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(dataAdapter);
            sp.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            int val=Integer.parseInt((String)adapterView.getItemAtPosition(i));
                            double totalPrice=val*0.75;
                            charge.setText("$"+totalPrice);
                            totCharge=totalPrice;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                            charge.setText("$0.00");
                        }
                    }
            );*/
            Button dialogButton = (Button) dialog.findViewById(R.id.proceed);

            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showParkDialog(latitude1,longitude1,latitude2,longitude2);
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

}
