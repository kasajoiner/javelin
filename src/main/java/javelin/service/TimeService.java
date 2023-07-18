package javelin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TimeService {

    @Value("${shift.start}")
    private int startHour;
    @Value("${shift.end}")
    private int endHour;
    @Value("${app.tz}")
    private String tz;

    public LocalDate today() {
        return LocalDate.now(ZoneId.of(tz));
    }

    public LocalDateTime shiftStart() {
        return today().atTime(startHour, 0);
    }

    public LocalDateTime shiftEnd() {
        return today().atTime(endHour, 0);
    }

    public boolean isOpenNow() {
        var now = LocalDateTime.now(ZoneId.of(tz));
        return now.isAfter(shiftStart()) && now.isBefore(shiftEnd());
    }
}
