package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import scenarios.SimpleExampleScenario;

import static io.gatling.javaapi.core.CoreDsl.*;

public class SimpleExample extends Simulation {

    ScenarioBuilder scenario = new SimpleExampleScenario().mainScenario();

    {
        setUp(scenario.injectClosed(constantConcurrentUsers(1).during(10)));
    }
}

