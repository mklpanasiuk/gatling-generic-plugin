package dto;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static final AtomicInteger iterator = new AtomicInteger(0);
    private static final long initialId = Instant.now().toEpochMilli();
    public long id;
    public String name;

    public static long provideId() {
        return initialId + iterator.addAndGet(1);
    }
}
