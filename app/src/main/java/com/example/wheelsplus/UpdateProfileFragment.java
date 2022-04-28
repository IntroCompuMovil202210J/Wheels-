package com.example.wheelsplus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Animations
     */
    Animation rotateOpen, rotateClose, fromBottom, toBottom;

    /**
     * Screen elements (to inflate)
     */
    FloatingActionButton buttonChangeProfilePic, buttonChangeCam, buttonChangeGallery;

    /**
     * Utils
     */
    boolean clicked = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfileFragment newInstance(String param1, String param2) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
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
        root = inflater.inflate(R.layout.fragment_update_profile, container, false);

        buttonChangeProfilePic = root.findViewById(R.id.buttonChangeProfilePic);
        buttonChangeCam = root.findViewById(R.id.buttonChangeCam);
        buttonChangeGallery = root.findViewById(R.id.buttonChangeGallery);

        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonChangeCam.setClickable(false);
        buttonChangeGallery.setClickable(false);
        buttonChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility();
                setAnimation();
                clicked = !clicked;
            }
        });

        buttonChangeCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Camera", Toast.LENGTH_LONG).show();
            }
        });

        buttonChangeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Gallery", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setVisibility(){
        if(!clicked){
            buttonChangeCam.setVisibility(View.VISIBLE);
            buttonChangeGallery.setVisibility(View.VISIBLE);
            buttonChangeCam.setClickable(true);
            buttonChangeGallery.setClickable(true);
        }else{
            buttonChangeCam.setVisibility(View.INVISIBLE);
            buttonChangeGallery.setVisibility(View.INVISIBLE);
            buttonChangeCam.setClickable(false);
            buttonChangeGallery.setClickable(false);
        }
    }

    private void setAnimation(){
        if(!clicked){
            buttonChangeCam.startAnimation(fromBottom);
            buttonChangeGallery.startAnimation(fromBottom);
            buttonChangeProfilePic.startAnimation(rotateOpen);
        }else{
            buttonChangeCam.startAnimation(toBottom);
            buttonChangeGallery.startAnimation(toBottom);
            buttonChangeProfilePic.startAnimation(rotateClose);
        }
    }

}