package gatling.generic.plugin

import io.gatling.commons.stats.{KO, OK, Status}
import io.gatling.commons.util.Clock
import io.gatling.core.action.{Action, ExitableAction}
import io.gatling.core.stats.StatsEngine
import io.gatling.core.structure.ScenarioContext
import io.gatling.javaapi.core.Session

import java.util.function.Function
import scala.util.{Failure, Success, Try}

class GenericActionScala(val name: String,
                         val function: Function[Session, Session],
                         val ctx: ScenarioContext,
                         val next: Action) extends ExitableAction {

  override def statsEngine: StatsEngine = ctx.coreComponents.statsEngine

  override def clock: Clock = ctx.coreComponents.clock

  final override protected def execute(session: io.gatling.core.session.Session): Unit = {
    var startTimestamp = clock.nowMillis
    var message: Option[String] = None

    var javaapiSession = new io.gatling.javaapi.core.Session(session)

    val result = Try[Status] {
      startTimestamp = clock.nowMillis
      javaapiSession = function.apply(javaapiSession)
      OK
    } match {
      case Success(value) =>
        value
      case Failure(exception) =>
        message = Option(exception.getMessage)
        KO
    }

    var endTimestamp = clock.nowMillis

    if (javaapiSession.contains("latency")) {
      if (javaapiSession.contains("startTimestamp")) {
        startTimestamp = javaapiSession.getLong("startTimestamp")
        javaapiSession = javaapiSession.remove("startTimestamp")
      }

      endTimestamp = startTimestamp + javaapiSession.getLong("latency")
      javaapiSession = javaapiSession.remove("latency")
    }

    statsEngine.logResponse(
      session.scenario,
      session.groups,
      name,
      startTimestamp,
      endTimestamp,
      result,
      None,
      message)

    next ! javaapiSession.asScala().logGroupRequestTimings(startTimestamp, endTimestamp)
  }
}