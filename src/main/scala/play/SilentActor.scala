package play

import akka.actor._

class SilentActor extends Actor {
  def receive = {
    case msg =>
  }
}
