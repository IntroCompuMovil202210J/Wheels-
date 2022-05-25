package com.example.wheelsplus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Grupo;
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
    TextInputEditText editGroupDestination, editGroupName;
    TextView tvSelectedTime;

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
    public static final String FB_GROUPS_PATH = "groups/";
    boolean time, date = false;
    double latitud = 0;
    double longitud = 0;
    Calendar calendar = Calendar.getInstance();

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
        editGroupDestination = root.findViewById(R.id.editGroupDestination);
        editGroupName = root.findViewById(R.id.editGroupName);
        tvSelectedTime = root.findViewById(R.id.tvSelectedTime);

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
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
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
                        tvSelectedTime.setText(sdf.format(calendar.getTime()));
                        date = true;
                    }
                }, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        buttonSearchGroup.setOnClickListener(new View.OnClickListener() {
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
                if(flag){
                    ArrayList<Grupo> grupos = new ArrayList<>();
                    LatLng latLng = searchLatLngAddress(editGroupDestination.getText().toString());
                    long timestamp = calendar.getTimeInMillis();
                    String groupName = editGroupName.getText().toString();
                    if(!TextUtils.isEmpty(editGroupName.getText()) && TextUtils.isEmpty(editGroupDestination.getText())){
                        myRef.child(FB_GROUPS_PATH).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DataSnapshot single : task.getResult().getChildren()){
                                        Grupo grupo = single.getValue(Grupo.class);
                                        if(grupo.getNombreGrupo().toLowerCase().contains(groupName.toLowerCase())){
                                            if(distance(grupo.getLatitudAcuerdo(), grupo.getLongitudAcuerdo(), latitud, longitud) <= 0.3){
                                                grupos.add(single.getValue(Grupo.class));
                                            }
                                        }
                                    }
                                    Toast.makeText(view.getContext(), "Size -> " + grupos.size(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else if(TextUtils.isEmpty(editGroupName.getText()) && !TextUtils.isEmpty(editGroupDestination.getText())){
                        if(latLng != null) {
                            if(!TextUtils.isEmpty(editGroupDestination.getText()) && TextUtils.isEmpty(editGroupName.getText())){
                                myRef.child(FB_GROUPS_PATH).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(DataSnapshot single : task.getResult().getChildren()){
                                                Grupo grupo = single.getValue(Grupo.class);
                                                if(distance(grupo.getLatitudAcuerdo(), grupo.getLongitudAcuerdo(), latitud, longitud) <= 0.3){
                                                    if(distance(grupo.getLatitudDestino(), grupo.getLongitudDestino(), latLng.lat, latLng.lng) <= 0.3){
                                                        grupos.add(single.getValue(Grupo.class));
                                                    }
                                                }
                                            }
                                            Toast.makeText(view.getContext(), "Size -> " + grupos.size(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                myRef.child(FB_GROUPS_PATH).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(DataSnapshot single : task.getResult().getChildren()){
                                                Grupo grupo = single.getValue(Grupo.class);
                                                if(grupo.getNombreGrupo().toLowerCase().contains(groupName.toLowerCase())){
                                                    if(distance(grupo.getLatitudAcuerdo(), grupo.getLongitudAcuerdo(), latitud, longitud) <= 0.3){
                                                        if(distance(grupo.getLatitudDestino(), grupo.getLongitudDestino(), latLng.lat, latLng.lng) <= 0.3){
                                                            grupos.add(single.getValue(Grupo.class));
                                                        }
                                                    }
                                                }
                                            }
                                            Toast.makeText(view.getContext(), "Size -> " + grupos.size(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }else{
                            editGroupDestination.setError("Dirección no encontrada, intente de nuevo");
                        }
                    }
                }
            }
        });
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