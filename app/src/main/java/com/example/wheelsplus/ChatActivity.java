package com.example.wheelsplus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import adapters.MessagesAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Chat;
import model.Mensaje;
import model.Usuario;
import services.DownloadImageTask;

public class ChatActivity extends AppCompatActivity {

    TextView tvOtherUserName;
    RecyclerView recyclerView;
    EditText editTextMessage;
    ImageButton buttonSendImage, buttonSendGallery, buttonSendMessage;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageReference;
    ValueEventListener vel;

    public static final String FB_USERS_PATH = "users/";
    public static final String FB_CHATS_PATH = "chats/";
    public static final String FB_MESSAGES_PATH = "messages/";
    public static final String FB_CHAT_CONTENT = "chatContent/";
    String chatKey, fromActivity;
    Usuario otherUser;
    MessagesAdapter messagesAdapter = new MessagesAdapter(this, new ArrayList<>());
    ArrayList<Mensaje> mensajes = new ArrayList<>();
    Uri uri = null;

    ActivityResultLauncher<String> mGetContentGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result != null){
                uri = result;
                String key = myRef.push().getKey();
                storageReference = storage.getReference(FB_CHAT_CONTENT + chatKey).child(key);
                storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        myRef = database.getReference(FB_CHATS_PATH + chatKey).child(FB_MESSAGES_PATH);
                                        Calendar calendar = Calendar.getInstance();
                                        myRef.push().setValue(new Mensaje(task.getResult().toString(), calendar.getTimeInMillis(), auth.getCurrentUser().getUid(), "IMAGE"));
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    });

    ActivityResultLauncher<Uri> mGetContentCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                String key = myRef.push().getKey();
                storageReference = storage.getReference(FB_CHAT_CONTENT + chatKey).child(key);
                storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        myRef = database.getReference(FB_CHATS_PATH + chatKey).child(FB_MESSAGES_PATH);
                                        Calendar calendar = Calendar.getInstance();
                                        myRef.push().setValue(new Mensaje(task.getResult().toString(), calendar.getTimeInMillis(), auth.getCurrentUser().getUid(), "IMAGE"));
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);

        chatKey = getIntent().getStringExtra("chatKey");
        if(getIntent().getStringExtra("fromActivity") != null){
            fromActivity = getIntent().getStringExtra("fromActivity");
        }else{
            fromActivity = "tripDetail";
        }

        tvOtherUserName = findViewById(R.id.tvOtherChatUsername);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messagesAdapter);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendImage = findViewById(R.id.buttonSendImage);
        buttonSendGallery = findViewById(R.id.buttonSendGallery);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        myRef = database.getReference(FB_CHATS_PATH + chatKey);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Chat chat = task.getResult().getValue(Chat.class);
                    String other;
                    if (chat.getIdEmisor().equals(auth.getCurrentUser().getUid())) {
                        other = chat.getIdReceptor();
                    } else {
                        other = chat.getIdEmisor();
                    }
                    myRef = database.getReference(FB_USERS_PATH + other);
                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                otherUser = task.getResult().getValue(Usuario.class);
                                tvOtherUserName.setText(otherUser.getNombre() + " " + otherUser.getApellido());
                                new DownloadImageTask((CircleImageView) findViewById(R.id.profilePicOtherUser))
                                        .execute(otherUser.getUrlFoto());
                            }
                        }
                    });
                }
            }
        });

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference(FB_CHATS_PATH + chatKey).child(FB_MESSAGES_PATH);
                String message = editTextMessage.getText().toString();
                if(!message.equals("")){
                    Calendar calendar = Calendar.getInstance();
                    myRef.push().setValue(new Mensaje(message, calendar.getTimeInMillis(), auth.getCurrentUser().getUid(), "TEXT"));
                    editTextMessage.setText("");
                }
            }
        });

        buttonSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(getFilesDir(), "picFromCamera");
                uri = FileProvider.getUriForFile(view.getContext(), getApplicationContext().getPackageName() + ".fileprovider", file);
                mGetContentCamera.launch(uri);
            }
        });

        buttonSendGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContentGallery.launch("image/*");
            }
        });

        readMessages();

    }

    @Override
    protected void onStart() {
        super.onStart();
        readMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myRef != null){
            myRef.removeEventListener(vel);
        }
    }

    @Override
    public void onBackPressed() {
        if(!fromActivity.equals("tripDetail")){
            startActivity(new Intent(this, NavActivity.class));
        }else{
            super.onBackPressed();
        }
    }

    private void readMessages(){
        myRef = database.getReference(FB_CHATS_PATH + chatKey).child(FB_MESSAGES_PATH);
        vel = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mensajes.clear();
                for(DataSnapshot single : snapshot.getChildren()){
                    Mensaje mensaje = single.getValue(Mensaje.class);
                    mensajes.add(mensaje);
                }
                messagesAdapter = new MessagesAdapter(ChatActivity.this, mensajes);
                recyclerView.setAdapter(messagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}