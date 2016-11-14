package play

import akka.actor._

class EchoActor extends Actor {
  def receive = {
    case msg => sender() ! msg
  }
}
