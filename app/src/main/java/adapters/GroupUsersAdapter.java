package adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wheelsplus.ChatActivity;
import com.example.wheelsplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Chat;
import model.Usuario;
import services.DownloadImageTask;

public class GroupUsersAdapter extends ArrayAdapter<Usuario> {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public static final String FB_USERS_PATH = "users/";
    public static final String FB_CHATS_PATH = "chats/";

    public GroupUsersAdapter(Context context, ArrayList<Usuario> usuarios){
        super(context, 0, usuarios);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.group_user_row, parent, false);
        }
        Usuario usuario = getItem(position);
        TextView tvGroupUsername = v.findViewById(R.id.tvGroupUsername);
        ImageButton buttonChatUser = v.findViewById(R.id.buttonChatUser);
        tvGroupUsername.setText(usuario.getNombre() + " " + usuario.getApellido());
        buttonChatUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!auth.getCurrentUser().getUid().equals(usuario.getIdUsuario())){
                    myRef = database.getReference(FB_CHATS_PATH);
                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                Chat previousChat = null;
                                boolean exist = false;
                                for (DataSnapshot single : task.getResult().getChildren()) {
                                    Chat chat = single.getValue(Chat.class);
                                    if ((chat.getIdEmisor().equals(auth.getCurrentUser().getUid()) && chat.getIdReceptor().equals(usuario.getIdUsuario())) || (chat.getIdEmisor().equals(usuario.getIdUsuario()) && chat.getIdReceptor().equals(auth.getCurrentUser().getUid()))) {
                                        previousChat = chat;
                                        exist = true;
                                    }
                                }
                                if (exist) {
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra("chatKey", previousChat.getIdChat());
                                    intent.putExtra("otherUser", usuario.getIdUsuario());
                                    getContext().startActivity(intent);
                                } else {
                                    new MaterialAlertDialogBuilder(view.getContext())
                                            .setTitle("¿Desea iniciar un chat con " + usuario.getNombre() + " " + usuario.getApellido() + "?")
                                            .setNegativeButton("Volver", null)
                                            .setPositiveButton("Crear chat", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String key = myRef.push().getKey();
                                                    myRef = database.getReference(FB_CHATS_PATH + key);
                                                    myRef.setValue(new Chat(key, auth.getCurrentUser().getUid(), usuario.getIdUsuario())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Map<String, Object> childUpdates = new HashMap<>();
                                                                myRef = database.getReference();
                                                                childUpdates.put(FB_USERS_PATH + usuario.getIdUsuario() + "/" + FB_CHATS_PATH + key, true);
                                                                childUpdates.put(FB_USERS_PATH + auth.getCurrentUser().getUid() + "/" + FB_CHATS_PATH + key, true);
                                                                myRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(view.getContext(), "Chat creado correctamente", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getContext(), ChatActivity.class);
                                                                        intent.putExtra("chatKey", key);
                                                                        intent.putExtra("fromActivity", "groupDetail");
                                                                        getContext().startActivity(intent);
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            })
                                            .show();
                                }
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "No se puede realizar un chat con si mismo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        new DownloadImageTask((CircleImageView) v.findViewById(R.id.profilePicUsers))
                .execute(usuario.getUrlFoto());
        return v;
    }
}
