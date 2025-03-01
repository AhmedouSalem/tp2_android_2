package fr.umontpellier.android_tp2_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import fr.umontpellier.android_tp2_2.adepters.CountryAdapter;
import fr.umontpellier.android_tp2_2.api.CountryService;
import fr.umontpellier.android_tp2_2.models.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CountryAdapter adapter;
    private List<Country> countryList = new ArrayList<>();
    private CountryService countryService;
    private CircularProgressIndicator loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadingIndicator = findViewById(R.id.loading_indicator);

        // Initialiser Retrofit
        countryService = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryService.class);

        loadingIndicator.setVisibility(View.VISIBLE);

        fetchCountries();

        // Configurer l'adaptateur avec un écouteur pour gérer les clics sur les éléments
        adapter = new CountryAdapter(countryList, this, new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Country country) {
                // Passer au détail du pays avec un Intent
                Intent intent = new Intent(MainActivity.this, CountryDetailActivity.class);
                intent.putExtra("country_name", country.getName().getCommon());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void fetchCountries() {
        countryService.getCountries().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    countryList.clear();
                    countryList.addAll(response.body());
                    countryList.sort(null);
                    adapter.notifyDataSetChanged();

                    // Masquer l'indicateur de progression une fois les données chargées
                    loadingIndicator.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("MainActivity", "Failed to fetch countries", t);
                // Masquer l'indicateur de progression si l'appel échoue
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }
}

