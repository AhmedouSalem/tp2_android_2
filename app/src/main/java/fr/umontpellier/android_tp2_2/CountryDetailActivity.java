package fr.umontpellier.android_tp2_2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;
import java.util.Map;

import fr.umontpellier.android_tp2_2.api.CountryService;
import fr.umontpellier.android_tp2_2.models.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryDetailActivity extends AppCompatActivity {
    private TextView nameTextView, capitalTextView, regionTextView, subregionTextView,  detailsTextView, languagesTextView;
    private ImageView flagImageView;
    private CountryService countryService;
    private CircularProgressIndicator loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        nameTextView = findViewById(R.id.country_name);
        capitalTextView = findViewById(R.id.country_capital);
        regionTextView = findViewById(R.id.country_region);
        subregionTextView = findViewById(R.id.country_sub_region);
        flagImageView = findViewById(R.id.country_flag);
        detailsTextView = findViewById(R.id.country_details);
        languagesTextView = findViewById(R.id.country_language);
        loadingIndicator = findViewById(R.id.loading_indicator);

        MaterialToolbar toolbar = findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white));
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        String countryName = getIntent().getStringExtra("country_name");
        countryService = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryService.class);
//        String countryName = "Mauritania";

        // Afficher l'indicateur de chargement
        loadingIndicator.setVisibility(View.VISIBLE);

        fetchCountryDetails(countryName);
    }

    private void fetchCountryDetails(String countryName) {
        // Initialiser Retrofit (ou votre client API)
        countryService.getCountryDetails(countryName).enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Country country = response.body().get(0);

                    // Mettre à jour les vues avec les données du pays
                    nameTextView.setText(country.getName().getCommon());
                    capitalTextView.setText("Capital: " + (country.getCapital().isEmpty() ? "N/A" : country.getCapital().get(0)));
                    regionTextView.setText("Region: " + country.getRegion());
                    subregionTextView.setText("Subregion" + country.getSubregion());
                    detailsTextView.setText("Area: " + country.getArea() + " sq km\nPopulation: " + country.getPopulation());
                    Map<String, String> languages = country.getLanguages();
                    StringBuilder languagesText = new StringBuilder();

                    languagesText.append("Languages: ");
                    for (Map.Entry<String, String> entry : languages.entrySet()) {
                        languagesText.append(entry.getValue()).append(" - ");
                    }

                    languagesTextView.setText(languagesText.toString());

                    // Charger le drapeau avec Glide
                    Glide.with(CountryDetailActivity.this).load(country.getFlags().getPng()).into(flagImageView);

                    // Masquer l'indicateur de progression après le chargement
                    loadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("CountryDetailActivity", "Failed to fetch country details", t);
                // En cas d'échec, on peut aussi masquer l'indicateur de progression
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
