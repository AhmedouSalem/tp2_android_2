package fr.umontpellier.android_tp2_2.adepters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

import fr.umontpellier.android_tp2_2.R;
import fr.umontpellier.android_tp2_2.models.Country;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private List<Country> countryList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Country country);
    }

    public CountryAdapter(List<Country> countryList, Context context, OnItemClickListener listener) {
        this.countryList = countryList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_item, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.bind(country, listener);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private ImageView flagImageView;
        private TextView regionTextView;
        private CircularProgressIndicator imageLoadingIndicator;

        public CountryViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.country_name);
            flagImageView = itemView.findViewById(R.id.country_flag);
            regionTextView = itemView.findViewById(R.id.country_region);
            imageLoadingIndicator = itemView.findViewById(R.id.image_loading_indicator);
        }

        public void bind(final Country country, final OnItemClickListener listener) {
            nameTextView.setText(country.getName().getCommon());
            regionTextView.setText(country.getRegion());

            imageLoadingIndicator.setVisibility(View.VISIBLE);

            // Utiliser Glide pour charger l'image du drapeau
            Glide.with(context)
                    .load(country.getFlags().getPng())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imageLoadingIndicator.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageLoadingIndicator.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(flagImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(country);
                }
            });
        }
    }
}
