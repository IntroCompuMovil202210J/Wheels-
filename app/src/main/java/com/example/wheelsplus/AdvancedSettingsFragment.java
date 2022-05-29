package com.example.wheelsplus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdvancedSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdvancedSettingsFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    SwitchMaterial switchFingerprint;

    /**
     * Firebase
     */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    /**
     * Utils
     */
    boolean activated = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdvancedSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdvancedSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdvancedSettingsFragment newInstance(String param1, String param2) {
        AdvancedSettingsFragment fragment = new AdvancedSettingsFragment();
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
        root = inflater.inflate(R.layout.fragment_advanced_settings, container, false);

        switchFingerprint = root.findViewById(R.id.switchFingerprint);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        myRef = database.getReference(RegisterActivity.FB_FINGERPRINT_PATH).child(auth.getCurrentUser().getUid());
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    activated = (boolean) task.getResult().getValue();
                    if(activated){
                        switchFingerprint.setChecked(true);
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchFingerprint.isChecked()){
                    BiometricManager biometricManager = BiometricManager.from(getContext());
                    switch (biometricManager.canAuthenticate()) {
                        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                            Toast.makeText(getActivity().getBaseContext(), "No tienes el hardware para la app", Toast.LENGTH_SHORT).show();
                            break;

                        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                            Toast.makeText(getActivity().getBaseContext(), "El hardware está ocupado para la app", Toast.LENGTH_SHORT).show();
                            break;

                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                            Toast.makeText(getActivity().getBaseContext(), "No tienes huella registrada en el dispositivo", Toast.LENGTH_SHORT).show();
                            break;

                        case BiometricManager.BIOMETRIC_SUCCESS:
                            myRef = database.getReference(RegisterActivity.FB_FINGERPRINT_PATH + auth.getCurrentUser().getUid());
                            myRef.setValue(true);
                            switchFingerprint.setChecked(true);
                            Toast.makeText(getActivity().getBaseContext(), "Autenticación por huella activada", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    myRef = database.getReference(RegisterActivity.FB_FINGERPRINT_PATH + auth.getCurrentUser().getUid());
                    myRef.setValue(false);
                    switchFingerprint.setChecked(false);
                    Toast.makeText(getActivity().getBaseContext(), "Autenticación por huella desactivada", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}