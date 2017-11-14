package isu.se329;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    Intent myIntent = new Intent(MapsActivity.this, Home.class);
//                    MapsActivity.this.startActivity(myIntent);
//                    return true;
//                case R.id.navigation_map:
//                    Intent myIntent1 = new Intent(MapsActivity.this, MapsActivity.class);
//                    MapsActivity.this.startActivity(myIntent1);
//                    return true;
//                case R.id.navigation_notifications:
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Ames and move the camera
        LatLng trondheim = new LatLng(63.4305, 10.3951);
        LatLng ames = new LatLng(42.0308, -93.6319);
        LatLng curtissHall = new LatLng(42.0262, -93.6449);
        mMap.addMarker(new MarkerOptions()
                .position(curtissHall)
                .title("Curtiss Hall")
                .snippet(getString(R.string.ames)));
        mMap.addMarker(new MarkerOptions()
                .position(ames)
                .title("Ames")
                .snippet(getString(R.string.ames)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curtissHall, 15));
    }

}
