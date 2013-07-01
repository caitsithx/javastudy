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
	private NIOTCPSrvSocketWrapper tcpServer;
	


	/**
	 * @param p_tcpServer the tcpServer to set
	 */
	public void setTcpServer(NIOTCPSrvSocketWrapper p_tcpServer) {
		this.tcpServer = p_tcpServer;
	}



	@Override
	public void run() { // normally in a new Thread
		try {
			while (!Thread.interrupted()) {
				Set<SelectionKey> l_selectedKeys = tcpServer.selectKeys();
				Iterator<SelectionKey> l_skItr = l_selectedKeys.iterator();
				while (l_skItr.hasNext()) {
					try {
						SelectionKey l_sk = l_skItr.next();
						if(l_sk.isValid()) {
							try {
							tcpServer.dispatch(l_sk);
							} catch (IOException ex) {
								tcpServer.closeSocketChannel((Integer)l_sk.attachment());
							}
						}
					} finally {
						l_skItr.remove();
					}
				}
			}
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

}