package play

import akka.actor._

class SilentActor extends Actor {
  import SilentActor._
  var internalState = Vector[String]()

  def receive = {
    case SilentMessage(data) =>
      internalState = internalState :+ data
    case GetState(receiver) =>
      receiver ! internalState
  }

  def state = internalState
}

object SilentActor {
  case class SilentMessage(data: String)
  case class GetState(receiver: ActorRef)
}

