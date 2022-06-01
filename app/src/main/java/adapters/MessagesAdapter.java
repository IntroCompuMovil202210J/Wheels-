package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wheelsplus.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import model.Mensaje;
import services.DownloadImageTask;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_IMG_TYPE_LEFT = 2;
    public static final int MSG_IMG_TYPE_RIGHT = 3;

    Context mContext;
    ArrayList<Mensaje> mensajes;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    public MessagesAdapter(Context mContext, ArrayList<Mensaje> mensajes) {
        this.mContext = mContext;
        this.mensajes = mensajes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_LEFT){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
        }else if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
        }else if(viewType == MSG_IMG_TYPE_LEFT){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_img_left, parent, false);
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_img_right, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mensajes.get(position).getTipo().equals("TEXT")){
            holder.tvMessage.setText(mensajes.get(position).getDato());
        }else{
            new DownloadImageTask((ImageView) holder.ivMessage)
                    .execute(mensajes.get(position).getDato());
        }
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mensajes.get(position).getIdEnvio().equals(auth.getCurrentUser().getUid())){
            if(mensajes.get(position).getTipo().equals("TEXT")) {
                return MSG_TYPE_RIGHT;
            }else{
                return MSG_IMG_TYPE_RIGHT;
            }
        }else{
            if(mensajes.get(position).getTipo().equals("TEXT")){
                return MSG_TYPE_LEFT;
            }else{
                return MSG_IMG_TYPE_LEFT;
            }
        }
    }

    public void addMessage(Mensaje mensaje){
        mensajes.add(mensaje);
        notifyItemInserted(getItemCount() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMessage;
        ImageView ivMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivMessage = itemView.findViewById(R.id.ivMessage);
        }


    }

}
