/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */
package lixl.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 * 
 */
public class NIOTCPClientSocketWrapper {

	final static Charset UTF8 = Charset.forName("UTF-8");
	private int port;
	private String remoteHostName;

	private SocketChannel clientChannel;

	private Selector selector;
	
	private SelectionKey selectKey;

	private ClientOpsListener listener = null;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	

	private ExecutorService executor = Executors.newSingleThreadExecutor();
	/**
	 * @return the clientChannel
	 */
	public SocketChannel getClientChannel() {
		return this.clientChannel;
	}

	/**
	 * @return the selector
	 */
	public Selector getSelector() {
		return this.selector;
	}
	
	public void setOpsListener(ClientOpsListener p_listener) {
		try {
			lock.writeLock().lock();
			listener = p_listener;
		} finally {
			lock.writeLock().unlock();
		}
	}

	public String getRemoteHostName() {
		return remoteHostName;
	}

	public void setRemoteHostName(String hostName) {
		this.remoteHostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public void open() {
		try {
			selector = Selector.open();

			clientChannel = SocketChannel.open();
			clientChannel.configureBlocking(false);

			clientChannel.connect(new InetSocketAddress(remoteHostName, port));
			clientChannel.register(selector,
					SelectionKey.OP_CONNECT);

			NIOClientRunner l_clientRunner = new NIOClientRunner();
			l_clientRunner.setTcpClient(this);
			executor.execute(l_clientRunner);
		} catch (Exception ex) {
			System.err.print(ex.getMessage());
			// sdsd
		}
	}
	
	public synchronized void enableRW() {
		if(selectKey == null) {
			return;
		}
		
		if(((selectKey.interestOps() & SelectionKey.OP_WRITE) != 0)
				&&
				((selectKey.interestOps() & SelectionKey.OP_READ) != 0)){
			return;
		}
		
//		selectKey.cancel();
		selectKey.interestOps( selectKey.interestOps() | SelectionKey.OP_WRITE);
		selector.wakeup();

	}
	
	public synchronized void disableW() {
		if(selectKey == null) {
			return;
		}
		
		if(((selectKey.interestOps() & SelectionKey.OP_WRITE) == 0)
				&&
				((selectKey.interestOps() & SelectionKey.OP_READ) != 0)){
			return;
		}
		
//		selectKey.cancel();
		selectKey.interestOps( selectKey.interestOps() - SelectionKey.OP_WRITE);
		selector.wakeup();

	}
	
	public synchronized void startup() {
		try {
			selectKey =	clientChannel.register(selector, SelectionKey.OP_READ);
		} catch (ClosedChannelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

	}

	private DefaultOpsHandler openHandler = new DefaultOpsHandler();
	
	public void selectFor() throws IOException {
		try {
			lock.readLock().lock();
			selectFor(this, listener);
		} finally {
			lock.readLock().unlock();
		}
	}
	

	public static void selectFor(NIOTCPClientSocketWrapper p_clientWrapper, ClientOpsListener p_listener) throws IOException {
		p_clientWrapper.getSelector().select();
		for (Iterator<SelectionKey> iterator = p_clientWrapper.getSelector().selectedKeys().iterator(); iterator.hasNext();) {
			SelectionKey l_selectedKey = iterator.next();
			iterator.remove();
			if (l_selectedKey.isConnectable()) {
				p_listener.onConnectable(p_clientWrapper);
			}
			if (l_selectedKey.isWritable()) {
				p_listener.onWritable(p_clientWrapper);
			} else if (l_selectedKey.isReadable()) {
				p_listener.onReadable(p_clientWrapper);
			}
		}
	}

	/**
	 * 
	 */
	public void close() {
		try {
			selector.close();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		try {
			clientChannel.close();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
}
