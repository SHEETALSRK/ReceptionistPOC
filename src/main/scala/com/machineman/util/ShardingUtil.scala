package com.machineman.util

import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.scaladsl.ClusterSharding

object ShardingUtil {

  def getShardingExtension(system : ActorSystem[Nothing]) : ClusterSharding = ClusterSharding(system)


}
