package me.yejun.famabot.util;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    public String[] getDatesInCST() {
        ZoneId cstZone = ZoneId.of("America/Chicago");
        LocalDate today = LocalDate.now(ZoneId.of("UTC")).atStartOfDay(cstZone).toLocalDate();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return new String[]{today.format(formatter), yesterday.format(formatter)};
    }
}
