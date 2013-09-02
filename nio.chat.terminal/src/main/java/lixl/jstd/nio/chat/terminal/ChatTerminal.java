/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.jstd.nio.chat.terminal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;

import lixl.nio.client.NIOTCPClientSocketWrapper;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class ChatTerminal implements Runnable {
	
	public ChatTerminal() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		BufferedReader l_rdr = new BufferedReader(
		new InputStreamReader(System.in));
		while(true) {
			try {
				networkHandler.appendMsg(l_rdr.readLine());
//				tCPClient.enableRW();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @return
	 */
	public Writer getConsoleWriter() {
		return new PrintWriter(System.out);
	}

	private ChatClientOpsHandler networkHandler;
	/**
	 * @param p_chatHandler
	 */
	public void setChatNetworkHandler(ChatClientOpsHandler p_chatHandler) {
		networkHandler = p_chatHandler;
	}

	private NIOTCPClientSocketWrapper tCPClient;
	/**
	 * @param p_tcpClient
	 */
	public void setTCPClient(NIOTCPClientSocketWrapper p_tcpClient) {
		tCPClient = p_tcpClient;	
	}

}
