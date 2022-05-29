package com.example.wheelsplus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.VehiclesAdapter;
import display.DisplayGroupDriver;
import model.Vehiculo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateGroupFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    TextInputEditText editChangeName, editChangeOrigin, editChangeDestination, editChangeFee;
    Button buttonUpdateGroup;
    ImageButton changeDate;
    TextView tvCarDetail, tvChangeDate;
    ListView listCars;

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
    public static final String FB_VEHICLES_PATH = "vehicles/";
    Vehiculo vehiculo;
    DisplayGroupDriver displayGroup;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateGroupFragment newInstance(String param1, String param2) {
        UpdateGroupFragment fragment = new UpdateGroupFragment();
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

        root = inflater.inflate(R.layout.fragment_update_group, container, false);

        editChangeDestination = root.findViewById(R.id.editChangeDestino);
        editChangeName = root.findViewById(R.id.editChangeNameGroup);
        editChangeFee = root.findViewById(R.id.editChangeFee);
        editChangeOrigin = root.findViewById(R.id.editChangeOrigin);
        tvCarDetail = root.findViewById(R.id.tvCarDetail);
        tvChangeDate = root.findViewById(R.id.tvChangeDate);
        changeDate = root.findViewById(R.id.buttonUpDate);
        listCars = root.findViewById(R.id.listCars);
        buttonUpdateGroup = root.findViewById(R.id.buttonUpdateGroup);

        geocoder = new Geocoder(getActivity().getBaseContext());

        displayGroup = getArguments().getParcelable("displayDriverGroup");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        loadVehicles();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editChangeOrigin.setText(displayGroup.getOrigen());
        editChangeDestination.setText(displayGroup.getDestino());
        editChangeFee.setText(displayGroup.getTarifa());
        editChangeName.setText(displayGroup.getNombre());
        tvCarDetail.setText(displayGroup.getPlaca());
        tvChangeDate.setText(displayGroup.getFecha());

        listCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vehiculo = (Vehiculo) adapterView.getItemAtPosition(i);
                tvCarDetail.setText(vehiculo.getPlaca());
            }
        });

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        tvChangeDate.setText(sdf.format(calendar.getTime()));
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
                    }
                }, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        buttonUpdateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng origen = searchLatLngAddress(editChangeOrigin.getText().toString());
                LatLng destino = searchLatLngAddress(editChangeDestination.getText().toString());
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/nombreGrupo", editChangeName.getText().toString());
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/latitudAcuerdo", origen.lat);
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/latitudDestino", destino.lat);
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/longitudAcuerdo", origen.lng);
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/longitudDestino", destino.lng);
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/tarifa", Double.parseDouble(editChangeFee.getText().toString()));
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/idVehiculo", vehiculo.getIdVehiculo());
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/cupo", vehiculo.getCapacidad());
                childUpdates.put(FB_GROUPS_PATH + displayGroup.getIdGrupo() + "/fechaAcuerdo", calendar.getTimeInMillis());
                myRef = database.getReference();
                myRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            replaceFragment(new DriverGroupFragment());
                        }
                    }
                });
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.driver_nav_host_fragment, fragment);
        fragmentTransaction.commit();
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

    public void loadVehicles(){
        listCars.setAdapter(null);
        myRef = database.getReference(FB_VEHICLES_PATH);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Vehiculo> vehiculos = new ArrayList<>();
                    for(DataSnapshot single : task.getResult().getChildren()){
                        if(single.getKey().equals(auth.getCurrentUser().getUid())){
                            for(DataSnapshot minisingle : single.getChildren()) {
                                vehiculos.add(minisingle.getValue(Vehiculo.class));
                            }
                        }
                    }
                    if(getActivity() != null) {
                        VehiclesAdapter vehiclesAdapter = new VehiclesAdapter(getActivity(), vehiculos);
                        listCars.setAdapter(vehiclesAdapter);
                    }
                }
            }
        });
    }
}