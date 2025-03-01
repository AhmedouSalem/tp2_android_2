package fr.umontpellier.android_tp2_2.models;

import java.util.List;
import java.util.Map;

public class Country implements Comparable<Country>{
    private CountryName name;
    private Flags flags;
    private List<String> capital;
    private String region;
    private String subregion;
    private String area;
    private String population;
    private List<String> borders;
    private Map<String, String> languages;


    public Country(CountryName name, Flags flags, List<String> capital, String region, String subregion, String area, String population, List<String> borders, Map<String, String> languages) {
        this.name = name;
        this.flags = flags;
        this.capital = capital;
        this.region = region;
        this.subregion = subregion;
        this.area = area;
        this.population = population;
        this.borders = borders;
        this.languages = languages;
    }

    public CountryName getName() {
        return name;
    }

    public void setName(CountryName name) {
        this.name = name;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public List<String> getCapital() {
        return capital;
    }

    public void setCapital(List<String> capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public List<String> getBorders() {
        return borders;
    }

    public void setBorders(List<String> borders) {
        this.borders = borders;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    @Override
    public int compareTo(Country otherCountry) {
        return this.name.getCommon().compareTo(otherCountry.getName().getCommon());
    }
}
