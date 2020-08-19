package com.lrx.rpc.netty

import java.io.{Externalizable, ObjectInput, ObjectOutput}

import com.lrx.rpc.{RpcCallContext, RpcEndpoint, RpcEnv}
import com.lrx.util.Utils

/**
  * An [[RpcEndpoint]] for remote [[RpcEnv]]s to query if an `RpcEndpoint` exists.
  *
  * This is used when setting up a remote endpoint reference.
  */
private[netty] class RpcEndpointVerifier(override val rpcEnv: RpcEnv, dispatcher: Dispatcher)
  extends RpcEndpoint {

	override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
		case c: RpcEndpointVerifier.CheckExistence => context.reply(dispatcher.verify(c.getName))
	}
}

private[netty] object RpcEndpointVerifier {
	val NAME = "endpoint-verifier"

	/** A message used to ask the remote [[RpcEndpointVerifier]] if an `RpcEndpoint` exists. */
	class CheckExistence(var name: String) extends Serializable with Externalizable {
		def getName: String = name

		def this() = this(null) // For deserialization only

		override def writeExternal(out: ObjectOutput): Unit = Utils.tryOrIOException {
			out.writeUTF(name)
		}

		override def readExternal(in: ObjectInput): Unit = Utils.tryOrIOException {
			name = in.readUTF()
		}
	}

	def createCheckExistence(name: String) = {
		new CheckExistence(name)
	}

}