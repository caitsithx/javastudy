package lixl.nio.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class ServerHandler implements Runnable {
	

//	public static void main(String[] args) throws IOException {
//
//		ExecutorService l_netSrvRunner = Executors.newSingleThreadExecutor();
//		l_netSrvRunner.submit(l_netServer);
//
//	}
//	
	private NIOTCPServer tcpServer;
	


	/**
	 * @param p_tcpServer the tcpServer to set
	 */
	public void setTcpServer(NIOTCPServer p_tcpServer) {
		this.tcpServer = p_tcpServer;
	}



	@Override
	public void run() { // normally in a new Thread
			tcpServer.selectFor();
	}

}