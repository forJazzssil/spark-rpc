import com.lrx.RpcConf
import com.lrx.rpc.netty.NettyRpcEnvFactory
import com.lrx.rpc._

object Client {
	def main(args: Array[String]): Unit = {
		val host = "localhost"
		val clientconf = RpcEnvClientConfig(new RpcConf(), "client")
		val clientEnv = NettyRpcEnvFactory.create(clientconf)
		val serverRef:RpcEndpointRef = clientEnv.setupEndpointRef(RpcAddress(host, 5005), "server")
		val result = serverRef.askWithRetry[String](Hello("叮当"))
		print(result)

	}

	def async()={
		val rpcConf = new RpcConf()
		val config = RpcEnvClientConfig(rpcConf, "client")
		val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(config)
		val endPointRef: RpcEndpointRef = rpcEnv.setupEndpointRef(RpcAddress("localhost", 5005), "server")
		val result = endPointRef.askWithRetry[String](Hello("neo"))
		println(result)
	}
}
