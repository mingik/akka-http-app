package play

import akka.testkit.{ CallingThreadDispatcher, EventFilter, TestKit }
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.scalatest.WordSpecLike
import GreeterActorTest._

class GreeterActorTest extends TestKit(testSystem) with WordSpecLike with StopSystemAfterAll {
  "The Greeter Actor" must {
    "say Hello World! when a Greeting('World') is sent to it" in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props[Greeter].withDispatcher(dispatcherId)
      val greeter = system.actorOf(props)
      EventFilter.info(message = "Hello World!", occurrences = 1).intercept {
        greeter ! Greeting("World")
      }
    }
  }

  "The Greeter2 Actor" must {
    "say Hello World! when a Greeting('World') is sent to it" in {
      val props = Greeter2.props(Some(testActor))
      val greeter = system.actorOf(props, "greeter02-1")
      greeter ! Greeting("World")
      expectMsg("Hello World!")
    }
    "say somethign else and see what happens" in {
      val props = Greeter2.props(Some(testActor))
      val greeter = system.actorOf(props, "greeter02-2")
      system.eventStream.subscribe(testActor, classOf[UnhandledMessage])
      greeter ! "World"
      expectMsg(UnhandledMessage("World", system.deadLetters, greeter))
    }
  }
}

object GreeterActorTest {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
         akka.loggers = [akka.testkit.TestEventListener]
      """)
    ActorSystem("testsystem", config)
  }
}
