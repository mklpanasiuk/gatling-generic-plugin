package gatling.generic.plugin;

import gatling.generic.plugin.GenericCoreActionBuilder;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Session;

import java.util.function.Function;

public class GenericActionBuilder implements ActionBuilder {
    private final io.gatling.core.action.builder.ActionBuilder wrapper;

    public GenericActionBuilder(String name, Function<Session, Session> function) {
        wrapper = new GenericCoreActionBuilder(name, function);
    }

    @Override
    public io.gatling.core.action.builder.ActionBuilder asScala() {
        return wrapper;
    }

    @Override
    public ChainBuilder toChainBuilder() {
        return ActionBuilder.super.toChainBuilder();
    }
}
