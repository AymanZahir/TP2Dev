package ca.qc.bdeb.p55.tp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LieuxAdapter extends RecyclerView.Adapter<LieuxAdapter.ItemViewHolder> {
    private ArrayList<Lieux> rviList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick(int position);

        void onFavoriClick(int position, ImageView imageViewFavori);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView ImageLieux;
        public TextView txtViewNom;
        public TextView txtViewType;
        public TextView txtViewNbr;
        public TextView txtViewTelephone;
        public ImageView imageFavori;
        public CardView cardView;

        public ItemViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            ImageLieux = itemView.findViewById(R.id.ImageLieux);
            txtViewNom = itemView.findViewById(R.id.txtViewNom);
            txtViewType = itemView.findViewById(R.id.txtViewType);
            txtViewNbr = itemView.findViewById(R.id.txtViewNbr);
            txtViewTelephone = itemView.findViewById(R.id.txtViewTelephone);
            imageFavori = itemView.findViewById(R.id.imageFavori);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
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

            imageFavori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // vérifier que le listener n'est pas null
                    if (listener != null) {
                        int position = getAdapterPosition();
                        // la position est valide?
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFavoriClick(position, imageFavori);
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
        return new ItemViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Lieux item = rviList.get(position);
        holder.txtViewNom.setText(item.getNom());
        String texte = "Nombres de visites : " + item.getNombreVisites();
        holder.txtViewNbr.setText(texte);

        holder.txtViewTelephone.setText(item.getTelephone());

        if (item.getFavori() == 0) {
            holder.imageFavori.setImageResource(R.drawable.ic_baseline_star_border_24);
        } else {
            holder.imageFavori.setImageResource(R.drawable.ic_baseline_star_24);
        }

        switch (item.getType()) {
            case 1:
                holder.txtViewType.setText("Point d'eau potable");
                holder.cardView.setCardBackgroundColor(Color.parseColor("#4CB4C3"));
                break;
            case 2:
                holder.txtViewType.setText("Aire de repos");
                holder.cardView.setCardBackgroundColor(Color.parseColor("#4E9F38"));
                break;
            case 3:
                holder.txtViewType.setText("Point observation");
                holder.cardView.setCardBackgroundColor(Color.parseColor("#DA8E2F"));
                break;
        }

        if (item.getImageResId() != null) {
            Bitmap bitmap = DbBitmapUtility.getImage(item.getImageResId());
           // bitmap.setWidth(82);
            //bitmap.setHeight(82);
            holder.ImageLieux.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return rviList.size();
    }
}
