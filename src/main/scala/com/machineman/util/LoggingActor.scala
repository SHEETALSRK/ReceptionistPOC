package com.machineman.util

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.scalalogging.Logger

object LoggingActor {

  sealed trait Logger

  case class Info(message: String) extends Logger

  case class Debug(message: String) extends Logger

  case class Warn(message: String) extends Logger

  case class Error(message: String) extends Logger

  val logger = Logger("POCLogging")

  def apply() : Behavior[Logger] = logActor

  private def logActor: Behavior[Logger] = {
    Behaviors.receive { (context, message) =>
      message match {
        case Info(message) =>
          logger.info(message)
          Behaviors.same
        case Debug(message) =>
          logger.debug(message)
          Behaviors.same
        case Warn(message) =>
          logger.warn(message)
          Behaviors.same
        case Error(message) =>
          logger.error(message)
          Behaviors.same
      }
    }
  }
}
