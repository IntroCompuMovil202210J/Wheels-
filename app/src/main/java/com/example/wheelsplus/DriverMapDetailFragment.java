package com.example.wheelsplus;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
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
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;
import display.DisplayGroupDriver;

public class DriverMapDetailFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Map/Location
     */
    MapView map;
    IMapController mapController;
    RoadManager roadManager;
    Polyline roadOverlay;
    ArrayList<GeoPoint> points = new ArrayList<>();
    ArrayList<GeoPoint> pointsAux = new ArrayList<>();
    double latitudA, longitudA;

    /**
     * Firebase
     */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    /**
     * Light sensor
     */
    SensorManager sensorManager;
    Sensor light;
    SensorEventListener lightEvent;

    /**
     * Utils
     */
    public static final String FB_GROUPS_PATH = "groups/";
    public static final String FB_ROUTE_PATH = "ruta/";
    DisplayGroupDriver displayGroup;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverMapDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DriverMapDetailFragment newInstance(String param1, String param2) {
        DriverMapDetailFragment fragment = new DriverMapDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_driver_map_detail, container, false);

        displayGroup = getArguments().getParcelable("displayDriverGroupInfo");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        roadManager = new OSRMRoadManager(getActivity(), "ANDROID");

        initMap();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightEvent = createLightEventListener();

        getCoordenadasFB();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        mapController = map.getController();
        LatLng bogota = new LatLng(4.6269938175930525, -74.06389749953162);
        mapController.setCenter(new GeoPoint(bogota.latitude, bogota.longitude));
        mapController.setZoom(16.0);
        sensorManager.registerListener(lightEvent, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEvent);
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
                        } else {
                            Log.i("MAPS", "LIGHT MAP " + event.values[0]);
                            map.getOverlayManager().getTilesOverlay().setColorFilter(null);
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

    private void initMap(){
        map = (MapView) root.findViewById(R.id.driverHomeMapDetail);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.getZoomController().activate();
        mapController = map.getController();
    }

    private void getCoordenadasFB(){
        myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo());
        myRef.child("latitudAcuerdo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) latitudA = (Double) snapshot.getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        myRef.child("longitudAcuerdo").addValueEventListener(new ValueEventListener() {
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
                    for(DataSnapshot s: task.getResult().getChildren()){
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
                                if(task.isSuccessful()){
                                    longitudA = (Double) task.getResult().getValue();
                                    points.add(new GeoPoint(latitudA, longitudA));
                                    sortGeoPoint(points);
                                }
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