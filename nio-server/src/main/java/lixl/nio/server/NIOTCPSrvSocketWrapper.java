package lixl.nio.server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 *
 */
public class NIOTCPSrvSocketWrapper {
	
	private final ConcurrentHashMap<Integer, SocketChannel> channels = new ConcurrentHashMap<Integer, SocketChannel>();
	
	private final Applicative appServer = new Applicative();
	private final ExecutorService execService = Executors.newCachedThreadPool();
	
	private int index = 0;
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
	private Selector selector;
	private ServerSocketChannel serverSocket;
	
	private int port = 8003;
	String host = "localhost";
	private int selectorCount = 1;
	
	/**
	 * @param p_selectorCount the selectorCount to set
	 */
	public void setSelectorCount(int p_selectorCount) {
		this.selectorCount = p_selectorCount;
	}

	/**
	 * @param p_port the port to set
	 */
	public void setPort(int p_port) {
		this.port = p_port;
	}

	/**
	 * @param p_host the host to set
	 */
	public void setHost(String p_host) {
		this.host = p_host;
	}
	
	public void start () throws IOException {
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress(
				InetAddress.getByName("localhost"), port);
		serverSocket.socket().bind(address);

		serverSocket.configureBlocking(false);
		SelectionKey sk = serverSocket.register(selector,
				SelectionKey.OP_ACCEPT);
		
		for (int i = 0; i < selectorCount; i++) {
			ServerHandler l_selector = new ServerHandler();
			l_selector.setTcpServer(this);
			
			execService.submit(l_selector);
		}

		// logger.debug("-->Start serverSocket.register!");

		// sk.attach(new Acceptor());
		// logger.debug("-->attach(new Acceptor()!");
	}
	

	int getNextIndex() {
		int l_retVal = 0;

		try {
			try {
				LOCK.writeLock().lock();
				if (index < Integer.MAX_VALUE) {
					index++;
				} else {
					index = 0;
				}

				LOCK.readLock().lock();
			} finally {
				LOCK.writeLock().lock();
			}
			return index;
		} finally {
			LOCK.readLock().unlock();
		}
	}
	

	void dispatch(SelectionKey k) throws IOException {
		if (k.isAcceptable()) {
			accept(k);
		} else if (k.isReadable()) {
			read(k);
			write(k);
		} else if (k.isWritable()) {
			System.out.println("isWritale()");
			write(k);
		}
	}

	public void accept(SelectionKey k) throws IOException {
			SocketChannel l_sktChannel = serverSocket.accept();
			l_sktChannel.configureBlocking(false);
			Integer l_channelIndex = getNextIndex();
			channels.put(l_channelIndex, l_sktChannel);
			SelectionKey l_selKey = l_sktChannel.register(selector, SelectionKey.OP_READ);
			l_selKey.attach(l_channelIndex);
	}

	public void read(SelectionKey selectKey) throws IOException {
		SocketChannel l_sktChannel = channels.get(selectKey.attachment());
		ByteBuffer l_bb = ByteBuffer.allocate(1000);
		l_bb.clear();
		// @TODO handle long request end.
		l_sktChannel.read(l_bb);
		l_bb.flip();
		getAppServer().onRequest(l_bb);
	}

	public void write(SelectionKey selectKey) throws IOException {
		SocketChannel l_sktChannel = channels.get(selectKey.attachment());

		l_sktChannel.write(getAppServer().getResponse());
	}

	Applicative getAppServer() {
		return appServer;
	}


	/**
	 * @return
	 * @throws IOException 
	 */
	public Set<SelectionKey> selectKeys() throws IOException {
		int l_count = selector.select();
		return selector.selectedKeys();
	}

	/**
	 * @param p_attachment
	 */
	public void closeSocketChannel(Integer p_attachment) {
		SocketChannel l_channel = channels.remove(p_attachment);
		if(l_channel != null) {
			try {
				l_channel.close();
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}
}
