package scenarios;

import io.gatling.javaapi.core.ScenarioBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

import static gatling.generic.plugin.GenericDsl.genericAction;
import static io.gatling.javaapi.core.CoreDsl.scenario;


public class KafkaExampleScenario {


    public ScenarioBuilder mainScenario() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        var producer = new KafkaProducer<String, String>(props);
        var topic = "my-topic";

        return scenario("Kafka Example")
                .exec(
                        genericAction("message_1", session -> {
                            producer.send(new ProducerRecord<>(topic, "key-my-1", "value-my-1"));

                            return session;
                        }))
                .exec(
                        genericAction("message_2", session -> {
                            producer.send(new ProducerRecord<>(topic, "key-my-2", "value-my-2"));

                            return session;
                        }))
                .pause(1);

    }
}

