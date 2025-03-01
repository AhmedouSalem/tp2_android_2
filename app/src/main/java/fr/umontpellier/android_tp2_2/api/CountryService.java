package fr.umontpellier.android_tp2_2.api;

import java.util.List;

import fr.umontpellier.android_tp2_2.models.Country;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CountryService {
    @GET("v3.1/all?fields=name,flags")
    Call<List<Country>> getCountries();  // Récupérer la liste des pays

    @GET("v3.1/name/{name}?fullText=true")
    Call<List<Country>> getCountryDetails(@Path("name") String name);  // Récupérer les détails d'un pays
}
