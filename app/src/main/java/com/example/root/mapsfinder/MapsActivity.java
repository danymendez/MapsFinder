package com.example.root.mapsfinder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    double lat= 0.0, lng= 0.0;
    AutoCompleteTextView autoBuscar;
    ImageButton buscarBtn;
    EditText edBuscar;
    PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40,-168), new LatLng(71,136)
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

       Toolbar toolbar = findViewById(R.id.toolbarcustomizable2);
       setSupportActionBar(toolbar);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();


        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,LAT_LNG_BOUNDS,null);

              //  edBuscar = toolbar.findViewById(R.id.edBuscar);
                autoBuscar = toolbar.findViewById(R.id.AuBuscar);
        buscarBtn = toolbar.findViewById(R.id.imageButtonBuscar);
        autoBuscar.setAdapter(mPlaceAutocompleteAdapter);
        buscarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), edBuscar.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                actualizarMapaConBusqueda(autoBuscar.getText().toString().trim());
            }
        });

        autoBuscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                            || event.getAction() == KeyEvent.ACTION_DOWN
                            || event.getAction() == KeyEvent.KEYCODE_ENTER){

                }

                return false;
            }
        });


    }

    public void actualizarMapaConBusqueda(String parametro){

        LatLng sydney;
        mMap.clear();
        //   getLatLongFromAddress("El Salvador");
        // Add a marker in Sydney and move the camera

        Location location = new Location(LOCATION_SERVICE);


        Geocoder geoCoder = new Geocoder(getApplicationContext());
        try
        {
            List<Address> addresses = new ArrayList<>();
            addresses = geoCoder.getFromLocationName(parametro,10);
            if(geoCoder.isPresent()) {
                if (addresses.size() > 0) {

                    for (Address ad : addresses) {
                        sydney = new LatLng(ad.getLatitude(), ad.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(sydney).title(ad.getCountryName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
                    }


                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }*/

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

        LatLng sydney;
    //   getLatLongFromAddress("El Salvador");
        // Add a marker in Sydney and move the camera

        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName("El Salvador",1);
            if (addresses.size() > 0)
            {

                for(Address ad : addresses){
                     sydney = new LatLng(ad.getLatitude(), ad.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(ad.getCountryName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
                }


            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void getLatLongFromAddress(String address)
    {


        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0)
            {

                for(Address ad : addresses){
                    lat = ad.getLatitude();
                    lng = ad.getLongitude();
                }


            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
