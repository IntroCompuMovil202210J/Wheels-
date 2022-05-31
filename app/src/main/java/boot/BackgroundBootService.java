package boot;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wheelsplus.MainActivity;
import com.example.wheelsplus.NavActivity;
import com.example.wheelsplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import model.Grupo;

public class BackgroundBootService extends Service {

    public static final String CHANNEL_ID = "WheelsPlus";
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_GROUPS_PATH = "groups/";

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef, myRefMap;
    ChildEventListener vel, velM;

    String uuid;
    Map<String, String> mapGroup = new HashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        createNotificationChannel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myRef != null){
            myRef.removeEventListener(vel);
            myRefMap.removeEventListener(velM);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        startForeground(2, buildComplexNotification("WheelsPlus", "Connected", R.drawable.logo_wheels));
        if(auth.getCurrentUser() != null) {
            myRef = database.getReference(FB_USERS_PATH + auth.getCurrentUser().getUid()).child("groups");
            vel = myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    myRef = database.getReference(FB_GROUPS_PATH + snapshot.getKey());
                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                Grupo grupo = task.getResult().getValue(Grupo.class);
                                Log.i("OSUNA", grupo.getNombreGrupo());
                                mapGroup.put(grupo.getId_Grupo(), grupo.getNombreGrupo());
                            }
                        }
                    });
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String grupoId = snapshot.getKey();
                    groupStarted(mapGroup.get(grupoId));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    String grupoId = snapshot.getKey();
                    mapGroup.remove(grupoId);
                    groupRemoved(mapGroup.get(grupoId));
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return START_STICKY;
    }

    /*
    private void dataChanged(String username) {
        sendNotification("Taller3FOV", username + " actualizó su estado a disponible", R.drawable.ic_baseline_local_fire_department_24);
    }

    */

    private void groupRemoved(String groupname) {
        sendNotification("Wheelsplus", "El grupo " + groupname +" se ha eliminado", R.drawable.logo_wheels);
    }

    private void groupStarted(String groupname) {
        sendNotification("Wheelsplus", "El grupo " + groupname +" se ha eliminado", R.drawable.logo_wheels);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Notification buildComplexNotification(String title, String message, int icon){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setSmallIcon(icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(message);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent;
        if(auth.getCurrentUser() != null){
            intent = new Intent(this, NavActivity.class);
        }else{
            intent = new Intent(this, MainActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);

        return mBuilder.build();
    }

    private void sendNotification(String title, String message, int icon){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(7, buildComplexNotification(title, message, icon));
    }


}
