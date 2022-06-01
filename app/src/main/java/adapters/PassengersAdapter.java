package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wheelsplus.HomeFragment;
import com.example.wheelsplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.PuntoRuta;
import model.Usuario;
import services.DownloadImageTask;

public class PassengersAdapter extends ArrayAdapter<Usuario> {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public static final String FB_USERS_PATH = "users/";
    public static final String FB_GROUPS_PATH = "groups/";
    public static final String FB_ROUTE_PATH = "ruta/";

    public PassengersAdapter(Context context, ArrayList<Usuario> usuarios){
        super(context, 0, usuarios);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.trip_passenger_row, parent, false);
        }
        Usuario usuario = getItem(position);
        TextView tvPassengerUsername = v.findViewById(R.id.tvPassengerUsername);
        ImageButton buttonInUser = v.findViewById(R.id.buttonInUser);
        ImageButton buttonOutUser = v.findViewById(R.id.buttonOutUser);
        tvPassengerUsername.setText(usuario.getNombre() + " " + usuario.getApellido());
        buttonInUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference(FB_USERS_PATH + usuario.getIdUsuario()).child(FB_GROUPS_PATH);
                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            for(DataSnapshot single : task.getResult().getChildren()){
                                if(single.getValue(Boolean.class)){
                                    myRef = database.getReference(FB_GROUPS_PATH + single.getKey()).child(FB_ROUTE_PATH);
                                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(DataSnapshot superSingle : task.getResult().getChildren()){
                                                    PuntoRuta puntoRuta = superSingle.getValue(PuntoRuta.class);
                                                    if(puntoRuta.getIdUsuario().equals(usuario.getIdUsuario())){
                                                        myRef = database.getReference(FB_USERS_PATH + auth.getCurrentUser().getUid());
                                                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    Usuario actual = task.getResult().getValue(Usuario.class);
                                                                    if(distance(actual.getLatitud(), actual.getLongitud(), puntoRuta.getLatitud(), puntoRuta.getLongitud()) <= 0.1){
                                                                        superSingle.getRef().removeValue();
                                                                    }else{
                                                                        Toast.makeText(getContext(), "No estas cerca a " + usuario.getNombre() + " " + usuario.getApellido() + " para recogerlo", Toast.LENGTH_SHORT).show();
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
                        }
                    }
                });
            }
        });

        buttonOutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference(FB_USERS_PATH + usuario.getIdUsuario()).child(FB_GROUPS_PATH);
                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            for(DataSnapshot single : task.getResult().getChildren()){
                                if(single.getValue(Boolean.class)){
                                    myRef = database.getReference(FB_GROUPS_PATH + single.getKey()).child(FB_ROUTE_PATH);
                                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(DataSnapshot superSingle : task.getResult().getChildren()){
                                                    PuntoRuta puntoRuta = superSingle.getValue(PuntoRuta.class);
                                                    if(puntoRuta.getIdUsuario().equals(usuario.getIdUsuario())){
                                                        superSingle.getRef().removeValue();
                                                        single.getRef().removeValue();
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
        });
        new DownloadImageTask((CircleImageView) v.findViewById(R.id.profilePicPassenger))
                .execute(usuario.getUrlFoto());
        return v;
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
