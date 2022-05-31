package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wheelsplus.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import display.DisplayChat;
import services.DownloadImageTask;

public class ChatsAdapter extends ArrayAdapter<DisplayChat> {

    public ChatsAdapter(Context context, ArrayList<DisplayChat> displayChats) {
        super(context, 0, displayChats);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.chat_row, parent, false);
        }
        DisplayChat displayChat = getItem(position);
        TextView tvNameContact = v.findViewById(R.id.tvChatName);
        TextView tvMessageContent = v.findViewById(R.id.tvChatContent);
        TextView tvChatLastHour = v.findViewById(R.id.tvChatLastHour);
        tvNameContact.setText(displayChat.getNombreChat());
        tvMessageContent.setText(displayChat.getUltimoMensaje());
        tvChatLastHour.setText(displayChat.getHoraUltimoMensaje());
        new DownloadImageTask((CircleImageView) v.findViewById(R.id.profilePicChat))
                .execute(displayChat.getUrlFoto());
        return v;
    }
}
