package com.machineman.actor

import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.Receptionist.Listing
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import com.machineman.commands.{Data, DataBehavior}
import com.machineman.util.LoggingActor
import com.machineman.util.LoggingActor.Error

object Supervisor {

  trait Command

  case class AddProcessor(listing: Receptionist.Listing) extends Command

  case class Message(data: String, entityID: String) extends Command

  case object Replied extends Command

  def apply(): Behavior[Command] = supervisor

  //  val ProcessingActorServiceKey = ServiceKey[DataBehavior](ServiceProvider.serviceProvider)

  val supervisor: Behavior[Command] = Behaviors.setup[Command] { context =>
    context.setLoggerName(this.getClass)
    val listingAdapter: ActorRef[Receptionist.Listing] = context.messageAdapter { listing =>
      AddProcessor(listing)
    }
    val logger = context.spawn(LoggingActor(), "Logger")
    logger ! Error("Setting up sharding")
    val sharding = ClusterSharding(context.system)
    //    val entityID = ConfigFactory.load("application.conf").getString("entityId")
    //    val messageTo = ConfigFactory.load("application.conf").getString("messageTo")
    val processorTypeKey = EntityTypeKey[DataBehavior]("ProcessingActorA")
    //    val messageExtractor =
    //      new HashCodeNoEnvelopeMessageExtractor[DataBehavior](numberOfShards = 30) {
    //        override def entityId(message: DataBehavior): String = entityID
    //      }

//    val shardRegion: ActorRef[ShardingEnvelope[DataBehavior]] =


    sharding.init(
      Entity(processorTypeKey) { context => {
        println(s"--2---Entity ID is ${context.entityId}")
        ProcessingActor(context.entityId)
      }

      }
      //          .withMessageExtractor(messageExtractor)
    )

    //    val processorRef = sharding.entityRefFor(processorTypeKey, messageTo)
    //    context.system.receptionist ! Receptionist.Subscribe(ProcessingActorServiceKey, listingAdapter)
    def handleRequest(listing: Set[Listing]): Behavior[Command] =
      Behaviors.receiveMessage {
        case AddProcessor(processor) => handleRequest(listing + processor)
        case Message(data, entityID) =>
//          val actor = context.spawn(ProcessingActor(),data)
          context.log.info(s"Context logging")
          sharding.entityRefFor(processorTypeKey, entityID) ! Data(data, "100")
          //          shardRegion ! ShardingEnvelope(entityID, Data(data, "100", context.self))
          Behaviors.same
        case Replied =>
          println("Replied")
          Behaviors.same
      }

    handleRequest(Set.empty)
  }
}
