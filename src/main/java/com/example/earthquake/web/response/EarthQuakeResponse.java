package com.example.earthquake.web.response;

import com.example.earthquake.web.response.model.EarthQuakeResponseModel;
import lombok.Data;

import java.util.List;

@Data
public class EarthQuakeResponse {
    private List<EarthQuakeResponseModel> earthQuakeModels;
    private String message;
}
