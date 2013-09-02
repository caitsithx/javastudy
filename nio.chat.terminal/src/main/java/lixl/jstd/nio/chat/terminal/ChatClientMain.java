/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.jstd.nio.chat.terminal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lixl.nio.client.NIOTCPClientSocketWrapper;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class ChatClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChatTerminal l_terminal = new ChatTerminal();
		
		ChatClientOpsHandler l_chatHandler = new ChatClientOpsHandler();
		l_chatHandler.setWriter(l_terminal.getConsoleWriter());
		l_terminal.setChatNetworkHandler(l_chatHandler);
		
        String l_hostName = "localhost";
        int l_port = 8003;
        ExecutorService l_clientRunner = new ThreadPoolExecutor(
                2, 
                5,
                30, 
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        
            NIOTCPClientSocketWrapper l_tcpClient = new NIOTCPClientSocketWrapper();
            l_tcpClient.setRemoteHostName(l_hostName);
            l_tcpClient.setPort(l_port);
            l_chatHandler.setTcpClient(l_tcpClient);
            
            l_tcpClient.setOpsListener(l_chatHandler);
            l_terminal.setTCPClient(l_tcpClient);
            
            l_clientRunner.submit(l_terminal);
            l_tcpClient.open();
	}

}
