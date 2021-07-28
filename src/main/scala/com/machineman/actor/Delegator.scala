package com.machineman.actor

import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.Behaviors
import com.machineman.commands.DataBehavior

object Delegator {

  trait DelegatorBehaviour
  case class Data(msg : String) extends DelegatorBehaviour

  def delegator(actor : ActorRef[DataBehavior], msg : String) = Behaviors.setup[DelegatorBehaviour] { ctx =>
//    actor ! ProcessingActor.Data(msg)
    Behaviors.receive { (_, msg) =>
      msg match {
        case Data(msg) =>
//          actor ! ProcessingActor.Data(msg)
          Behaviors.same
      }
    }
  }
}
