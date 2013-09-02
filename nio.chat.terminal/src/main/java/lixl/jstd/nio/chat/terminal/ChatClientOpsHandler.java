/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.jstd.nio.chat.terminal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;

import lixl.nio.client.DefaultOpsHandler;
import lixl.nio.client.NIOTCPClientSocketWrapper;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class ChatClientOpsHandler extends DefaultOpsHandler{
	final static Charset UTF8 = Charset.forName("UTF-8");
	
	java.io.Writer writer;


	StringBuffer outBuffer = new StringBuffer();
	
	NIOTCPClientSocketWrapper tcpClient;
	/**
	 * @param p_tcpClient the tcpClient to set
	 */
	public void setTcpClient(NIOTCPClientSocketWrapper p_tcpClient) {
		this.tcpClient = p_tcpClient;
	}

	/**
	 * @param p_writer the writer to set
	 */
	public void setWriter(java.io.Writer p_writer) {
		this.writer = p_writer;
	}
	
	public void appendMsg(String p_msg){
		outBuffer.append(p_msg);
//		onWritable(tcpClient);
		tcpClient.enableRW();
	}
	
	public void onWritable(NIOTCPClientSocketWrapper p_clientWrapper) {
		while(outBuffer.length() > 0) {
			String l_tmp =
			outBuffer.substring(0, outBuffer.length());
			outBuffer.delete(0, l_tmp.length());
			
			ByteBuffer l_bb = UTF8.encode(l_tmp);
			
			try {
				while(l_bb.remaining() > 0) {
					if(p_clientWrapper.getClientChannel().write(l_bb) == 0) {
						p_clientWrapper.enableRW();
					}
				}
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		
		p_clientWrapper.disableW();
	}
	
	public void onReadable(NIOTCPClientSocketWrapper p_chn) {
		ByteBuffer l_bb = ByteBuffer.allocate(200);
		
		StringBuilder l_sb = new StringBuilder();
		try {
			while(p_chn.getClientChannel().read(l_bb) > 0) {
				l_bb.flip();
				l_sb.append(UTF8.decode(l_bb));
				l_bb.clear();
			}
			
			if(l_sb.length() > 0)
				writer.write(l_sb.toString());
			writer.flush();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		

	}
}
