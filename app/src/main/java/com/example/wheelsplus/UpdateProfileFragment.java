package com.example.wheelsplus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import services.DownloadImageTask;

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
    CircleImageView profilePicture;
    TextInputEditText editChangeName, editChangeLastname, editChangePhone;
    Button buttonUpdateProfile;

    /**
     * Firebase
     */
    Uri uriProfilePic = null;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageReference;

    /**
     * Utils
     */
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_USERS_PP = "profilePics/";
    boolean clicked = false;

    ActivityResultLauncher<String> mGetContentGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result != null){
                uriProfilePic = result;
                loadImage(uriProfilePic);
            }
        }
    });

    ActivityResultLauncher<Uri> mGetContentCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                loadImage(uriProfilePic);
            }
        }
    });


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
        profilePicture = root.findViewById(R.id.editProfilePic);
        editChangeName = root.findViewById(R.id.editChangeName);
        editChangeLastname = root.findViewById(R.id.editChangeLastname);
        editChangePhone = root.findViewById(R.id.editChangePhone);
        buttonUpdateProfile = root.findViewById(R.id.buttonUpdateProfile);

        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();

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
                File file = new File(getContext().getFilesDir(), "picFromCamera");
                uriProfilePic = FileProvider.getUriForFile(view.getContext(), getContext().getApplicationContext().getPackageName() + ".fileprovider", file);
                mGetContentCamera.launch(uriProfilePic);
                setVisibility();
                setAnimation();
                clicked = !clicked;
            }
        });

        buttonChangeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContentGallery.launch("image/*");
                setVisibility();
                setAnimation();
                clicked = !clicked;
            }
        });

        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        new DownloadImageTask((CircleImageView) root.findViewById(R.id.editProfilePic))
                .execute(auth.getCurrentUser().getPhotoUrl().toString());

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
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

    private void loadImage(Uri uri){
        try {
            final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
            final Bitmap image = BitmapFactory.decodeStream(imageStream);
            profilePicture.setImageBitmap(image);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateProfile(){
        storageReference = storage.getReference(FB_USERS_PP + auth.getCurrentUser().getUid());
        storageReference.putFile(uriProfilePic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String name = editChangeName.getText().toString();
                        String lastname = editChangeLastname.getText().toString();
                        String phone = editChangePhone.getText().toString();
                        String url = uri.toString();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(FB_USERS_PATH + firebaseUser.getUid() + "/nombre", name);
                        childUpdates.put(FB_USERS_PATH + firebaseUser.getUid() + "/apellido", lastname);
                        childUpdates.put(FB_USERS_PATH + firebaseUser.getUid() + "/telefono", phone);
                        childUpdates.put(FB_USERS_PATH + firebaseUser.getUid() + "/urlFoto", url);
                        myRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                                    upcrb.setDisplayName(name + " " + lastname);
                                    upcrb.setPhotoUri(uri);
                                    firebaseUser.updateProfile(upcrb.build()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("FirebaseUser", "Profile updated");
                                            replaceFragment(new SettingsFragment());
                                            Snackbar.make(root, "Información de usuario actualizada", Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("FirebaseUser", "Profile saving failure");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Storage", "Image could not be saved in FirebaseStorage");
            }
        });
    }

}