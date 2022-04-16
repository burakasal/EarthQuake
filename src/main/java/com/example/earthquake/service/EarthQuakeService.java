package com.example.earthquake.service;

import com.example.earthquake.config.WebClientConfig;
import com.example.earthquake.core.NameInitializer;
import com.example.earthquake.model.EarthQuakeModel;
import com.example.earthquake.model.EarthQuakeModelFeatures;
import com.example.earthquake.model.EarthQuakeModelProperties;
import com.example.earthquake.web.response.EarthQuakeResponse;
import com.example.earthquake.web.response.model.EarthQuakeResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EarthQuakeService {

    private final WebClientConfig webClientConfig;

    public EarthQuakeService(WebClientConfig webClientConfig){
        this.webClientConfig = webClientConfig;
    }
    @Value("${earthquake.api.url}")
    private String earthQuakeUrl;

    public EarthQuakeResponse getEarthQuakeInfoForGivenCountry(String country, int numberOfDays, List<String> countryNames) {
        String query = EarthQuakeService.createQuery(numberOfDays);
        String url = earthQuakeUrl + query;

        Mono<EarthQuakeModel> earthQuakeMono = webClientConfig.webClient().get()
                .uri(url)
                .retrieve()
                .bodyToMono(EarthQuakeModel.class);

        EarthQuakeModel earthQuakeModel = earthQuakeMono.share().block();
        EarthQuakeModelFeatures[] features = Objects.requireNonNull(earthQuakeModel).getFeatures();

        return prepareEarthQuakeResponse(features, country, numberOfDays, countryNames);
    }

    private EarthQuakeResponse prepareEarthQuakeResponse(EarthQuakeModelFeatures[] features, String country, int numberOfDays, List<String> countryNames) {
        List<String> usaStates = NameInitializer.getUsaStates();
        EarthQuakeResponse earthQuakeResponse = new EarthQuakeResponse();
        List<EarthQuakeResponseModel> earthQuakeResponseModels = new ArrayList<>();

        if (country.equals("United States")) {
            iterateEarthQuakeFeatures(features, country, usaStates, earthQuakeResponseModels);
        }
        else {
            iterateEarthQuakeFeatures(features, country, countryNames, earthQuakeResponseModels);
        }

        if(earthQuakeResponseModels.isEmpty()){
            earthQuakeResponse.setMessage("No Earthquakes were recorded in the past " + numberOfDays +" days.");
        }
        else {
            earthQuakeResponse.setMessage("OK");
        }
        earthQuakeResponse.setEarthQuakeModels(earthQuakeResponseModels);
        return earthQuakeResponse;
    }

    private void iterateEarthQuakeFeatures(EarthQuakeModelFeatures[] features, String country, List<String> countryNames, List<EarthQuakeResponseModel> earthQuakeResponseModels) {
        if(!country.equals("United States") && countryNames.indexOf(country)!=1){
            String countryName = countryNames.get(countryNames.indexOf(country));
            countryNames = new ArrayList<>(Collections.singleton(countryName));
        }

        for (EarthQuakeModelFeatures feature : features) {
            EarthQuakeModelProperties properties = feature.getProperties();
            EarthQuakeResponseModel earthQuakeResponseModel = new EarthQuakeResponseModel();
            String place = properties.getPlace();
            if(place==null){continue;}
            if(countryNames.stream().anyMatch(place::contains)){
                earthQuakeResponseModel = setEarthQuakeResponseModelFields(properties, earthQuakeResponseModel, country);
            }
            if(earthQuakeResponseModel.getPlaceOfEarthQuake()!=null){
                earthQuakeResponseModels.add(earthQuakeResponseModel);
            }
        }
    }

    public EarthQuakeResponseModel setEarthQuakeResponseModelFields(EarthQuakeModelProperties properties, EarthQuakeResponseModel earthQuakeResponseModel, String country){
        String place = properties.getPlace();
        double magnitude = properties.getMag();
        Long time = properties.getTime();

        earthQuakeResponseModel.setPlaceOfEarthQuake(place);
        earthQuakeResponseModel.setCountry(country);

        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String myDate = format.format(date);
        earthQuakeResponseModel.setDate(myDate);
        earthQuakeResponseModel.setMagnitude(magnitude);
        earthQuakeResponseModel.setTime(time);
        return earthQuakeResponseModel;
    }
    private static String createQuery (int numberOfDays){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate today = LocalDate.now();
        LocalDate start = LocalDate.now().minusDays(numberOfDays);
        String todayDate = dtf.format(today);
        String startDate = dtf.format(start);

        if(startDate.equals(todayDate))
            return "query?format=geojson&starttime=" + startDate;
        else
            return "query?format=geojson&starttime=" + startDate + "&" + "endtime=" + todayDate;
    }
}
