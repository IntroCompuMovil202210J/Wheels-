package com.example.wheelsplus;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import adapters.ChatsAdapter;
import display.DisplayChat;
import model.Chat;
import model.Mensaje;
import model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayChatsFragment extends Fragment {

    /**
     * View
     */
    View root;

    /**
     * Screen elements (to inflate)
     */
    ListView listChats;

    /**
     * Firebase
     */
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ValueEventListener vel;

    /**
     * Utils
     */
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_CHATS_PATH = "chats/";
    public static final String FB_MESSAGES_PATH = "messages/";
    String lastMessage;
    long timeLastMessage;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    Queue<String> messages = new LinkedList<>();
    Queue<String> times = new LinkedList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplayChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayChatsFragment newInstance(String param1, String param2) {
        DisplayChatsFragment fragment = new DisplayChatsFragment();
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

        root = inflater.inflate(R.layout.fragment_display_chats, container, false);

        listChats = root.findViewById(R.id.listChats);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        loadChats();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DisplayChat displayChat = (DisplayChat) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("chatKey", displayChat.getIdChat());
                intent.putExtra("fromActivity", "displayChats");
                startActivity(intent);
            }
        });
    }

    private void loadChats(){
        listChats.setAdapter(null);
        myRef = database.getReference(FB_CHATS_PATH);
        vel = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myRef = database.getReference(FB_USERS_PATH + auth.getCurrentUser().getUid()).child(FB_CHATS_PATH);
                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<String> keyChats = new ArrayList<>();
                            ArrayList<Chat> chats = new ArrayList<>();
                            ArrayList<DisplayChat> displayChats = new ArrayList<>();
                            for(DataSnapshot single : task.getResult().getChildren()){
                                keyChats.add(single.getKey());
                            }
                            myRef = database.getReference(FB_CHATS_PATH);
                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DataSnapshot single : task.getResult().getChildren()){
                                            if(keyChats.contains(single.getKey())){
                                                chats.add(single.getValue(Chat.class));
                                            }
                                        }
                                        for(Chat chat : chats){
                                            String keySearch;
                                            if(chat.getIdEmisor().equals(auth.getCurrentUser().getUid())){
                                                keySearch = chat.getIdReceptor();
                                            }else{
                                                keySearch = chat.getIdEmisor();
                                            }
                                            myRef = database.getReference(FB_CHATS_PATH + chat.getIdChat()).child(FB_MESSAGES_PATH);
                                            myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        long count = task.getResult().getChildrenCount();
                                                        long aux = 0;
                                                        for(DataSnapshot minisingle : task.getResult().getChildren()){
                                                            if(aux == count - 1){
                                                                if(minisingle.getValue(Mensaje.class).getTipo().equals("TEXT")) {
                                                                    lastMessage = minisingle.getValue(Mensaje.class).getDato();
                                                                }else{
                                                                    lastMessage = "Foto";
                                                                }
                                                                timeLastMessage = minisingle.getValue(Mensaje.class).getFecha();
                                                                messages.offer(lastMessage);
                                                                times.offer(sdf.format(new Date(timeLastMessage)));
                                                            }
                                                            aux++;
                                                        }
                                                        myRef = database.getReference(FB_USERS_PATH + keySearch);
                                                        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                if(task.isSuccessful()){
                                                                    Usuario other = task.getResult().getValue(Usuario.class);
                                                                    displayChats.add(new DisplayChat(chat.getIdChat(), other.getUrlFoto(), other.getNombre() + " " + other.getApellido(), task.getResult().getKey(), messages.poll(), times.poll()));
                                                                    if(getActivity() != null){
                                                                        ChatsAdapter chatsAdapter = new ChatsAdapter(getActivity(), displayChats);
                                                                        listChats.setAdapter(chatsAdapter);
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}