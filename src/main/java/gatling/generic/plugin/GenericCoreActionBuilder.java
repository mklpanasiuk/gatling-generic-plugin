package gatling.generic.plugin;

import io.gatling.core.action.Action;
import io.gatling.core.action.builder.ActionBuilder;
import io.gatling.core.structure.ChainBuilder;
import io.gatling.core.structure.ScenarioContext;
import io.gatling.javaapi.core.Session;

import java.util.function.Function;

public class GenericCoreActionBuilder implements ActionBuilder {
    private final String name;
    private final Function<Session, Session> function;

    public GenericCoreActionBuilder(String name, Function<Session, Session> function) {
        this.name = name;
        this.function = function;
    }

    @Override
    public Action build(ScenarioContext ctx, Action next) {
        return new GenericActionScala(name, function, ctx, next);
    }

    @Override
    public ChainBuilder toChainBuilder() {
        return ActionBuilder.super.toChainBuilder();
    }
}
