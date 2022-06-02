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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import services.DownloadImageTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverSettingsFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    LinearLayout layCerrarSesion;
    ImageButton buttonModify;
    CircleImageView profilePic;
    TextView settingsName;
    LinearLayout trip, pay, adv, pass, cars;

    /**
     * Firebase
     */
    FirebaseAuth auth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverSettingsFragment newInstance(String param1, String param2) {
        DriverSettingsFragment fragment = new DriverSettingsFragment();
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
        root =  inflater.inflate(R.layout.fragment_driver_settings, container, false);

        layCerrarSesion = root.findViewById(R.id.layCerrarSesionD);
        buttonModify = root.findViewById(R.id.buttonModifyD);
        profilePic = root.findViewById(R.id.profilePicD);
        settingsName = root.findViewById(R.id.settingsNameD);
        trip = root.findViewById(R.id.layViajesD);
        pay = root.findViewById(R.id.layPayD);
        adv = root.findViewById(R.id.layAdvancedD);
        pass = root.findViewById(R.id.layPassenger);
        cars = root.findViewById(R.id.layCars);

        auth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                    .setTitle("¿Desea cerrar sesión?")
                    .setNegativeButton("Volver", null)
                    .setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            auth.signOut();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .show();
            }
        });

        adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AdvancedSettingsFragment());
            }
        });

        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), VehicleRegisterActivity.class));
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(view.getContext())
                    .setTitle("¿Cambiar a pasajero?")
                    .setMessage("¿Estás seguro que quieres moverte a tu cuenta de pasajero?")
                    .setNegativeButton("Rechazar", null)
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(view.getContext(), NavActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .show();
            }
        });

        trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new DisplayTripsFragment());
            }
        });

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new UpdateProfileFragment());
            }
        });

        settingsName.setText(auth.getCurrentUser().getDisplayName());
        new DownloadImageTask((CircleImageView) root.findViewById(R.id.profilePicD))
                .execute(auth.getCurrentUser().getPhotoUrl().toString());

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.driver_nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }
}