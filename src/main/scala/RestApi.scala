import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

class RestApi(system: ActorSystem, timeout: Timeout) extends Rest {
  implicit val requestTimeout = timeout
  implicit def executionContext = system.dispatcher

  def createRouteActor = system.actorOf(RouteActor.props, RouteActor.name)
}

trait Rest extends Api with EventMarshalling {
  import StatusCodes._

  def routes: Route = eventsRoute 

  def eventsRoute =
    pathPrefix("events") {
      pathEndOrSingleSlash {
        get {
          // GET /events
          onSuccess(getEvents()) { events => complete(events) }
        }
      }
    }
}

trait Api {
  import RouteActor._

  def createRouteActor(): ActorRef

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  lazy val routeActor = createRouteActor()

  def getEvents(): Future[Events] =
    routeActor.ask(GetEvents).mapTo[Events]
}
