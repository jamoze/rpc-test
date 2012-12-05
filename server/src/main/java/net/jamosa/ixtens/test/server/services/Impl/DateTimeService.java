package net.jamosa.ixtens.test.server.services.impl;

import net.jamosa.ixtens.test.server.services.IService;

import java.util.Date;

public class DateTimeService implements IService {

    private Date doomsdayTime = new Date(1356022800000L);

    public Date getCurrentDateTime() {
        return new Date();
    }

    public String getDoomsdayLeft() {
        Date now = new Date();
        long millis = doomsdayTime.getTime() - now.getTime();
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return  "Time left: " + days + " days or " + hours + " hours, or " + minutes + " minutes, or " + seconds + " seconds";
    }

    public void foo(Integer moo) {
        moo++;
    }
}
