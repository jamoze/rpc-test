package net.jamosa.ixtens.test.server.services;

import java.util.Date;

public class DateTimeService implements IService {

    private final Date doomsdayTime = new Date(1356127200L);

    public Date getCurrentDateTime() {
        return new Date();
    }

    public long getDoomsdayLeft() {
        Date now = new Date();
        return (doomsdayTime.getTime() - now.getTime()) /1000;
    }

}
