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
import display.DisplayGroupDriver;
import services.DownloadImageTask;

public class DriverGroupsAdapter  extends ArrayAdapter<DisplayGroupDriver> {
    public DriverGroupsAdapter(Context context, ArrayList<DisplayGroupDriver> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.group_driver_row, parent, false);
        }
        DisplayGroupDriver displayGroup = getItem(position);
        TextView tvGroupName = v.findViewById(R.id.tvGrNameDriver);
        TextView tvGroupOrigin = v.findViewById(R.id.tvGrOriginDriver);
        TextView tvGroupDestination = v.findViewById(R.id.tvGrDestDriver);
        TextView tvGroupFee = v.findViewById(R.id.tvGrFeeDriver);
        TextView tvFecha = v.findViewById(R.id.tvGrDateDriver);
        TextView tvPlaca = v.findViewById(R.id.tvGrPlacaDriver);
        tvGroupName.setText(displayGroup.getNombre());
        tvGroupOrigin.setText("Origen: " + displayGroup.getOrigen());
        tvGroupDestination.setText("Destino: " + displayGroup.getDestino());
        tvGroupFee.setText("Tarifa: " + displayGroup.getTarifa());
        tvFecha.setText("Fecha: " + displayGroup.getFecha());
        tvPlaca.setText("Placa: " + displayGroup.getPlaca());
        return v;
    }

}
