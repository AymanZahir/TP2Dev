package ca.qc.bdeb.p55.tp1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LieuxAdapter extends RecyclerView.Adapter<LieuxAdapter.ItemViewHolder> {
    private ArrayList<Lieux> rviList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // si jamais notre item layout contenait plusieurs views, on les rajoute ici
        public ImageView ImageLieux;
        public TextView txtViewNom;
        public TextView txtViewType;
        public TextView txtViewNbr;
        public ImageView imageFavori;

        public ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            // si jamais notre item layout contenait plusieurs views, on les rajoute ici

            ImageLieux = itemView.findViewById(R.id.ImageLieux);
            txtViewNom = itemView.findViewById(R.id.txtViewNom);
            txtViewType = itemView.findViewById(R.id.txtViewType);
            txtViewNbr = itemView.findViewById(R.id.txtViewNbr);
            imageFavori = itemView.findViewById(R.id.imageFavori);

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // vérifier que le listener n'est pas null
                    if (listener != null) {
                        int position = getAdapterPosition();
                        // la position est valide?
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                        }
                    }

                }
            });

        }
    }

    public LieuxAdapter(ArrayList<Lieux> list) {
        this.rviList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_lieux, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(v, listener);
        return ivh;
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Lieux item = rviList.get(position); // pas besoin d'appeller findViewById
        holder.txtViewNom.setText(item.getNom());
      //  holder.txtViewNbr.setText(item.getNombreVisites());
    }

    @Override
    public int getItemCount() {
        return rviList.size();
    }
}
