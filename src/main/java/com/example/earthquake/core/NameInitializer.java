package com.example.earthquake.core;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class NameInitializer {
    private NameInitializer(){

    }
    protected static List<String> countryNames = new ArrayList<>();
    protected static List<String> usaStates = new ArrayList<>();

    @PostConstruct
    public static void initializeCountryNames(){
        String[] countryCodes = Locale.getISOCountries();

        for (String countryCode : countryCodes) {
            Locale obj = new Locale("en", countryCode);
            countryNames.add(obj.getDisplayCountry());
        }
    }

    public static List<String> getCountryNames() {
        return countryNames;
    }

    public static List<String> getUsaStates(){
        return usaStates;
    }

    @PostConstruct
    public static void initializeUsaStates(){
        usaStates.addAll(List.of("Alabama",
                "Alaska",
                "Arizona",
                "Arkansas",
                "California",
                "CA",
                "Colorado",
                "Connecticut",
                "Delaware",
                "Florida",
                "Georgia",
                "Hawaii",
                "Idaho",
                "Illinois",
                "Indiana",
                "Iowa",
                "Kansas",
                "Kentucky",
                "Louisiana",
                "Maine",
                "Maryland",
                "Massachusetts",
                "Michigan",
                "Minnesota",
                "Mississippi",
                "Missouri",
                "Montana",
                "Nebraska",
                "Nevada",
                "Hampshire",
                "Jersey",
                "York",
                "Carolina",
                "Dakota",
                "Ohio",
                "Oklahoma",
                "Oregon",
                "Pennsylvania",
                "Rhode",
                "Tennessee",
                "Texas",
                "Utah",
                "Vermont",
                "Virginia",
                "Washington",
                "Wisconsin",
                "Wyoming"));
    }
}
