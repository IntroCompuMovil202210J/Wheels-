package com.example.wheelsplus;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import adapters.GroupUsersAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import display.DisplayGroup;
import model.Grupo;
import model.Usuario;
import services.DownloadImageTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupDetailFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    TextView tvDetailGroupName, tvDetailOrigin, tvDetailDestination, tvDetailDate, tvDetailDriverName;
    ImageButton buttonRemoveGroup;
    ListView listGroupUsers;

    /**
     * Firebase
     */
    FirebaseDatabase database;
    DatabaseReference myRef;

    /**
     * Utils
     */
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_GROUPS_PATH = "groups/";
    DisplayGroup displayGroup;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupDetailFragment newInstance(String param1, String param2) {
        GroupDetailFragment fragment = new GroupDetailFragment();
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
        root = inflater.inflate(R.layout.fragment_group_detail, container, false);

        tvDetailGroupName = root.findViewById(R.id.tvDetailGroupName);
        tvDetailOrigin = root.findViewById(R.id.tvDetailOrigin);
        tvDetailDestination = root.findViewById(R.id.tvDetailDestination);
        tvDetailDate = root.findViewById(R.id.tvDetailDate);
        tvDetailDriverName = root.findViewById(R.id.tvDetailDriverName);
        buttonRemoveGroup = root.findViewById(R.id.buttonRemoveGroup);
        listGroupUsers = root.findViewById(R.id.listGroupUsers);

        displayGroup = getArguments().getParcelable("displayGroup");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        loadGroupUsers();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDetailGroupName.setText(displayGroup.getNombreGrupo());
        tvDetailOrigin.setText("Origen: " + displayGroup.getOrigen());
        tvDetailDestination.setText("Destino: " + displayGroup.getDestino());
        tvDetailDate.setText("Fecha: " + displayGroup.getFecha());
        tvDetailDriverName.setText(displayGroup.getNombreConductor());
        new DownloadImageTask((CircleImageView) root.findViewById(R.id.profilePicDriver))
                .execute(displayGroup.getUrlFoto());

        listGroupUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        buttonRemoveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                    .setTitle("¿Desea salir del grupo " + displayGroup.getNombreGrupo() + "?")
                    .setNegativeButton("Volver", null)
                    .setPositiveButton("Salir del grupo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            myRef = database.getReference(FB_GROUPS_PATH);
                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DataSnapshot single : task.getResult().getChildren()) {
                                            Grupo grupo = single.getValue(Grupo.class);
                                            if(grupo.getNombreGrupo().equals(displayGroup.getNombreGrupo())) {
                                                String key = single.getKey();
                                                myRef = database.getReference(FB_USERS_PATH);
                                                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            for(DataSnapshot single : task.getResult().getChildren()){
                                                                for(DataSnapshot superSingle : single.child(FB_GROUPS_PATH).getChildren()){
                                                                    if(key.equals(superSingle.getKey())){
                                                                        myRef = database.getReference(FB_GROUPS_PATH + grupo.getId_Grupo());
                                                                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                                if(task.isSuccessful()){
                                                                                    Grupo gr = task.getResult().getValue(Grupo.class);
                                                                                    myRef = database.getReference(FB_GROUPS_PATH + grupo.getId_Grupo()).child("cupo");
                                                                                    myRef.setValue(gr.getCupo() + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            superSingle.getRef().removeValue();
                                                                                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                                                            replaceFragment(new GroupFragment());
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
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
                    })
                    .show();
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    private void loadGroupUsers(){
        listGroupUsers.setAdapter(null);
        ArrayList<Usuario> usuarios = new ArrayList<>();
        myRef = database.getReference(FB_GROUPS_PATH);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for(DataSnapshot single : task.getResult().getChildren()){
                        Grupo grupo = single.getValue(Grupo.class);
                        if(grupo.getNombreGrupo().equals(displayGroup.getNombreGrupo())){
                            String key = single.getKey();
                            myRef = database.getReference(FB_USERS_PATH);
                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DataSnapshot single : task.getResult().getChildren()){
                                            Usuario usuario = single.getValue(Usuario.class);
                                            for(DataSnapshot superSingle : single.child(FB_GROUPS_PATH).getChildren()){
                                                if(key.equals(superSingle.getKey()) && !grupo.getIdConductor().equals(single.getKey())){
                                                    usuarios.add(usuario);
                                                }
                                            }
                                        }
                                        if(getActivity() != null) {
                                            GroupUsersAdapter groupUsersAdapter = new GroupUsersAdapter(getActivity(), usuarios);
                                            listGroupUsers.setAdapter(groupUsersAdapter);
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