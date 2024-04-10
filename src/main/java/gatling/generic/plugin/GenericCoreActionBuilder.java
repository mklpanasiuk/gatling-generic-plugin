package gatling.generic.plugin;

import io.gatling.core.action.Action;
import io.gatling.core.structure.ChainBuilder;
import io.gatling.core.structure.ScenarioContext;
import io.gatling.javaapi.core.Session;

import java.util.function.Function;

public class GenericCoreActionBuilder implements io.gatling.core.action.builder.ActionBuilder {
    private final String name;
    private final Function<Session, Session> callback;

    public GenericCoreActionBuilder(String name, Function<Session, Session> callback) {
        this.name = name;
        this.callback = callback;
    }

    @Override
    public Action build(ScenarioContext ctx, Action next) {
        return new GenericAction(name, callback, ctx, next);
    }

    @Override
    public ChainBuilder toChainBuilder() {
        return io.gatling.core.action.builder.ActionBuilder.super.toChainBuilder();
    }
}
