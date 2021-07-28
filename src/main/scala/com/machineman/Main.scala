package com.machineman

import akka.actor.typed.ActorSystem
import com.machineman.actor.Supervisor
import com.machineman.actor.Supervisor.{Command, Message}
import com.typesafe.config.ConfigFactory

object Main extends App {
//  println(ServiceProvider.serviceProvider)


//  val message = ConfigFactory.load("application.conf").getString("msg")
  val superVisorActor : ActorSystem[Command] = ActorSystem(Supervisor(), "Machineman", ConfigFactory.load("application.conf"))
//  superVisorActor ! Message("1Hi!", "hi")
//  Thread.sleep(10000)
//  superVisorActor ! Message("2hello", "hello")
//  Thread.sleep(10000)
//  superVisorActor ! Message("3hey", "hey")
//  Thread.sleep(10000)
//  superVisorActor ! Message("4hey hasher", "hasher")
//  Thread.sleep(10000)
//  superVisorActor ! Message("5Welcome", "welcome")
//  Thread.sleep(10000)
//  superVisorActor ! Message("6You are great to see this", "hey")
  var i = 2000
  while (true) {
    val entityId = "h"+i;
    superVisorActor ! Message(s"$i Message $i", entityId)
    i=i+1
    Thread.sleep(2000)


  }



}
