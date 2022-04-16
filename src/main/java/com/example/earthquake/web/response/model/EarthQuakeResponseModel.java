package com.example.earthquake.web.response.model;

import lombok.Data;

@Data
public class EarthQuakeResponseModel {
    private String country;
    private String placeOfEarthQuake;
    private double magnitude;
    private String date;
    private Long time;
}
