package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import scenarios.KafkaExampleScenario;

import static io.gatling.javaapi.core.CoreDsl.*;

public class KafkaExample extends Simulation {

    ScenarioBuilder scenario = new KafkaExampleScenario().mainScenario();


    {
        setUp(scenario.injectClosed(constantConcurrentUsers(1).during(10)));
    }
}

