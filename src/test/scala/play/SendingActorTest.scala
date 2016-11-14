package play

import org.scalatest.{WordSpecLike, MustMatchers}
import akka.actor._
import akka.testkit.{TestKit, TestActorRef}
import scala.util.Random

class SendingActorTest extends TestKit(ActorSystem("testsystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {
  "A Sending Actor" must {
    "send a message to another actor when it has finished processing" in {
      import SendingActor._
      val props = SendingActor.props(testActor)
      val sendingActor = system.actorOf(props, "sendingActor")

      val size = 10
      val maxInclusive = 100

      def randomEvents() = (0 until size).map { _ =>
        Event(Random.nextInt(maxInclusive))
      }.toVector

      val unsorted = randomEvents()
      val sortEvents = SortedEvents(unsorted)
      sendingActor ! sortEvents

      expectMsgPF() {
        case SortedEvents(events) =>
          events.size must be(size)
          unsorted.sortBy(_.id) must be(events)
      }
    }
  }
}
