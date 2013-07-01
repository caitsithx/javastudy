/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.nio.server;

import java.io.IOException;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class NIOSrvMain {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		NIOTCPSrvSocketWrapper tcpSrv = new NIOTCPSrvSocketWrapper();
		tcpSrv.start();
	}

}
