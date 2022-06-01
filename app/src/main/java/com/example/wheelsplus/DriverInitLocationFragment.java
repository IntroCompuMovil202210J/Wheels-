package com.example.wheelsplus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverInitLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverInitLocationFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Map/Location
     */
    FusedLocationProviderClient mFusedLocationClient;
    boolean settingsOK = false;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    double latitude, longitude;
    MapView map;
    IMapController mapController;
    GeoPoint startPoint, destinationPoint;
    Geocoder geocoder;
    Marker origin, destination;
    RoadManager roadManager;
    Polyline roadOverlay;

    /**
     * Light sensor
     */
    SensorManager sensorManager;
    Sensor light;
    SensorEventListener lightEvent;

    /**
     * Screen elements (to inflate)
     */
    TextInputEditText editModDestinationDriver;
    TextInputEditText editModFee;
    Button buttonCancel, buttonConfirm;

    /**
     * Utils
     */
    boolean invert = false;

    ActivityResultLauncher<IntentSenderRequest> getLocationSettings = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == -1){
                settingsOK = true;
                startLocationUpdates();
            }
        }
    });

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverInitLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverInitLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverInitLocationFragment newInstance(String param1, String param2) {
        DriverInitLocationFragment fragment = new DriverInitLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_driver_init_location, container, false);

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        startPoint = getArguments().getParcelable("initOrigin");
        destinationPoint = getArguments().getParcelable("initDestination");

        geocoder = new Geocoder(getActivity().getBaseContext());

        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();

        sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightEvent = createLightEventListener();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        initMap();

        roadManager = new OSRMRoadManager(getActivity(), "ANDROID");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editModDestinationDriver = root.findViewById(R.id.editDriverDestination);
        editModFee = root.findViewById(R.id.editDriverFee);
        buttonCancel = root.findViewById(R.id.buttonCancelDriver);
        buttonConfirm = root.findViewById(R.id.buttonConfirmDriver);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editPolilyne(startPoint, destinationPoint);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverHomeFragment driverHomeFragment = new DriverHomeFragment();
                replaceFragment(driverHomeFragment);
            }
        });

        editModDestinationDriver.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i ==  EditorInfo.IME_ACTION_DONE){
                    String address = editModDestinationDriver.getText().toString();
                    if(!address.isEmpty()){
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(address, 2);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressResult = addresses.get(0);
                                destinationPoint = new GeoPoint(addressResult.getLatitude(), addressResult.getLongitude());
                                map.getOverlays().remove(destination);
                                destination = createMarker(destinationPoint, geocoder.getFromLocation(destinationPoint.getLatitude(), destinationPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mk_destination);
                                map.getOverlays().add(destination);
                                editPolilyne(startPoint, destinationPoint);
                            } else {
                                editModDestinationDriver.setError("Dirección no encontrada");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "La dirección esta vacía", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.driver_nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        mapController = map.getController();
        centerToPolyline(startPoint, destinationPoint);
        checkLocationSettings();
        startLocationUpdates();
        sensorManager.registerListener(lightEvent, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        stopLocationUpdates();
        sensorManager.unregisterListener(lightEvent);
    }

    private void initMap(){
        try {
            map = (MapView) root.findViewById(R.id.driverInitLocationMap);
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.setMultiTouchControls(true);
            map.getZoomController().activate();
            mapController = map.getController();
            if(invert){
                origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mkd_dark_origin);
            }else {
                origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mkd_origin);
            }            destination = createMarker(destinationPoint, geocoder.getFromLocation(destinationPoint.getLatitude(), destinationPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mk_destination);
            map.getOverlays().add(origin);
            map.getOverlays().add(destination);
            mapController.setZoom(15.0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void editPolilyne(GeoPoint startPoint, GeoPoint destinationPoint){
        map.getOverlays().remove(roadOverlay);
        if(invert){
            drawRoute(startPoint, destinationPoint, Color.rgb(255, 255, 255));
        }else {
            drawRoute(startPoint, destinationPoint, Color.rgb(39, 97, 198));
        }
        centerToPolyline(startPoint, destinationPoint);
    }

    private void centerToPolyline(GeoPoint startPoint, GeoPoint destinationPoint){
        double centerLat = (startPoint.getLatitude() + destinationPoint.getLatitude()) / 2;
        double centerLong = (startPoint.getLongitude() + destinationPoint.getLongitude()) / 2;
        mapController.setCenter(new GeoPoint(centerLat, centerLong));
    }

    private void drawRoute(GeoPoint start, GeoPoint finish, int color){
        ArrayList<GeoPoint> routePoints = new ArrayList<>();
        routePoints.add(start);
        routePoints.add(finish);
        Road road = roadManager.getRoad(routePoints);
        Log.i("RUTA", "Route length: "+road.mLength+" klm");
        Log.i("RUTA", "Duration: "+road.mDuration/60+" min");
        if(map!=null){
            if(roadOverlay!=null){
                map.getOverlays().remove(roadOverlay);
            }
            roadOverlay = RoadManager.buildRoadOverlay(road);
            roadOverlay.getOutlinePaint().setColor(color);
            roadOverlay.getOutlinePaint().setStrokeWidth(5);
            map.getOverlays().add(roadOverlay);
        }
    }

    private Marker createMarker(GeoPoint p, String title, String desc, int iconID){
        Marker marker = null;
        if(map!=null) {
            marker = new Marker(map);
            if (title != null) marker.setTitle(title);
            if (desc != null) marker.setSubDescription(desc);
            if (iconID != 0) {
                Drawable myIcon = getResources().getDrawable(iconID, getActivity().getTheme());
                marker.setIcon(myIcon);
            }
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        }
        return marker;
    }

    public void startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(settingsOK){
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest createLocationRequest(){
        LocationRequest req = LocationRequest.create().setFastestInterval(1000).setInterval(10000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return req;
    }

    private LocationCallback createLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location lastLocation = locationResult.getLastLocation();

                try {
                    if (lastLocation != null) {
                        Log.i("Callback", "Latitude: " + lastLocation.getLatitude() + " Longitude: " + lastLocation.getLongitude());
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                        if(distance(latitude, longitude, startPoint.getLatitude(), startPoint.getLongitude()) != 0){
                            startPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
                            if(origin != null){
                                map.getOverlays().remove(origin);
                            }
                            if(invert){
                                origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mkd_dark_origin);
                            }else {
                                origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mkd_origin);
                            }                            map.getOverlays().add(origin);
                            editPolilyne(startPoint, destinationPoint);
                        }
                    }

                } catch (Exception e) {
                    Log.e("Callback", e.toString());
                }
            }
        };
    }

    private void checkLocationSettings(){
        LocationSettingsRequest.Builder builder = new
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i("LocationTest", "GPS is ON");
                settingsOK = true;
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(((ApiException) e).getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED){
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    IntentSenderRequest isr = new IntentSenderRequest.Builder(resolvable.getResolution()).build();
                    getLocationSettings.launch(isr);
                }
            }
        });
    }

    private SensorEventListener createLightEventListener(){
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                try {
                    if (map != null) {
                        if (event.values[0] < 10000) {
                            Log.i("MAPS", "DARK MAP " + event.values[0]);
                            map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
                            invert = true;
                            editPolilyne(startPoint, destinationPoint);
                            if(origin != null){
                                map.getOverlays().remove(origin);
                            }
                            origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mkd_dark_origin);
                            map.getOverlays().add(origin);
                        } else {
                            Log.i("MAPS", "LIGHT MAP " + event.values[0]);
                            map.getOverlayManager().getTilesOverlay().setColorFilter(null);
                            invert = false;
                            editPolilyne(startPoint, destinationPoint);
                            if(origin != null){
                                map.getOverlays().remove(origin);
                            }
                            origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mkd_origin);
                            map.getOverlays().add(origin);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = HomeFragment.EARTH_RADIUS * c;
        return Math.round(result*100.0)/100.0;
    }

}