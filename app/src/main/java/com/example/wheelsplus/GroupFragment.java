package com.example.wheelsplus;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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
    TextInputEditText editGroupDestination, editGroupRoute;
    TextView tvSelectedTime;

    /**
     * Utils
     */
    long groupTimestamp = 0;

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
        editGroupRoute = root.findViewById(R.id.editGroupRoute);
        tvSelectedTime = root.findViewById(R.id.tvSelectedTime);

        geocoder = new Geocoder(getActivity().getBaseContext());

        groupTimestamp = 0;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        tvSelectedTime.setText("Hora de salida: " + hour + " : " + minute);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        groupTimestamp = calendar.getTimeInMillis();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        buttonSearchGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                if(TextUtils.isEmpty(editGroupDestination.getText())){
                    editGroupDestination.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(groupTimestamp == 0){
                    Toast.makeText(view.getContext(), "Hora de acuerdo requerida", Toast.LENGTH_SHORT).show();
                }else{
                    flag = false;
                }
                if(flag){
                    LatLng latLng = searchLatLngAddress(editGroupDestination.getText().toString());
                    if(latLng != null) {
                        Log.i("Timestamp", String.valueOf(groupTimestamp));
                        Log.i("LatLn", latLng.toString());
                    }else{
                        Toast.makeText(view.getContext(), "Dirección no encontrada, intente de nuevo", Toast.LENGTH_SHORT).show();
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

}