package actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import org.scalatest.FunSuite

// very simple
class ActorAA extends Actor {
  override def receive: Receive = {
    case "ping" => println("asdf")
  }
}

// actor with argument constructor
class ActorAB(val animal: String) extends Actor {
  override def receive: Receive = {
    case "ping" => println("asdf")
  }
}


class CreateActorSpec extends FunSuite {
  test("Create an actor system") {
    val actorSystem = ActorSystem("MyActorSystem")

    assert(actorSystem.isInstanceOf[ActorSystem])
    assert(actorSystem.name === "MyActorSystem")
    assert(actorSystem.toString === "akka://MyActorSystem")
  }

  test("Create an actor in an actor system") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorAA = actorSystem.actorOf(Props[ActorAA])

    assert(actorAA.isInstanceOf[ActorRef])
    assert(actorAA.path.toString === "akka://MyActorSystem/user/$a")
  }

  test("Create a named actor") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorAA = actorSystem.actorOf(Props[ActorAA], "named_actor")

    assert(actorAA.path.toString === "akka://MyActorSystem/user/named_actor")
  }

  test("I can create an actor with arguments constructor") {
    val actorSystem = ActorSystem("MyActorSystem")
    val actorAB = actorSystem.actorOf(Props(classOf[ActorAB], "elephant"), "ponger")

    assert(actorAB.isInstanceOf[ActorRef])
  }
}