package dev.mertkaanguzel.mediumclone;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

public class TestSupport {
    public static Instant getInstant() {
        Instant expectedInstant = Instant.parse("2023-10-23T19:23:00Z");
        Clock clock = Clock.fixed(expectedInstant, Clock.systemDefaultZone().getZone());

        return Instant.now(clock);
    }

    public static LocalDateTime getLocalDateTime() {
        return  LocalDateTime.ofInstant(getInstant(), Clock.systemDefaultZone().getZone());
    }
}
