package com.example.wheelsplus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitLocationFragment extends Fragment {

    View root;
    MapView map;
    GeoPoint startPoint;
    Geocoder geocoder;

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

        geocoder = new Geocoder(getActivity().getBaseContext());

        initMap();

        return root;
    }

    private void initMap(){
        try {
            map = (MapView) root.findViewById(R.id.mapView2);
            map.setTileSource(TileSourceFactory.MAPNIK);
            map.setMultiTouchControls(true);
            Marker other = createMarker(startPoint, geocoder.getFromLocation(startPoint.getLatitude(), startPoint.getLongitude(), 1).get(0).getAddressLine(0), null, R.drawable.ic_outline_home_24);
            map.getOverlays().add(other);
        }catch(Exception e){
            e.printStackTrace();
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
}