package net.jamosa.ixtens.test.server.services.impl;

import net.jamosa.ixtens.test.server.services.IService;

import java.util.Date;
import java.util.Random;

public class WeatherService implements IService {

    private final static Random RND = new Random();

    public String getWeather() {
        return "The weather on " + new Date() + " is " + (-40 + RND.nextInt(40) + " degrees");
    }

}
