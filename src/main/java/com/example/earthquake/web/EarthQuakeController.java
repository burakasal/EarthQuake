package com.example.earthquake.web;

import com.example.earthquake.core.NameInitializer;
import com.example.earthquake.service.EarthQuakeService;
import com.example.earthquake.web.response.EarthQuakeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/v1/earthquake")
public class EarthQuakeController {
    private final EarthQuakeService earthQuakeService;

    public EarthQuakeController(EarthQuakeService earthQuakeService){
        this.earthQuakeService = earthQuakeService;
    }

    @GetMapping("/getEarthquakeInfo")
    public ResponseEntity<EarthQuakeResponse> getEarthQuakeInfo(@RequestParam String country, @RequestParam int numberOfDays){
        if(numberOfDays<0){
            EarthQuakeResponse earthQuakeResponse = new EarthQuakeResponse();
            earthQuakeResponse.setMessage("Number of days can not be smaller than 0.");
            return new ResponseEntity<>(earthQuakeResponse, HttpStatus.OK);
        }

        country = organizeCountryInput(country);

        List<String> countryNames = NameInitializer.getCountryNames();
        if(!countryNames.contains(country)){
            EarthQuakeResponse earthQuakeResponse = new EarthQuakeResponse();
            earthQuakeResponse.setMessage("Country name is invalid. Make sure you write the full name. Example: United States");
            return new ResponseEntity<>(earthQuakeResponse, HttpStatus.OK);
        }
        EarthQuakeResponse earthQuakeInfoForGivenCountry = earthQuakeService.getEarthQuakeInfoForGivenCountry(country, numberOfDays, countryNames);
        return new ResponseEntity<>(earthQuakeInfoForGivenCountry, HttpStatus.OK);
    }

    public String organizeCountryInput(String country){
        StringBuilder returnedCountry = new StringBuilder();
        String[] splitCountry = country.toLowerCase(Locale.ROOT).split("\\s+");

        for (String str : splitCountry){
            returnedCountry.append(str.substring(0, 1).toUpperCase()).append(str.substring(1)).append(" ");
        }
        return returnedCountry.toString().trim();
    }
}
