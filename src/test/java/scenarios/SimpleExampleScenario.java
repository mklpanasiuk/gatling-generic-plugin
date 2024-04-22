package scenarios;

import dto.User;
import io.gatling.javaapi.core.ScenarioBuilder;

import static gatling.generic.plugin.GenericDsl.genericAction;
import static io.gatling.javaapi.core.CoreDsl.scenario;

public class SimpleExampleScenario {

    public ScenarioBuilder mainScenario() {
        return scenario("FirstScenario")
                .exec(
                        genericAction("step_1", session -> {
                            var user = new User();
                            user.id = User.provideId();
                            user.name = "Mykola";

                            session = session.set("payload_1", user);

                            EmulateWork(111);

                            return session;
                        }))
                .exec(
                        genericAction("step_2", session -> {
                            var payload = (User) session.get("payload_1");
                            System.out.println("===> " + payload.id);
                            System.out.println("===> " + payload.name);

                            EmulateWork(222);

                            session = session.set("latency", 555);

                            return session;
                        }))
                .pause(1);
    }

    private void EmulateWork(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


