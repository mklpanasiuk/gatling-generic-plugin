package gatling.generic.plugin

import io.gatling.commons.stats.{KO, OK, Status}
import io.gatling.commons.util.Clock
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.stats.StatsEngine
import io.gatling.core.structure.ScenarioContext
import io.gatling.javaapi.core.Session

import java.util.function.Function
import scala.util.{Failure, Success, Try}

class GenericAction(val name: String,
                    val function: Function[Session, Session],
                    val ctx: ScenarioContext,
                    val next: Action) extends ExitableAction {

  override def statsEngine: StatsEngine = ctx.coreComponents.statsEngine

  override def clock: Clock = ctx.coreComponents.clock

  final override protected def execute(session: io.gatling.core.session.Session): Unit = {
    var startTimestamp = clock.nowMillis
    var endTimestamp = clock.nowMillis
    var responseCode = Option("200")
    var message: Option[String] = None

    var javaapiSession = new io.gatling.javaapi.core.Session(session)

    val result = Try[Status] {
      startTimestamp = clock.nowMillis
      javaapiSession = function.apply(javaapiSession)
      OK
    } match {
      case Success(value) =>
        endTimestamp = clock.nowMillis
        value
      case Failure(exception) =>
        endTimestamp = clock.nowMillis
        responseCode = Option("404")
        message = Option(exception.getMessage)
        KO
    }

    if (javaapiSession.contains("latency")) {
      if (javaapiSession.contains("startTimestamp")) {
        startTimestamp = javaapiSession.getString("startTimestamp").toLong
        javaapiSession = javaapiSession.remove("startTimestamp")
      }

      endTimestamp = startTimestamp + javaapiSession.getString("latency").toLong
      javaapiSession = javaapiSession.remove("latency")
    }

    javaapiSession = new io.gatling.javaapi.core.Session(
      javaapiSession.asScala().logGroupRequestTimings(startTimestamp, endTimestamp))

    statsEngine.logResponse(
      session.scenario,
      session.groups,
      name,
      startTimestamp,
      endTimestamp,
      result,
      None,
      message)

    next ! javaapiSession.asScala()
  }
}