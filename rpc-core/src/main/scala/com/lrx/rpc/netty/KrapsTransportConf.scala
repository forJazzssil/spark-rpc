package com.lrx.rpc.netty


import java.lang
import java.util.Map

import com.lrx.RpcConf
import org.apache.spark.network.util.{ConfigProvider, TransportConf}

/**
  * Created by xu.zhang on 7/30/17.
  */
object KrapsTransportConf {
	/**
	  * Specifies an upper bound on the number of Netty threads that Kraps requires by default.
	  * In practice, only 2-4 cores should be required to transfer roughly 10 Gb/s, and each core
	  * that we use will have an initial overhead of roughly 32 MB of off-heap memory, which comes
	  * at a premium.
	  */
	private val MAX_DEFAULT_NETTY_THREADS = 8

	/**
	  * Utility for creating a [[KrapsTransportConf]] from a [[com.lrx.RpcConf]].
	  *
	  * @param conf           the [[RpcConf]]
	  * @param module         the module name
	  * @param numUsableCores if nonzero, this will restrict the server and client threads to only
	  *                       use the given number of cores, rather than all of the machine's cores.
	  *                       This restriction will only occur if these properties are not already set.
	  */
	def fromSparkConf(conf: RpcConf, module: String, numUsableCores: Int = 0): TransportConf = {
		// Specify thread configuration based on our JVM's allocation of cores (rather than necessarily
		// assuming we have all the machine's cores).
		// NB: Only set if serverThreads/clientThreads not already set.
		val numThreads = defaultNumThreads(numUsableCores)
		conf.setIfMissing(s"spark.$module.io.serverThreads", numThreads.toString)
		conf.setIfMissing(s"spark.$module.io.clientThreads", numThreads.toString)

		new TransportConf(module, new ConfigProvider {
			override def get(name: String): String = conf.get(name)

			override def getAll: lang.Iterable[Map.Entry[String, String]] = ???
		})
	}

	/**
	  * Returns the default number of threads for both the Netty client and server thread pools.
	  * If numUsableCores is 0, we will use Runtime get an approximate number of available cores.
	  */
	private def defaultNumThreads(numUsableCores: Int): Int = {
		val availableCores =
			if (numUsableCores > 0) numUsableCores else Runtime.getRuntime.availableProcessors()
		math.min(availableCores, MAX_DEFAULT_NETTY_THREADS)
	}
}