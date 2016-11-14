package play

import akka.testkit.{ CallingThreadDispatcher, EventFilter, TestKit, ImplicitSender }
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.scalatest.WordSpecLike
import GreeterActorTest._

class EchoActorTest extends TestKit(ActorSystem("testsystem")) with WordSpecLike with ImplicitSender with StopSystemAfterAll {
  "Reply with the same message it receives without ask" in {
    val echo = system.actorOf(Props[EchoActor], "echo2")
    echo ! "some message"
    expectMsg("some message")
  }
}
