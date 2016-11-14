package play

import akka.actor._

object FilteringActor {
  def props(nextActor: ActorRef, bufferSize: Int) = 
    Props(new FilteringActor(nextActor, bufferSize))
  case class Event(id: Long)
}

class FilteringActor(nextActor: ActorRef, bufferSize: Int) extends Actor {
  import FilteringActor._
  var lastMessages = Vector[Event]()

  def receive = {
    case msg: Event =>
      if (!lastMessages.contains(msg)) {
        lastMessages :+= msg
        nextActor ! msg
        if (lastMessages.size > bufferSize) {
          // discard the oldest
          lastMessages = lastMessages.tail
        }
      }
  }
}
