package com.example.wheelsplus;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.GroupsAdapter;
import display.DisplayGroup;
import model.Grupo;
import model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayTripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayTripsFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    ListView listTrips;

    /**
     * Firebase
     */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    /**
     * Utils
     */
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_TRIPS_PATH = "trips/";
    Geocoder geocoder;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplayTripsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayTripsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayTripsFragment newInstance(String param1, String param2) {
        DisplayTripsFragment fragment = new DisplayTripsFragment();
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
        root = inflater.inflate(R.layout.fragment_display_trips, container, false);

        listTrips = root.findViewById(R.id.listTrips);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        geocoder = new Geocoder(getActivity().getBaseContext());

        loadTrips();

        return root;
    }

    private void loadTrips(){
        listTrips.setAdapter(null);
        myRef = database.getReference(FB_TRIPS_PATH + auth.getCurrentUser().getUid());
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<DisplayGroup> displayGroups = new ArrayList<>();
                if(task.isSuccessful()) {
                    for(DataSnapshot single : task.getResult().getChildren()){
                        Grupo grupo = single.getValue(Grupo.class);
                        myRef = database.getReference(FB_USERS_PATH + grupo.getIdConductor());
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    Usuario usuario = task.getResult().getValue(Usuario.class);
                                    displayGroups.add(new DisplayGroup(usuario.getNombre() + " " + usuario.getApellido(), grupo.getNombreGrupo(), usuario.getIdUsuario(), String.valueOf(grupo.getTarifa()), geoCoderBuscar(new LatLng(grupo.getLatitudAcuerdo(), grupo.getLongitudAcuerdo())), geoCoderBuscar(new LatLng(grupo.getLatitudDestino(), grupo.getLongitudDestino())), usuario.getUrlFoto(), grupo.getId_Grupo(), sdf.format(grupo.getFechaAcuerdo())));
                                    if(getActivity() != null) {
                                        GroupsAdapter groupsAdapter = new GroupsAdapter(getActivity(), displayGroups);
                                        listTrips.setAdapter(groupsAdapter);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private String geoCoderBuscar(LatLng latLng){
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.lat, latLng.lng, 2);
            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}