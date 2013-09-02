/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.nio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import lixl.jstd.nio.commons.NIOOpsListener;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class ServerOpsHandler implements NIOOpsListener<NIOTCPServer> {

   /*
    * (non-Javadoc)
    * 
    * @see
    * lixl.jstd.nio.commons.NIOOpsListener#onConnectable(lixl.jstd.nio.commons
    * .NIOSocketWrapper)
    */
   @Override
   public void onConnectable(NIOTCPServer p_serverTCP) {

   }

   @Override
   public void onReadable(NIOTCPServer p_serverTCP,
	   SelectionKey p_selectedKey) {
	SocketChannel l_sktChannel = p_serverTCP.getChannels().get(p_selectedKey.attachment());
	ByteBuffer l_bb = ByteBuffer.allocate(1000);
	l_bb.clear();
	// @TODO handle long request end.
	try {
	   l_sktChannel.read(l_bb);
	} catch (IOException ex) {
	   ex.printStackTrace();
	}

	l_bb.flip();
	p_serverTCP.getAppServer().onRequest(l_bb);
   }

   @Override
   public void onWritable(NIOTCPServer p_serverTCP, SelectionKey p_selectKey) {
	SocketChannel l_sktChannel = p_serverTCP.getChannels().get(p_selectKey.attachment());

	try {
	   l_sktChannel.write(p_serverTCP.getAppServer().getResponse());
	} catch (IOException ex) {
	   ex.printStackTrace();
	}
   }

   @Override
   public void onAcceptable(NIOTCPServer p_serverTCP) {
	try {
		SocketChannel l_sktChannel = p_serverTCP.getServerSocket().accept();
		l_sktChannel.configureBlocking(false);
		Integer l_channelIndex = p_serverTCP.getNextIndex();
		p_serverTCP.getChannels().put(l_channelIndex, l_sktChannel);
		SelectionKey l_selKey = l_sktChannel.register(p_serverTCP.getSelector(),
			SelectionKey.OP_READ);
		l_selKey.attach(l_channelIndex);  
	} catch (IOException l_ex) {
	   l_ex.printStackTrace();
	}
   }
}
