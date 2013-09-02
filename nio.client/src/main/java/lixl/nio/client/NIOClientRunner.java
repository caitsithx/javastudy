package lixl.nio.client;

import java.io.IOException;


public class NIOClientRunner implements Runnable {
	private NIOTCPClientSocketWrapper tcpClient;

	/**
	 * @param p_tcpClient the tcpClient to set
	 */
	public void setTcpClient(NIOTCPClientSocketWrapper p_tcpClient) {
		this.tcpClient = p_tcpClient;
	}

	public void run() {
		try {
		while (true) {
				tcpClient.selectFor();
		}
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}finally {
			tcpClient.close();
		}
	}
}
