package simulations;

import dto.User;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static gatling.generic.plugin.GenericDsl.genericAction;
import static gatling.generic.plugin.GenericDsl.genericProtocol;
import static io.gatling.javaapi.core.CoreDsl.*;

public class SimpleExample extends Simulation {

    ScenarioBuilder scenario = scenario("FirstScenario")
        .exec(
            genericAction("step_1", session -> {
                var user = new User();
                user.id = User.provideId();
                user.name = "Mykola";

                session = session.set("payload_1", user);

                EmulateWork(111);

                return session;
            }),
            genericAction("step_2", session -> {
                var payload = (User) session.get("payload_1");
                System.out.println("===> " + payload.id);
                System.out.println("===> " + payload.name);

                EmulateWork(222);

                session = session.set("latency", 555);

                return session;
            }),
            pause(1)
        );

    {
        setUp(
            scenario.injectClosed(constantConcurrentUsers(1).during(10))
        ).protocols(genericProtocol());
    }

    private void EmulateWork(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

