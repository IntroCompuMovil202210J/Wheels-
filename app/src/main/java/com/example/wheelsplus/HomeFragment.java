package com.example.wheelsplus;

import static android.app.Activity.RESULT_OK;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import java.io.IOException;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import services.DownloadImageTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Map/Location
     */
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    MapView map;
    Marker other = null;
    IMapController mapController;

    GeoPoint startPoint = null;
    Geocoder geocoder;

    /**
     * Light sensor
     */
    SensorManager sensorManager;
    Sensor light;
    SensorEventListener lightEvent;

    /**
     * Screen elements (to inflate)
     */
    TextInputEditText if_viaje;
    TextView tvI_ubicacionP;
    CircleImageView ic_profile;

    /**
     * Firebase
     */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    /**
     * Utils
     */
    double latitude = 0, longitude = 0;
    boolean settingsOK = false;
    public static final double EARTH_RADIUS = 6371;

    public static final String FB_USERS_PATH = "users/";
    boolean invert = false;

    ActivityResultLauncher<String> requestPermissionLocation = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result)
                startLocationUpdates();
        }
    });

    ActivityResultLauncher<IntentSenderRequest> getLocationSettings = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                settingsOK = true;
                startLocationUpdates();
            }
            else
                Log.i("LocationTest", "GPS is OFF");

        }
    });

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        root = inflater.inflate(R.layout.fragment_home, container, false);

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        if_viaje = root.findViewById(R.id.editViaje);
        tvI_ubicacionP = root.findViewById(R.id.tvI_ubicacionP);
        ic_profile = root.findViewById(R.id.ic_profile);

        geocoder = new Geocoder(getActivity().getBaseContext());

        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();

        sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightEvent = createLightEventListener();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        requestPermissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        initMap();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if_viaje.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    String address = if_viaje.getText().toString();
                    if(!address.isEmpty()){
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(address, 2);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressResult = addresses.get(0);
                                GeoPoint position = new GeoPoint(addressResult.getLatitude(), addressResult.getLongitude());
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("initDestination", position);
                                bundle.putParcelable("initOrigin", startPoint);
                                InitLocationFragment initLocationFragment = new InitLocationFragment();
                                initLocationFragment.setArguments(bundle);
                                replaceFragment(initLocationFragment);
                            }
                            else
                                if_viaje.setError("Dirección no encontrada");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        Toast.makeText(getActivity(), "La dirección esta vacía", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        new DownloadImageTask((CircleImageView) root.findViewById(R.id.ic_profile))
                .execute(auth.getCurrentUser().getPhotoUrl().toString());

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLocationSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        sensorManager.registerListener(lightEvent, light, SensorManager.SENSOR_DELAY_NORMAL);
        mapController = map.getController();
        LatLng bogota = new LatLng(4.6269938175930525, -74.06389749953162);
        startPoint = new GeoPoint(bogota.latitude, bogota.longitude);
        mapController.setCenter(startPoint);
        mapController.setZoom(14.0);
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        sensorManager.unregisterListener(lightEvent);
        stopLocationUpdates();
    }

    private void initMap(){
        map = (MapView) root.findViewById(R.id.homeMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().activate();
        mapController = map.getController();
    }

    private String geoCoderBuscar(LatLng latLng){
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 2);
            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
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
            if(settingsOK)
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
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
                        if(distance(latitude, longitude, lastLocation.getLatitude(), lastLocation.getLongitude()) > 0.03){
                            mapController.setZoom(17.0);
                            mapController.setCenter(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
                            latitude = lastLocation.getLatitude();
                            longitude = lastLocation.getLongitude();
                            myRef = database.getReference(FB_USERS_PATH + auth.getCurrentUser().getUid());
                            myRef.child("latitud").setValue(latitude);
                            myRef.child("longitud").setValue(longitude);
                        }
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                        startPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
                        tvI_ubicacionP.setText(geoCoderBuscar(new LatLng(latitude, longitude)));
                        if(other != null)
                            map.getOverlays().remove(other);

                        if(invert)
                            other = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mk_dark_origin);

                        else
                            other = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mk_origin);

                        map.getOverlays().add(other);
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
                }else{
                    Log.i("LocationTest", "GPS is not available");
                }
            }
        });
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = EARTH_RADIUS * c;
        return Math.round(result*100.0)/100.0;
    }

    private SensorEventListener createLightEventListener(){
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                try {
                    if (map != null) {
                        if (event.values[0] < 5000) {
                            Log.i("MAPS", "DARK MAP " + event.values[0]);
                            map.getOverlayManager().getTilesOverlay().setColorFilter(TilesOverlay.INVERT_COLORS);
                            invert = true;
                            if (other != null) {
                                map.getOverlays().remove(other);
                            }
                            other = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mk_dark_origin);
                            map.getOverlays().add(other);
                        } else {
                            Log.i("MAPS", "LIGHT MAP " + event.values[0]);
                            map.getOverlayManager().getTilesOverlay().setColorFilter(null);
                            invert = false;
                            if(other != null){
                                map.getOverlays().remove(other);
                            }
                            other = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_mk_origin);
                            map.getOverlays().add(other);
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

}