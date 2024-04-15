package gatling.generic.plugin;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import java.util.function.Function;

public final class GenericDsl {

    public static ChainBuilder genericAction(String name, Function<Session, Session> function) {
        return new GenericActionBuilder(name, function).toChainBuilder();
    }
}
