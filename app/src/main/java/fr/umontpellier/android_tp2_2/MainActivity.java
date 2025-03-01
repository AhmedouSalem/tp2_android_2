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
import fr.umontpellier.android_tp2_2.fragments.CountryDetailFragment;
import fr.umontpellier.android_tp2_2.fragments.CountryListFragment;
import fr.umontpellier.android_tp2_2.models.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements CountryListFragment.OnCountrySelectedListener {
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vérifier si l'écran est une tablette
        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            // Charger les deux fragments pour les tablettes
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CountryListFragment())
                    .replace(R.id.fragment_container_detail, new CountryDetailFragment())
                    .commit();
        } else {
            // Utiliser un seul fragment pour les petits écrans
            CountryListFragment countryListFragment = new CountryListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, countryListFragment)
                    .commit();
        }
    }

    @Override
    public void onCountrySelected(Country country) {
        if (isTablet) {
            // Passer le nom du pays uniquement, et créer le fragment après la sélection
            CountryDetailFragment detailFragment = CountryDetailFragment.newInstance(country.getName().getCommon());

            // Remplacer le fragment de détails dans le conteneur
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_detail, detailFragment)
                    .commit();
        } else {
            // Pour les petits écrans, utiliser l'activité de détails comme avant
            Intent intent = new Intent(MainActivity.this, CountryDetailActivity.class);
            intent.putExtra("country_name", country.getName().getCommon());
            startActivity(intent);
        }
    }
}
