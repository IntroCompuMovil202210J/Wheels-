package adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.wheelsplus.R;

import de.hdodenhof.circleimageview.CircleImageView;
import services.DownloadImageTask;

public class ChatsAdapter extends CursorAdapter {
    public ChatsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNameContact = view.findViewById(R.id.tvChatName);
        TextView tvMessageContent = view.findViewById(R.id.tvChatContent);
        TextView tvCantMessages = view.findViewById(R.id.cantMessages);
        tvNameContact.setText(cursor.getString(0));
        tvMessageContent.setText(cursor.getString(1));
        tvCantMessages.setText(cursor.getString(2));
        new DownloadImageTask((CircleImageView) view.findViewById(R.id.profilePicChat))
                .execute(cursor.getString(3));
    }
}
