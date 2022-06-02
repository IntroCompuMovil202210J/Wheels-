package com.example.wheelsplus;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import adapters.GroupUsersAdapter;
import display.DisplayGroupDriver;
import model.Grupo;
import model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverGroupDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverGroupDetailFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    TextView tvDetailDriverGroupName, tvDetailDriverOrigin, tvDetailDriverDestination, tvDetailDriverDate, tvDetailDriverFee, tvDetailDriverPlaca, tvDetailDriverMarca;

    ImageView car;
    ImageButton buttonRemoveGroup, buttonModifyGroup, buttonDriverMapDetail, buttonStart;

    ListView listDriverGroupUsers;

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
    public static final String FB_DRIVERS_PATH = "drivers/";
    public static final String FB_GROUPS_PATH = "groups/";
    DisplayGroupDriver displayGroup;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverGroupDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverGroupDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverGroupDetailFragment newInstance(String param1, String param2) {
        DriverGroupDetailFragment fragment = new DriverGroupDetailFragment();
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

        root = inflater.inflate(R.layout.fragment_driver_group_detail, container, false);

        tvDetailDriverGroupName = root.findViewById(R.id.tvDetailDriverGroupName);
        tvDetailDriverOrigin = root.findViewById(R.id.tvDetailDriverOrigin);
        tvDetailDriverDestination = root.findViewById(R.id.tvDetailDriverDestination);
        tvDetailDriverDate = root.findViewById(R.id.tvDetailDriverDate);
        tvDetailDriverFee = root.findViewById(R.id.tvDetailDriverFee);
        tvDetailDriverPlaca = root.findViewById(R.id.tvPlacaDriverDetail);
        tvDetailDriverMarca = root.findViewById(R.id.tvMarcaDriverDetail);
        buttonRemoveGroup = root.findViewById(R.id.buttonRemoveDriverGroup);
        buttonModifyGroup = root.findViewById(R.id.buttonModifyDriverGroup);
        buttonStart = root.findViewById(R.id.buttonStart);
        listDriverGroupUsers = root.findViewById(R.id.listGroupDriverUsers);
        buttonDriverMapDetail = root.findViewById(R.id.buttonDriverMapDetail);
        car = root.findViewById(R.id.imageViewCar);

        displayGroup = getArguments().getParcelable("displayDriverGroup");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        loadGroupUsers();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDetailDriverGroupName.setText("Nombre del grupo: "+ displayGroup.getNombre());
        tvDetailDriverOrigin.setText("Origen: " + displayGroup.getOrigen());
        tvDetailDriverDestination.setText("Destino: " + displayGroup.getDestino());
        tvDetailDriverDate.setText("Fecha: " + displayGroup.getFecha());
        tvDetailDriverFee.setText("Tarifa: " + displayGroup.getTarifa());
        tvDetailDriverPlaca.setText(displayGroup.getPlaca());
        tvDetailDriverMarca.setText(displayGroup.getMarca() + " " + displayGroup.getModelo());

        if (!displayGroup.getUrlImagen().equals("N/A"))
            Glide.with(getContext()).load(displayGroup.getUrlImagen()).into(car);


        listDriverGroupUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        buttonDriverMapDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverMapDetailFragment driverMapDetailFragment = new DriverMapDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("displayDriverGroupInfo", displayGroup);
                driverMapDetailFragment.setArguments(bundle);
                replaceFragment(driverMapDetailFragment);
            }
        });

        buttonModifyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateGroupFragment updateGroupFragment = new UpdateGroupFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("displayDriverGroup", displayGroup);
                updateGroupFragment.setArguments(bundle);
                replaceFragment(updateGroupFragment);
            }
        });

        buttonRemoveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("¿Desea eliminar el grupo " + displayGroup.getNombre() + "?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar grupo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef = database.getReference(FB_GROUPS_PATH + displayGroup.getIdGrupo());
                                myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            myRef = database.getReference(FB_USERS_PATH);
                                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(DataSnapshot single : task.getResult().getChildren()){
                                                            for(DataSnapshot minisingle : single.child(FB_GROUPS_PATH).getChildren()){
                                                                if(minisingle.getKey().equals(displayGroup.getIdGrupo()))
                                                                    minisingle.getRef().removeValue();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                            myRef = database.getReference(FB_DRIVERS_PATH + auth.getCurrentUser().getUid()).child(FB_GROUPS_PATH + displayGroup.getIdGrupo());
                                            myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    replaceFragment(new DriverGroupFragment());
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }).show();
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("¿Desea iniciar el grupo " + displayGroup.getNombre() + "?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef = database.getReference(FB_DRIVERS_PATH + auth.getCurrentUser().getUid()).child(FB_GROUPS_PATH + displayGroup.getIdGrupo());
                                myRef.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            myRef = database.getReference(FB_USERS_PATH);
                                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(DataSnapshot single : task.getResult().getChildren()){
                                                            for(DataSnapshot minisingle : single.child(FB_GROUPS_PATH).getChildren()){
                                                                if(minisingle.getKey().equals(displayGroup.getIdGrupo())){
                                                                    minisingle.getRef().setValue(true);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                Intent intent = new Intent(getContext(), TripActivity.class);
                                intent.putExtra("displayDriverGroupInfo", displayGroup);
                                startActivity(intent);
                            }
                        }).show();
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.driver_nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void loadGroupUsers(){
        listDriverGroupUsers.setAdapter(null);
        ArrayList<Usuario> usuarios = new ArrayList<>();
        myRef = database.getReference(FB_GROUPS_PATH);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for(DataSnapshot single : task.getResult().getChildren()){
                        Grupo grupo = single.getValue(Grupo.class);
                        if(grupo.getNombreGrupo().equals(displayGroup.getNombre())){
                            String key = single.getKey();
                            myRef = database.getReference(FB_USERS_PATH);
                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DataSnapshot single : task.getResult().getChildren()){
                                            Usuario usuario = single.getValue(Usuario.class);
                                            for(DataSnapshot superSingle : single.child(FB_GROUPS_PATH).getChildren()){
                                                if(key.equals(superSingle.getKey()) && !grupo.getIdConductor().equals(single.getKey()))
                                                    usuarios.add(usuario);
                                            }
                                        }
                                        if(getActivity() != null) {
                                            GroupUsersAdapter groupUsersAdapter = new GroupUsersAdapter(getActivity(), usuarios);
                                            listDriverGroupUsers.setAdapter(groupUsersAdapter);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}