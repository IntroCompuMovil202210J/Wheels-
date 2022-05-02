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

public class GroupsAdapter extends CursorAdapter {
    public GroupsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.group_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvDriverName = view.findViewById(R.id.tvDriverNameGroup);
        TextView tvGroupName = view.findViewById(R.id.tvGroupName);
        TextView tvGroupOrigin = view.findViewById(R.id.tvOriginGroup);
        TextView tvGroupDestination = view.findViewById(R.id.tvDestinationGroup);
        tvDriverName.setText(cursor.getString(0));
        tvGroupName.setText(cursor.getString(1));
        tvGroupOrigin.setText(cursor.getString(2));
        tvGroupDestination.setText(cursor.getString(3));
        new DownloadImageTask((CircleImageView) view.findViewById(R.id.profilePicDriver))
                .execute(cursor.getString(4));
    }
}
