package lixl.nio.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lixl.jstd.nio.commons.NIOOpsListener;
import lixl.jstd.nio.commons.NIOSocketWrapper;
import lixl.jstd.nio.commons.NIOUtil;

/*
 * Copyright (c) 2013 - xiaoliang.li@gemalto.com.
 *
 */

/**
 * @author <a href="mailto:xiaoliang.li@gemalto.com">lixl </a>
 * 
 */
public class NIOTCPServer implements NIOSocketWrapper {

   private final ConcurrentHashMap<Integer, SocketChannel> channels = new ConcurrentHashMap<Integer, SocketChannel>();

   private final Applicative appServer = new Applicative();

   private final ExecutorService execService = Executors.newCachedThreadPool();

   private int index = 0;

   private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

   private Selector selector;

   private ServerSocketChannel serverSocket;

   private int port = 8003;

   String host = "localhost";

   private int handlerCount = 1;
   
   private ServerOpsHandler opsHandler;

   /**
    * @return the serverSocket
    */
   public ServerSocketChannel getServerSocket() {
	return this.serverSocket;
   }

   /**
    * @param p_selectorCount
    *           the selectorCount to set
    */
   public void setHandlerCount(int p_selectorCount) {
	this.handlerCount = p_selectorCount;
   }

   /**
    * @param p_port
    *           the port to set
    */
   public void setPort(int p_port) {
	this.port = p_port;
   }

   /**
    * @param p_host
    *           the host to set
    */
   public void setHost(String p_host) {
	this.host = p_host;
   }

   Applicative getAppServer() {
	return appServer;
   }

   /**
    * @throws IOException
    * 
    */
   public void start() throws IOException {
	selector = Selector.open();
	serverSocket = ServerSocketChannel.open();
	InetSocketAddress address = new InetSocketAddress(
		InetAddress.getByName("localhost"), port);
	serverSocket.socket().bind(address);

	serverSocket.configureBlocking(false);
	SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);

	for (int i = 0; i < handlerCount; i++) {
	   ServerHandler l_selector = new ServerHandler();
	   l_selector.setTcpServer(this);

	   execService.submit(l_selector);
	}

	// logger.debug("-->Start serverSocket.register!");

	// sk.attach(new Acceptor());
	// logger.debug("-->attach(new Acceptor()!");
   }

   int getNextIndex() {
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
		LOCK.writeLock().unlock();
	   }
	   return index;
	} finally {
	   LOCK.readLock().unlock();
	}
   }

   @Override
   public Selector getSelector() {
	return selector;
   }

   @Override
   public void selectFor() {
	try {
	   NIOUtil.selectFor(this, opsHandler);
	} catch (IOException ex2) {
	   // TODO Auto-generated catch block
	   ex2.printStackTrace();
	}
   }

   void shutdown() {
	try {
	   this.selector.close();
	   this.execService.shutdown();
	   this.serverSocket.close();
	} catch (IOException ex) {
	   ex.printStackTrace();
	}

	Iterator<SocketChannel> l_chnItr = channels.values().iterator();
	while (l_chnItr.hasNext()) {
	   try {
		l_chnItr.next().close();
	   } catch (IOException ex) {
		// TODO Auto-generated catch block
		ex.printStackTrace();
	   }
	   l_chnItr.remove();
	}

   }

   /**
    * @param p_attachment
    */
   public void closeSocketChannel(Integer p_attachment) {
	SocketChannel l_channel = channels.remove(p_attachment);
	if (l_channel != null) {
	   try {
		l_channel.close();
	   } catch (IOException ex) {
		// TODO Auto-generated catch block
		ex.printStackTrace();
	   }
	}
   }
   
   protected final Map<Integer, SocketChannel> getChannels() {
	return this.channels;
   }

}
