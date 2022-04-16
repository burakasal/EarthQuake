package com.example.earthquake.model;

import lombok.Data;

@Data
public class EarthQuakeModelProperties {
    private String place;
    private double mag;
    private Long time;
}
