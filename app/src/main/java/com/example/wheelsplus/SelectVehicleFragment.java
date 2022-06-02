package com.example.wheelsplus;

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
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapters.VehiclesAdapter;
import model.Vehiculo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectVehicleFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    ListView listVehicles;

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
    public static final String FB_VEHICLES_PATH = "vehicles/";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectVehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectVehicleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectVehicleFragment newInstance(String param1, String param2) {
        SelectVehicleFragment fragment = new SelectVehicleFragment();
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
        root =  inflater.inflate(R.layout.fragment_select_vehicle, container, false);

        listVehicles = root.findViewById(R.id.listVehicles);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        loadVehicles();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listVehicles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vehiculo vehiculo = (Vehiculo) adapterView.getItemAtPosition(i);
                DriverGroupFragment driverGroupFragment = new DriverGroupFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("carSelected",vehiculo);
                driverGroupFragment.setArguments(bundle);
                replaceFragment(driverGroupFragment);
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.driver_nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    public void loadVehicles(){
        listVehicles.setAdapter(null);
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
                        listVehicles.setAdapter(vehiclesAdapter);
                    }
                }
            }
        });
    }
}