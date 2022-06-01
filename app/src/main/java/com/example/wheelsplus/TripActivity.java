package com.example.wheelsplus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapters.PassengersAdapter;
import display.DisplayGroupDriver;
import model.PuntoRuta;
import model.Usuario;

public class TripActivity extends AppCompatActivity {

    ListView listTripPassengers;

    private MapView map;
    private IMapController mapController;
    private Marker marker, marker1, marker2;

    public static final String FB_USERS_PATH = "users/";
    public static final String FB_GROUPS_PATH = "groups/";
    public static final String FB_ROUTE_PATH = "ruta/";

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    boolean settingsOK = false;

    private RoadManager roadManager;
    private Polyline roadOverlay;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    ValueEventListener vel;

    private DisplayGroupDriver displayGroup;

    private ArrayList<GeoPoint> points = new ArrayList<>();
    private ArrayList<GeoPoint> pointsAux = new ArrayList<>();
    private double latitude = 4.76943, longitude = -74.04317, latitudA, longitudA;
    private GeoPoint startPoint;


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
            }else
                Log.i("LocationTest", "GPS is OFF");
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_trip);

        displayGroup = getIntent().getParcelableExtra("displayDriverGroupInfo");

        listTripPassengers = findViewById(R.id.listTripPassengers);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        geocoder = new Geocoder(TripActivity.this);

        roadManager = new OSRMRoadManager(TripActivity.this, "ANDROID");

        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermissionLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        initMap();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loadPassengers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationSettings();
        loadPassengers();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        mapController = map.getController();
        LatLng bogota = new LatLng(4.6269938175930525, -74.06389749953162);
        mapController.setCenter(new GeoPoint(bogota.latitude, bogota.longitude));
        mapController.setZoom(16.0);
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
        stopLocationUpdates();
        if(myRef != null){
            myRef.removeEventListener(vel);
        }
    }

    private void loadPassengers(){
        ArrayList<Usuario> usuarios = new ArrayList<>();
        myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo()).child(FB_ROUTE_PATH);
        vel = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTripPassengers.setAdapter(null);
                for(DataSnapshot single : snapshot.getChildren()){
                    PuntoRuta puntoRuta = single.getValue(PuntoRuta.class);
                    myRef = database.getReference(FB_USERS_PATH + puntoRuta.getIdUsuario());
                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                usuarios.add(task.getResult().getValue(Usuario.class));
                                PassengersAdapter passengersAdapter = new PassengersAdapter(TripActivity.this, usuarios);
                                listTripPassengers.setAdapter(passengersAdapter);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initMap(){
        map = findViewById(R.id.driverTripMapDetail);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().activate();
        mapController = map.getController();
    }

    public void startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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

                android.location.Location lastLocation = locationResult.getLastLocation();
                try {
                    if (lastLocation != null) {
                        if(distance(latitude, longitude, lastLocation.getLatitude(), lastLocation.getLongitude()) > 0.01){
                            mapController.setZoom(15.0);
                            mapController.setCenter(new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));
                            latitude = lastLocation.getLatitude();
                            longitude = lastLocation.getLongitude();

                            startPoint = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());

                            if(marker != null){
                                map.getOverlays().remove(marker);
                            }

                            marker = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.vector_baseline_directions_car_24);
                            map.getOverlays().add(marker);
                            points.clear();
                            pointsAux.clear();
                            getCoordenadasFB(startPoint);

                            myRef = database.getReference(FB_USERS_PATH + auth.getCurrentUser().getUid());
                            myRef.child("latitud").setValue(latitude);
                            myRef.child("longitud").setValue(longitude);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Callback", e.toString());
                }
            }
        };
    }

    private Marker createMarker(GeoPoint p, String title, String desc, int iconID){
        Marker marker = null;
        if(map!=null) {
            marker = new Marker(map);
            if (title != null) marker.setTitle(title);
            if (desc != null) marker.setSubDescription(desc);
            if (iconID != 0) {
                Drawable myIcon = getResources().getDrawable(iconID, getTheme());
                marker.setIcon(myIcon);
            }
            marker.setPosition(p);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        }
        return marker;
    }

    private void checkLocationSettings(){
        LocationSettingsRequest.Builder builder = new
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
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

    private String geoCoderBuscar(LatLng latLng){
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 2);
            return addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getCoordenadasFB(GeoPoint origen){

        latitudA = origen.getLatitude();
        longitudA = origen.getLongitude();
        points.add(new GeoPoint(latitudA, longitudA));

        myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo());
        myRef.child("latitudDestino").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) latitudA = (Double) snapshot.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        myRef.child("longitudDestino").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    longitudA = (Double) snapshot.getValue();
                    points.add(new GeoPoint(latitudA, longitudA));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo()).child(FB_ROUTE_PATH);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task){
                if (task.isSuccessful()){
                    for (DataSnapshot s : task.getResult().getChildren()) {
                        myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/ruta/" + s.getKey() + "/latitud");
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) latitudA = (Double) task.getResult().getValue();
                            }
                        });

                        myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/ruta/" + s.getKey() + "/longitud");
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task){
                                if (task.isSuccessful())
                                    longitudA = (Double) task.getResult().getValue();
                                points.add(new GeoPoint(latitudA, longitudA));
                                sortGeoPoint(points);
                            }
                        });
                    }
                }
            }
        });
    }

    private void sortGeoPoint (ArrayList<GeoPoint> pointss){

        final Long[] tamano = {Long.valueOf(pointss.size())};

        myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo()).child(FB_ROUTE_PATH);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    long count = task.getResult().getChildrenCount();
                    if(count + 2 == tamano[0]){
                        GeoPoint geoAux, geoTemp = null;
                        Integer tam = pointss.size();
                        Double distancia = Double.valueOf(10000), distanciaTemp;

                        pointsAux.add(pointss.get(0));
                        geoAux = pointss.get(1);
                        pointss.remove(1);
                        pointss.remove(0);

                        while(pointsAux.size() != tam){
                            for(int j = 0; j<pointss.size(); j++){
                                distanciaTemp = distance(pointsAux.get(0).getLatitude(), pointsAux.get(0).getLongitude(), pointss.get(j).getLatitude(), pointss.get(j).getLongitude());
                                if (distanciaTemp < distancia){
                                    distancia = distanciaTemp;
                                    geoTemp = pointss.get(j);
                                }
                            }
                            pointss.remove(geoTemp);
                            pointsAux.add(geoTemp);
                            distancia = Double.valueOf(10000);
                        }

                        pointsAux.add(geoAux);
                        drawRoute(pointsAux);
                    }
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
        double result = HomeFragment.EARTH_RADIUS * c;
        return Math.round(result*100.0)/100.0;
    }

    private void drawRoute(ArrayList<GeoPoint> pointsR){

        if (!pointsR.isEmpty()){
            Road road = roadManager.getRoad(pointsR);
            if(map!=null){
                if(roadOverlay!=null)
                    map.getOverlays().remove(roadOverlay);

                roadOverlay = RoadManager.buildRoadOverlay(road);
                roadOverlay.getOutlinePaint().setColor(Color.RED);
                roadOverlay.getOutlinePaint().setStrokeWidth(10);
                map.getOverlays().add(roadOverlay);
            }
        }
    }



}