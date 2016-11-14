package play

import akka.actor._

case class Greeting(message: String)

class Greeter extends Actor with ActorLogging {
  def receive = {
    case Greeting(message) => log.info("Hello {}!", message)
  }
}

class Greeter2(listener: Option[ActorRef]) extends Actor with ActorLogging {
  def receive = {
    case Greeting(who) =>
    val message = "Hello " + who + "!"
    log.info(message)
    listener.foreach(_ ! message)
  }
}

object Greeter2 {
  def props(listener: Option[ActorRef] = None) =
    Props(new Greeter2(listener))
}
