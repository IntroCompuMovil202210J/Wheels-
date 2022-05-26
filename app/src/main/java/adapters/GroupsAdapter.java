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
import display.DisplayGroup;
import services.DownloadImageTask;

public class GroupsAdapter extends ArrayAdapter<DisplayGroup> {
    public GroupsAdapter(Context context, ArrayList<DisplayGroup> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.group_row, parent, false);
        }
        DisplayGroup displayGroup = getItem(position);
        TextView tvDriverName = v.findViewById(R.id.tvDriverNameGroup);
        TextView tvGroupName = v.findViewById(R.id.tvGroupName);
        TextView tvGroupOrigin = v.findViewById(R.id.tvOriginGroup);
        TextView tvGroupDestination = v.findViewById(R.id.tvDestinationGroup);
        TextView tvGroupFee = v.findViewById(R.id.tvGroupFee);
        tvDriverName.setText(displayGroup.getNombreConductor());
        tvGroupName.setText(displayGroup.getNombreGrupo());
        tvGroupOrigin.setText(displayGroup.getOrigen());
        tvGroupDestination.setText(displayGroup.getDestino());
        tvGroupFee.setText(displayGroup.getTarifa());
        new DownloadImageTask((CircleImageView) v.findViewById(R.id.profilePicDriver))
                .execute(displayGroup.getUrlFoto());
        return v;
    }

}
