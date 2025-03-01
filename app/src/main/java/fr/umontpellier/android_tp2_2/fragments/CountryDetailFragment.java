package fr.umontpellier.android_tp2_2.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;
import java.util.Map;

import fr.umontpellier.android_tp2_2.R;
import fr.umontpellier.android_tp2_2.api.CountryService;
import fr.umontpellier.android_tp2_2.models.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryDetailFragment extends Fragment {
    private TextView nameTextView, capitalTextView, regionTextView, subregionTextView, detailsTextView, languagesTextView;
    private ImageView flagImageView;
    private CircularProgressIndicator loadingIndicator;

    public static CountryDetailFragment newInstance(String countryName) {
        CountryDetailFragment fragment = new CountryDetailFragment();
        Bundle args = new Bundle();
        args.putString("country_name", countryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_country_detail, container, false);

        // Initialisation des vues
        nameTextView = rootView.findViewById(R.id.country_name);
        capitalTextView = rootView.findViewById(R.id.country_capital);
        regionTextView = rootView.findViewById(R.id.country_region);
        subregionTextView = rootView.findViewById(R.id.country_sub_region);
        flagImageView = rootView.findViewById(R.id.country_flag);
        detailsTextView = rootView.findViewById(R.id.country_details);
        languagesTextView = rootView.findViewById(R.id.country_language);
        loadingIndicator = rootView.findViewById(R.id.loading_indicator);

        // Vérification que les arguments existent
        if (getArguments() != null) {
            String countryName = getArguments().getString("country_name");

            if (countryName != null) {
                // Afficher l'indicateur de chargement
                loadingIndicator.setVisibility(View.VISIBLE);

                // Appeler l'API pour récupérer les détails du pays
                fetchCountryDetails(countryName);
            } else {
                Log.e("CountryDetailFragment", "Country name is null");
            }
        }

        return rootView;
    }

    private void fetchCountryDetails(String countryName) {
        CountryService countryService = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryService.class);

        countryService.getCountryDetails(countryName).enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Country country = response.body().get(0);

                    // Mettre à jour les vues avec les détails du pays
                    nameTextView.setText(country.getName().getCommon());
                    capitalTextView.setText("Capital: " + (country.getCapital().isEmpty() ? "N/A" : country.getCapital().get(0)));
                    regionTextView.setText("Region: " + country.getRegion());
                    subregionTextView.setText("Subregion: " + country.getSubregion());
                    detailsTextView.setText("Area: " + country.getArea() + " sq km\nPopulation: " + country.getPopulation());

                    Map<String, String> languages = country.getLanguages();
                    StringBuilder languagesText = new StringBuilder();
                    languagesText.append("Languages: ");
                    for (Map.Entry<String, String> entry : languages.entrySet()) {
                        languagesText.append(entry.getValue()).append(" - ");
                    }
                    languagesTextView.setText(languagesText.toString());

                    // Charger le drapeau du pays
                    Glide.with(getContext()).load(country.getFlags().getPng()).into(flagImageView);

                    // Masquer l'indicateur de chargement
                    loadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("CountryDetailFragment", "Failed to fetch country details", t);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }
}


