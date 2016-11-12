import spray.json._

case class Error(message: String)

trait EventMarshalling extends DefaultJsonProtocol {
  import RouteActor._

  implicit val eventFormat = jsonFormat2(Event)
  implicit val eventsFormat = jsonFormat1(Events)
  implicit val errorFormat = jsonFormat1(Error)
}
