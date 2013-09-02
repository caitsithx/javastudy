/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.nio.client;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class DefaultOpsHandler implements ClientOpsListener{
	
	@Override
	public void onWritable(NIOTCPClientSocketWrapper p_clientWrapper) {
		//nothing
	}
	
	@Override
	public void onReadable(NIOTCPClientSocketWrapper p_chn) {
		//nothing
	}
	
	@Override
	public void onConnectable(NIOTCPClientSocketWrapper p_chn) {
		if(!p_chn.getClientChannel().isConnected()) {
			try {
				p_chn.getClientChannel().finishConnect();
				p_chn.startup();
//				p_chn.getClientChannel().register(
//				p_chn.getSelector(),
//				SelectionKey.OP_READ);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}							
	}
}
