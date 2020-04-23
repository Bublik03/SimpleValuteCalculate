package com.example.valcalc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JSONtoValute {
    public Rest getValute(String json){
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();

        Gson gson = new Gson();
        Rest valute = gson.fromJson(json, Rest.class);
        return valute;
    }
}