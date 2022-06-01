package com.example.wheelsplus;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.GroupsAdapter;
import adapters.VehiclesAdapter;
import display.DisplayGroup;
import model.Grupo;
import model.PuntoRuta;
import model.Usuario;
import model.Vehiculo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectGroupFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    ListView listGroupsUser;

    /**
     * Map/Location
     */
    Geocoder geocoder;

    /**
     * Firebase
     */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    /**
     * Utils
     */
    public static final String FB_GROUPS_PATH = "groups/";
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_ROUTE_PATH = "ruta/";
    ArrayList<Grupo> grupos = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectGrouoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectGroupFragment newInstance(String param1, String param2) {
        SelectGroupFragment fragment = new SelectGroupFragment();
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
        root = inflater.inflate(R.layout.fragment_select_group, container, false);

        listGroupsUser = root.findViewById(R.id.listGroupsUser);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        geocoder = new Geocoder(getActivity().getBaseContext());

        grupos = getArguments().getParcelableArrayList("grupos");

        loadGroups();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listGroupsUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DisplayGroup grupoEscogido = (DisplayGroup) adapterView.getItemAtPosition(i);
                new MaterialAlertDialogBuilder(view.getContext())
                    .setTitle("¿Seguro que desea entrar en el grupo " + grupoEscogido.getNombreGrupo() + "?")
                    .setNegativeButton("Rechazar", null)
                    .setPositiveButton("Unirse", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            myRef = database.getReference(FB_USERS_PATH + auth.getCurrentUser().getUid()).child(FB_GROUPS_PATH + grupoEscogido.getIdGrupo());

                            myRef.setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                                    if(task.isSuccessful()){
                                        myRef = database.getReference(FB_GROUPS_PATH + grupoEscogido.getIdGrupo());
                                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>(){
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    Grupo gr = task.getResult().getValue(Grupo.class);
                                                    myRef = database.getReference(FB_GROUPS_PATH + grupoEscogido.getIdGrupo()).child("cupo");
                                                    myRef.setValue(gr.getCupo() - 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            myRef = database.getReference(FB_GROUPS_PATH + grupoEscogido.getIdGrupo()).child(FB_ROUTE_PATH);
                                                            myRef.push().setValue(new PuntoRuta(auth.getCurrentUser().getUid(), grupoEscogido.getLatOrigin(), grupoEscogido.getLonOrigin())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(getActivity(), "Grupo añadido correctamente", Toast.LENGTH_LONG).show();
                                                                        replaceFragment(new GroupFragment());
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }).show();
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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void loadGroups(){
        listGroupsUser.setAdapter(null);
        myRef = database.getReference(FB_USERS_PATH);
        ArrayList<DisplayGroup> displayGroups = new ArrayList<>();
        for(Grupo grupo : grupos) {
            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        for(DataSnapshot single : task.getResult().getChildren()){
                            Usuario usuario = single.getValue(Usuario.class);
                            if(grupo.getIdConductor().equals(single.getKey())){
                                displayGroups.add(new DisplayGroup(usuario.getNombre() + " " + usuario.getApellido(), grupo.getNombreGrupo(), single.getKey(), String.valueOf(grupo.getTarifa()), geoCoderBuscar(new LatLng(grupo.getLatitudAcuerdo(), grupo.getLongitudAcuerdo())), geoCoderBuscar(new LatLng(grupo.getLatitudDestino(), grupo.getLongitudDestino())), usuario.getUrlFoto(), grupo.getId_Grupo(), sdf.format(grupo.getFechaAcuerdo()), grupo.getLatUser(), grupo.getLonUser()));
                            }
                        }
                        if(getActivity() != null) {
                            GroupsAdapter groupsAdapter = new GroupsAdapter(getActivity(), displayGroups);
                            listGroupsUser.setAdapter(groupsAdapter);
                        }
                    }
                }
            });
        }

    }

}