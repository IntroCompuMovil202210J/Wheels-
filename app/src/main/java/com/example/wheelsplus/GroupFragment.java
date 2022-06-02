package com.example.wheelsplus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.LatLng;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapters.GroupsAdapter;
import display.DisplayGroup;
import model.Grupo;
import model.PuntoRuta;
import model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Map/Location
     */
    Geocoder geocoder;

    /**
     * Screen elements (to inflate)
     */
    Button buttonTime, buttonSearchGroup;
    TextInputEditText editGroupOrigin, editGroupDestination, editGroupName;
    TextView tvSelectedTime;
    ListView listGroups;

    /**
     * Firebase
     */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener vel;

    /**
     * Utils
     */
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_GROUPS_PATH = "groups/";
    boolean time, date = false;
    double latitud = 0;
    double longitud = 0;
    double latOrigin, lngOrigin;
    LatLng latLngDestino;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
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

        root = inflater.inflate(R.layout.fragment_group, container, false);

        buttonTime = root.findViewById(R.id.buttonTime);
        buttonSearchGroup = root.findViewById(R.id.buttonSearchGroup);
        editGroupOrigin = root.findViewById(R.id.editGroupOrigin);
        editGroupDestination = root.findViewById(R.id.editGroupDestination);
        editGroupName = root.findViewById(R.id.editGroupName);
        tvSelectedTime = root.findViewById(R.id.tvSelectedTime);
        listGroups = root.findViewById(R.id.listGroups);

        geocoder = new Geocoder(getActivity().getBaseContext());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child(FB_USERS_PATH + auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Usuario usuario = task.getResult().getValue(Usuario.class);
                    latitud = usuario.getLatitud();
                    longitud = usuario.getLongitud();
                    latOrigin = latitud;
                    lngOrigin = longitud;
                }
            }
        });

        loadGroups();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        tvSelectedTime.setText(sdf.format(calendar.getTime()));
                        time = true;
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.updateTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                timePickerDialog.show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        date = true;
                    }
                }, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        buttonSearchGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean flag = true;

                if(TextUtils.isEmpty(editGroupDestination.getText()) && TextUtils.isEmpty(editGroupName.getText())){
                    Toast.makeText(getContext(), "Alguno de los dos campos debe ser llenado para la búsqueda", Toast.LENGTH_SHORT).show();
                    flag = false;
                }

                if(!time || !date){
                    Toast.makeText(view.getContext(), "Hora y dia de acuerdo requeridos", Toast.LENGTH_SHORT).show();
                    flag = false;
                }

                if(!TextUtils.isEmpty(editGroupOrigin.getText())){
                    LatLng origin = searchLatLngAddress(editGroupOrigin.getText().toString());

                    if(origin != null) {
                        latOrigin = origin.lat;
                        lngOrigin = origin.lng;

                    }else{
                        editGroupOrigin.setError("Direccion de origen no encontrada");
                        flag = false;
                    }
                }


                if(flag){

                    ArrayList<Grupo> grupos = new ArrayList<>();
                    latLngDestino = searchLatLngAddress(editGroupDestination.getText().toString());
                    long timestamp = calendar.getTimeInMillis();
                    String groupName = editGroupName.getText().toString();
                    myRef = database.getReference(FB_GROUPS_PATH);

                    if(!TextUtils.isEmpty(editGroupName.getText()) && TextUtils.isEmpty(editGroupDestination.getText())){
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DataSnapshot single : task.getResult().getChildren()){
                                        Grupo grupo = single.getValue(Grupo.class);
                                        if(candidatoGrupoXDistancia(grupo.getLatitudAcuerdo(), grupo.getLatitudDestino(), grupo.getLongitudAcuerdo(), grupo.getLongitudDestino(), new LatLng(latOrigin, lngOrigin))){
                                            long tiempo = grupo.getFechaAcuerdo();
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(new Date(tiempo));

                                            if(calendar.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == c.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == c.get(Calendar.YEAR)){
                                                if(Math.abs(timestamp - c.getTimeInMillis()) / 60000 <= 30){
                                                    if(grupo.getNombreGrupo().toLowerCase().contains(groupName.toLowerCase()) && grupo.getCupo() > 0){
                                                        if(!grupo.getIdConductor().equals(auth.getCurrentUser().getUid())){
                                                            Grupo aux = single.getValue(Grupo.class);
                                                            aux.setLatUser(latOrigin);
                                                            aux.setLonUser(lngOrigin);
                                                            grupos.add(aux);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if(grupos.isEmpty())
                                        Toast.makeText(getActivity(), "No se encontraron grupos", Toast.LENGTH_SHORT).show();

                                    else {
                                        SelectGroupFragment selectGroupFragment = new SelectGroupFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelableArrayList("grupos", grupos);
                                        selectGroupFragment.setArguments(bundle);
                                        replaceFragment(selectGroupFragment);
                                    }
                                }
                            }
                        });
                    }


                    else if(TextUtils.isEmpty(editGroupName.getText()) && !TextUtils.isEmpty(editGroupDestination.getText())){
                        if(latLngDestino != null) {
                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DataSnapshot single : task.getResult().getChildren()){
                                            Grupo grupo = single.getValue(Grupo.class);

                                            if(candidatoGrupoXDistancia(grupo.getLatitudAcuerdo(), grupo.getLatitudDestino(), grupo.getLongitudAcuerdo(), grupo.getLongitudDestino(), new LatLng(latOrigin, lngOrigin))){

                                                long tiempo = grupo.getFechaAcuerdo();
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(new Date(tiempo));
                                                if(calendar.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == c.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
                                                    if (Math.abs(timestamp - c.getTimeInMillis()) / 60000 <= 30) {
                                                        if(grupo.getCupo() > 0){
                                                            if(distance(grupo.getLatitudDestino(), grupo.getLongitudDestino(), latLngDestino.lat, latLngDestino.lng) <= 0.3){
                                                                if(!grupo.getIdConductor().equals(auth.getCurrentUser().getUid())){
                                                                    Grupo aux = single.getValue(Grupo.class);
                                                                    aux.setLatUser(latOrigin);
                                                                    aux.setLonUser(lngOrigin);
                                                                    grupos.add(aux);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if(grupos.isEmpty())
                                            Toast.makeText(getActivity(), "No se encontraron grupos", Toast.LENGTH_SHORT).show();

                                        else {
                                            SelectGroupFragment selectGroupFragment = new SelectGroupFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelableArrayList("grupos", grupos);
                                            selectGroupFragment.setArguments(bundle);
                                            replaceFragment(selectGroupFragment);
                                        }
                                    }
                                }
                            });
                        }
                        else
                            editGroupDestination.setError("Dirección no encontrada, intente de nuevo");

                    }else{
                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DataSnapshot single : task.getResult().getChildren()){
                                        Grupo grupo = single.getValue(Grupo.class);

                                        if(candidatoGrupoXDistancia(grupo.getLatitudAcuerdo(), grupo.getLatitudDestino(), grupo.getLongitudAcuerdo(), grupo.getLongitudDestino(), new LatLng(latOrigin, lngOrigin))){
                                            long tiempo = grupo.getFechaAcuerdo();
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(new Date(tiempo));
                                            if(calendar.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == c.get(Calendar.MONTH) && calendar.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
                                                if (Math.abs(timestamp - c.getTimeInMillis()) / 60000 <= 30) {
                                                    if(grupo.getNombreGrupo().toLowerCase().contains(groupName.toLowerCase())){
                                                        if(grupo.getCupo() > 0){
                                                            if(distance(grupo.getLatitudDestino(), grupo.getLongitudDestino(), latLngDestino.lat, latLngDestino.lng) <= 0.3){
                                                                if(!grupo.getIdConductor().equals(auth.getCurrentUser().getUid())){
                                                                    Grupo aux = single.getValue(Grupo.class);
                                                                    aux.setLatUser(latOrigin);
                                                                    aux.setLonUser(lngOrigin);
                                                                    grupos.add(aux);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if(grupos.isEmpty())
                                        Toast.makeText(getActivity(), "No se encontraron grupos", Toast.LENGTH_SHORT).show();

                                    else {
                                        SelectGroupFragment selectGroupFragment = new SelectGroupFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelableArrayList("grupos", grupos);
                                        selectGroupFragment.setArguments(bundle);
                                        replaceFragment(selectGroupFragment);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DisplayGroup displayGroup = (DisplayGroup) adapterView.getItemAtPosition(i);
                GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("displayGroup", displayGroup);
                groupDetailFragment.setArguments(bundle);
                replaceFragment(groupDetailFragment);
            }
        });
    }

    private boolean candidatoGrupoXDistancia (Double latitudA, Double latitudB, Double longitudA, Double longitudB, LatLng origenUsuario){

        GeoPoint A, B, C;

        B = calcularPuntoMedio(latitudA, latitudB, longitudA, longitudB);
        A = calcularPuntoMedio(latitudA, B.getLatitude(), longitudA, B.getLongitude());
        C = calcularPuntoMedio(B.getLatitude(), latitudB, B.getLongitude(), longitudB);

        Double dis = (distance(latitudA, longitudA, latitudB, longitudB));

        if (distance(A.getLatitude(), A.getLongitude(), origenUsuario.lat, origenUsuario.lng) < (dis/2) + 1)
            return true;

        else if(distance(B.getLatitude(), B.getLongitude(), origenUsuario.lat, origenUsuario.lng) < (dis/2) + 1)
            return true;

        else if(distance(C.getLatitude(), C.getLongitude(), origenUsuario.lat, origenUsuario.lng) < (dis/2) + 1)
            return true;

        else
            return false;
    }

    private GeoPoint calcularPuntoMedio (Double latitudA, Double latitudB, Double longitudA, Double longitudB){
        return new GeoPoint(((latitudA+latitudB)/2), ((longitudA+longitudB)/2));
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = HomeFragment.EARTH_RADIUS * c;
        return (Math.round(result*100.0)/100.0);
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
        loadGroups();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(myRef != null){
            myRef.removeEventListener(vel);
        }
    }

    public LatLng searchLatLngAddress(String address){
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 2);
            if (addresses != null && !addresses.isEmpty()) {
                Address addressResult = addresses.get(0);
                return new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    private void loadGroups(){
        myRef = database.getReference(FB_USERS_PATH + auth.getCurrentUser().getUid()).child(FB_GROUPS_PATH);
        vel = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGroups.setAdapter(null);
                ArrayList<DisplayGroup> displayGroups = new ArrayList<>();
                ArrayList<String> keyGroups = new ArrayList<>();
                for(DataSnapshot single : snapshot.getChildren()){
                    keyGroups.add(single.getKey());
                }
                myRef = database.getReference(FB_GROUPS_PATH);
                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Grupo> grupos = new ArrayList<>();
                            for(DataSnapshot single : task.getResult().getChildren()){
                                if(keyGroups.contains(single.getKey())){
                                    grupos.add(single.getValue(Grupo.class));
                                }
                            }
                            myRef = database.getReference(FB_USERS_PATH);
                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    for(DataSnapshot single : task.getResult().getChildren()){
                                        Usuario usuario = single.getValue(Usuario.class);
                                        for(Grupo grupo : grupos){
                                            if(grupo.getIdConductor().equals(single.getKey()) && !grupo.getIdConductor().equals(auth.getCurrentUser().getUid())){
                                                displayGroups.add(new DisplayGroup(usuario.getNombre() + " " + usuario.getApellido(), grupo.getNombreGrupo(), single.getKey(), String.valueOf(grupo.getTarifa()), geoCoderBuscar(new LatLng(grupo.getLatitudAcuerdo(), grupo.getLongitudAcuerdo())), geoCoderBuscar(new LatLng(grupo.getLatitudDestino(), grupo.getLongitudDestino())), usuario.getUrlFoto(), grupo.getId_Grupo(), sdf.format(grupo.getFechaAcuerdo())));
                                            }
                                        }
                                    }
                                    if(getActivity() != null) {
                                        GroupsAdapter groupsAdapter = new GroupsAdapter(getActivity(), displayGroups);
                                        listGroups.setAdapter(groupsAdapter);
                                    }
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

}