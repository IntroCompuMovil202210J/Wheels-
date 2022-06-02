package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wheelsplus.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import display.DisplayGroup;
import model.Vehiculo;
import services.DownloadImageTask;


public class VehiclesAdapter extends ArrayAdapter<Vehiculo> {

    Context contexto;
    public VehiclesAdapter(Context context, ArrayList<Vehiculo> groups) {
        super(context, 0, groups);
        contexto = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.cars_row, parent, false);
        }
        Vehiculo vehiculo = getItem(position);
        TextView tvPlaca = v.findViewById(R.id.tvPlaca);
        TextView tvMarca = v.findViewById(R.id.tvMarca);
        TextView tvModelo = v.findViewById(R.id.tvModelo);
        TextView tvCapacidad = v.findViewById(R.id.tvCapacidad);
        ImageView imagenCarro = v.findViewById(R.id.imageView5);


        if (!vehiculo.getUrlImagen().equals("N/A"))
            Glide.with(contexto).load(vehiculo.getUrlImagen()).into(imagenCarro);


        tvPlaca.setText("Placa: " + vehiculo.getPlaca());
        tvCapacidad.setText("Capacidad: " + String.valueOf(vehiculo.getCapacidad()));
        tvModelo.setText("Modelo: " + vehiculo.getModelo());
        tvMarca.setText("Marca: " + vehiculo.getMarca());
        return v;
    }
}
