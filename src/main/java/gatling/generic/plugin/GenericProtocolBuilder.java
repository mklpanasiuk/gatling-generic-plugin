package gatling.generic.plugin;

import io.gatling.core.protocol.Protocol;
import io.gatling.javaapi.core.ProtocolBuilder;

public class GenericProtocolBuilder implements ProtocolBuilder {
    @Override
    public Protocol protocol() {
        return new GenericProtocol();
    }
}
