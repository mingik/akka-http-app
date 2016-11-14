package play

import akka.actor._

object SendingActor {
  def props(receiver: ActorRef) = Props(new SendingActor(receiver))

  case class Event(id: Long)
  case class SortEvents(unsorted: Vector[Event])
  case class SortedEvents(sorte: Vector[Event])
}

class SendingActor(receiver: ActorRef) extends Actor {
  import SendingActor._

  def receive = {
    case SortedEvents(unsorted) =>
      receiver ! SortedEvents(unsorted.sortBy(_.id))
  }
}
