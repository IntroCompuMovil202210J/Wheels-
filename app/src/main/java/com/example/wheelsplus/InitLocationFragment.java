package com.example.wheelsplus;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitLocationFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Map/Location
     */
    MapView map;
    IMapController mapController;
    GeoPoint startPoint;
    GeoPoint destinationPoint;
    Geocoder geocoder;
    Marker origin, destination;
    RoadManager roadManager;
    Polyline roadOverlay;

    /**
     * Screen elements (to inflate)
     */
    TextInputEditText editModOrigin;
    TextInputEditText editModDestination;
    Button buttonCancel, buttonConfirm;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InitLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InitLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InitLocationFragment newInstance(String param1, String param2) {
        InitLocationFragment fragment = new InitLocationFragment();
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

        root = inflater.inflate(R.layout.fragment_init_location, container, false);

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        startPoint = getArguments().getParcelable("initOrigin");
        destinationPoint = getArguments().getParcelable("initDestination");

        geocoder = new Geocoder(getActivity().getBaseContext());

        initMap();

        roadManager = new OSRMRoadManager(getActivity(), "ANDROID");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        editModOrigin = root.findViewById(R.id.editOrigin);
        editModDestination = root.findViewById(R.id.editDestination);

        buttonConfirm = root.findViewById(R.id.buttonCancel);
        buttonConfirm = root.findViewById(R.id.buttonConfirm);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawRoute(startPoint, destinationPoint);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.homeFragment);
            }
        });

        editModOrigin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i ==  EditorInfo.IME_ACTION_DONE){
                    String address = editModOrigin.getText().toString();
                    if(!address.isEmpty()){
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(address, 2);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressResult = addresses.get(0);
                                startPoint = new GeoPoint(addressResult.getLatitude(), addressResult.getLongitude());
                                map.getOverlays().remove(origin);
                                origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.mk_origin);
                                map.getOverlays().add(origin);
                                editPolilyne(startPoint, destinationPoint);
                            } else {
                                editModOrigin.setError("Dirección no encontrada");
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

        editModDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i ==  EditorInfo.IME_ACTION_DONE){
                    String address = editModDestination.getText().toString();
                    if(!address.isEmpty()){
                        try {
                            List<Address> addresses = geocoder.getFromLocationName(address, 2);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address addressResult = addresses.get(0);
                                destinationPoint = new GeoPoint(addressResult.getLatitude(), addressResult.getLongitude());
                                map.getOverlays().remove(destination);
                                destination = createMarker(destinationPoint, geocoder.getFromLocation(destinationPoint.getLatitude(), destinationPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.mk_destination);
                                map.getOverlays().add(destination);
                                editPolilyne(startPoint, destinationPoint);
                            } else {
                                editModDestination.setError("Dirección no encontrada");
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

    private void initMap(){
        try {
            map = (MapView) root.findViewById(R.id.initLocationMap);
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.setMultiTouchControls(true);
            map.getZoomController().activate();
            mapController = map.getController();
            origin = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.mk_origin);
            destination = createMarker(destinationPoint, geocoder.getFromLocation(destinationPoint.getLatitude(), destinationPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.mk_destination);
            map.getOverlays().add(origin);
            map.getOverlays().add(destination);
            mapController.setZoom(15.0);
            centerToPolyline(startPoint, destinationPoint);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void editPolilyne(GeoPoint startPoint, GeoPoint destinationPoint){
        map.getOverlays().remove(roadOverlay);
        drawRoute(startPoint, destinationPoint);
        centerToPolyline(startPoint, destinationPoint);
    }

    private void centerToPolyline(GeoPoint startPoint, GeoPoint destinationPoint){
        double centerLat = (startPoint.getLatitude() + destinationPoint.getLatitude()) / 2;
        double centerLong = (startPoint.getLongitude() + destinationPoint.getLongitude()) / 2;
        mapController.setCenter(new GeoPoint(centerLat, centerLong));
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

    private void drawRoute(GeoPoint start, GeoPoint finish){
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
            roadOverlay.getOutlinePaint().setColor(Color.rgb(39, 97, 198));
            roadOverlay.getOutlinePaint().setStrokeWidth(5);
            map.getOverlays().add(roadOverlay);
        }
    }

}