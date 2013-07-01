package lixl.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NIOClient implements Runnable {
	final static Charset UTF8 = Charset.forName("UTF-8");

	private String hostName;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	private int port;
	SocketChannel clientChannel;
	Selector selector;

	NIOClient() {
	}


	public void run() {
		try {
			selector = Selector.open();
			
			clientChannel = SocketChannel.open();
			
			clientChannel.configureBlocking(false);
			InetSocketAddress l_srvAddr = new InetSocketAddress(hostName, port);
			clientChannel.connect(l_srvAddr);
			SelectionKey l_key = clientChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

			int l_readCount = 10;
			while (l_readCount != 0) {
				selector.select();
				for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext();) {
					SelectionKey l_selectedKey = iterator.next();
					iterator.remove();
					if (l_selectedKey.isConnectable()) {
						if(!clientChannel.isConnected()) {
							clientChannel.finishConnect();
						}
					}
					if (l_selectedKey.isWritable()) {
						clientChannel.write(UTF8.encode("I am client"));
					} else if (l_selectedKey.isReadable()) {
						ByteBuffer l_bb = ByteBuffer.allocate(1000);
						clientChannel.read(l_bb);
						l_bb.flip();
						System.out.println(UTF8.decode(l_bb));
						l_readCount --;
					}
				}
			}
		} catch(Exception ex) {
			System.err.print(ex.getMessage());
			//sdsd
		} finally {
			try {
				clientChannel.close();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}
}
