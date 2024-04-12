package simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static gatling.generic.plugin.GenericDsl.genericAction;
import static gatling.generic.plugin.GenericDsl.genericProtocol;
import static io.gatling.javaapi.core.CoreDsl.*;

public class KafkaExample extends Simulation {

    private ScenarioBuilder buildScenario(String name) {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        var producer = new KafkaProducer<String, String>(props);
        var topic = "my-topic";

        var scenario = scenario(name)
            .exec(
                genericAction("message_1", session -> {
                    producer.send(new ProducerRecord<>(topic, "key-my-1", "value-my-1"));

                    return session;
                }),
                genericAction("message_2", session -> {
                    producer.send(new ProducerRecord<>(topic, "key-my-2", "value-my-2"));

                    return session;
                }),
                pause(1)
            );

        return scenario;
    }

    {
        setUp(
            buildScenario("FirstScenario").injectClosed(constantConcurrentUsers(1).during(10))
        ).protocols(genericProtocol());
    }
}

