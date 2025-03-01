package fr.umontpellier.android_tp2_2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.umontpellier.android_tp2_2.R;
import fr.umontpellier.android_tp2_2.adepters.CountryAdapter;
import fr.umontpellier.android_tp2_2.api.CountryService;
import fr.umontpellier.android_tp2_2.models.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryListFragment extends Fragment {
    private RecyclerView recyclerView;
    private CountryAdapter adapter;
    private List<Country> countryList = new ArrayList<>();
    private CountryService countryService;
    private OnCountrySelectedListener listener;

    public interface OnCountrySelectedListener {
        void onCountrySelected(Country country);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_country_list, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        countryService = new Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryService.class);

        fetchCountries();

        adapter = new CountryAdapter(countryList, getContext(), new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Country country) {
                if (listener != null) {
                    listener.onCountrySelected(country);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        return rootView;
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
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                // Gestion des erreurs
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCountrySelectedListener) {
            listener = (OnCountrySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCountrySelectedListener");
        }
    }
}
