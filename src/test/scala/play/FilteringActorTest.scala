package play

import org.scalatest.{WordSpecLike, MustMatchers}
import akka.actor._
import akka.testkit.{TestKit, TestActorRef}
import scala.util.Random

class FilteringActorTest extends TestKit(ActorSystem("testsystem")) with WordSpecLike with MustMatchers with StopSystemAfterAll {
  "A Filtering Actor" must {
    "filter out particular messages" in {
      import FilteringActor._

      val props = FilteringActor.props(testActor, 5)
      val filter = system.actorOf(props, "filter-1")

      filter ! Event(1)
      filter ! Event(2)
      filter ! Event(1)
      filter ! Event(3)
      filter ! Event(1)
      filter ! Event(4)
      filter ! Event(5)
      filter ! Event(5)
      filter ! Event(6)

      val eventIds = receiveWhile() {
        case Event(id) if id <= 5 => id
      }
      eventIds must be(List(1,2,3,4,5))
      expectMsg(Event(6))
    }
  }
}
