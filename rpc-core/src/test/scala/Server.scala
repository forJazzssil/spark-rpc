import com.lrx.RpcConf
import com.lrx.rpc._
import com.lrx.rpc.netty.NettyRpcEnvFactory

object Server {
	def main(args: Array[String]): Unit = {
		val host = "localhost"
		val serverconf = RpcEnvServerConfig(new RpcConf(), "server", host, 5005)
		val serverEnv = NettyRpcEnvFactory.create(serverconf)
		val serverEndpoint: RpcEndpoint = new ServerEndpoint(serverEnv)
		serverEnv.setupEndpoint("server", serverEndpoint)
		serverEnv.awaitTermination()
	}
}

class ServerEndpoint(override val rpcEnv: RpcEnv) extends RpcEndpoint{
	override def onStart(): Unit = {
		println("server start")
		println(self.address.toSparkURL)
	}

	override def onStop(): Unit = println("sever stop")

	override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
		case Hello(msg) => {
			println(msg)
			context.reply(s"hi $msg")
		}
	}
}


case class Hello(msg: String)