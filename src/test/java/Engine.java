import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import simulations.SimpleExample;

public class Engine {

    public static void main(String[] args) {
        GatlingPropertiesBuilder props = new GatlingPropertiesBuilder()
            .resourcesDirectory(IDEPathHelper.mavenResourcesDirectory.toString())
            .resultsDirectory(IDEPathHelper.resultsDirectory.toString())
            .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString())
            .simulationClass(SimpleExample.class.getName());

        Gatling.fromMap(props.build());

        System.exit(0);
    }
}