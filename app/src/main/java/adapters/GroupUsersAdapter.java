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
import model.Usuario;
import services.DownloadImageTask;

public class GroupUsersAdapter extends ArrayAdapter<Usuario> {

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
        tvGroupUsername.setText(usuario.getNombre() + " " + usuario.getApellido());
        new DownloadImageTask((CircleImageView) v.findViewById(R.id.profilePicUsers))
                .execute(usuario.getUrlFoto());
        return v;
    }
}
