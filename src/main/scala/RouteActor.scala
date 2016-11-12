
import scala.concurrent.duration._
import scala.concurrent.Future

import akka.actor._
import akka.util.Timeout

object RouteActor {
  def props(implicit timeout: Timeout) = Props(new RouteActor)
  def name = "routeActor"

  case class Event(id: Long, name: String)
  case class GetEvent(name: String)
  case object GetEvents
  case class Events(events: Vector[Event])
}

class RouteActor(implicit timeout: Timeout) extends Actor {
  import RouteActor._
  import context._

  val events: Vector[Event] = Vector(Event(1L, "event1"), Event(2L, "event2"))

  def receive = {
    case GetEvents =>
      def getEvents =
        Events(events)

      sender() ! getEvents
  }
}
